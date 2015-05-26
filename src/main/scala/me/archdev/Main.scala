package me.archdev

object Main {

  def main(args: Array[String]) {
    implicit val airport = new Airport(delay = 2)
    val planeSender = new PlaneSender(delta = 0, delay = 10, waitTime = 4, planeType = "nonAirport")()
    val airportSender = new PlaneSender(delta = 0, delay = 10, waitTime = 1, planeType = "airport")()
    val totalTime = 1440

    for(i: Int <- 0 to totalTime) {
      airportSender.trySendPlane(i)
      planeSender.trySendPlane(i)
      airport.tryTakeoffPlane(i)
      Statistic.countPlanesStatistic()
    }

    println("Самолетов взлетело с аеропорта - " + airport.takeoffedPlanesCounter)
    println("Самолетов приземлилось на аеропорт - " + airport.landedPlanesCounter)
    println()
    println("Самолетов прибыло из аеропорта - " + airportSender.sendedPlanesCounter)
    println("Самолетов прибыло на посадку - " + planeSender.sendedPlanesCounter)
    println()
    println("Самолетов отправлено на доп. аеропорт - " + (planeSender.rejectedPlanesCounter + airportSender.rejectedPlanesCounter))
    println()
    Statistic.planesStatistic.foreach(data => println(s"${data._2} минут находилось ${data._1} самолетов в системе (${(100 / totalTime.toDouble * data._2.toDouble).round}%)"))
    println()
    println("Средняя количество времени проведенное при попытках посадки: " + (Statistic.planesList.filter(_.planeType == "nonAirport").foldLeft(0)(_ + _.additionalTime) / Statistic.planesList.count(_.planeType == "nonAirport")))
  }

}
