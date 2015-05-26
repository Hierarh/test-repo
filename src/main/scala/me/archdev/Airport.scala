package me.archdev

class AirportIsFullException() extends RuntimeException("Airport is full")

class Airport(delay: Int, var slot: Option[Plane] = None) {

  var takeoffTime: Int = 0
  var landedPlanesCounter = 0
  var takeoffedPlanesCounter = 0

  def landPlane(plane: Plane)(implicit currentTime: Int) = {
    if(slot.isDefined) throw new AirportIsFullException()
    takeoffTime = currentTime + delay
    slot = Some(plane)
    landedPlanesCounter += 1
  }

  def tryTakeoffPlane(implicit currentTime: Int) =
    if(takeoffTime <= currentTime && slot.isDefined) {
      Statistic.planeLeft(slot.get)
      slot = None
      takeoffedPlanesCounter += 1
    }

}