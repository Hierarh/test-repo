package me.archdev

import scala.collection.mutable
import scala.util.Random

class PlaneSender(delay: Int, delta: Int, waitTime: Int, planeType: String)(var nextSendingTime: Int = delay + (if(delta > 0) Random.nextInt(delta) else 0))(implicit airport: Airport) {

  var sendingQueue: Map[Int, mutable.Queue[Plane]] = Map()
  var sendedPlanesCounter = 0
  var rejectedPlanesCounter = 0

  def trySendPlane(implicit currentTime: Int) = {
    checkSendingQueue
    if(nextSendingTime == currentTime) {
      val plane = new Plane(planeType)()
      Statistic.planeAdded()
      sendedPlanesCounter += 1
      try {
        airport.landPlane(plane)
      } catch {
        case e: AirportIsFullException =>
          putPlaneIntoQueue(plane)
      }
      nextSendingTime = currentTime + delay + (if(delta > 0) Random.nextInt(delta) else 0)
    }
  }

  private def checkSendingQueue(implicit currentTime: Int) = {
    if(sendingQueue.isDefinedAt(currentTime)) {
      sendingQueue(currentTime).foreach { plane =>
        if (plane.tryAvailableCounter > 0) {
          try airport.landPlane(plane)
          catch {
            case e: AirportIsFullException =>
              putPlaneIntoQueue(plane)
          }
        } else {
          rejectedPlanesCounter += 1
          Statistic.planeLeft(plane)
        }
      }
      sendingQueue.-=(currentTime)
    }
  }

  private def putPlaneIntoQueue(plane: Plane)(implicit currentTime: Int) = {
    plane.tryAvailableCounter -= 1
    val sendTime = currentTime + waitTime
    if(!sendingQueue.isDefinedAt(sendTime)) sendingQueue.+=(sendTime -> mutable.Queue())

    sendingQueue(sendTime).enqueue(plane)
    plane.additionalTime += waitTime
  }

}
