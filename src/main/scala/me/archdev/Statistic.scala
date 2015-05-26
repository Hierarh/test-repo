package me.archdev

import scala.collection.mutable

object Statistic {

  var planesInSystem = 0
  var planesStatistic: mutable.Map[Int, Int] = mutable.Map()
  var planesList: mutable.Set[Plane] = mutable.Set()

  def planeLeft(plane: Plane) = {
    planesInSystem -= 1
    planesList.add(plane)
  }
  def planeAdded() = planesInSystem += 1

  def countPlanesStatistic() = {
    if (!planesStatistic.isDefinedAt(planesInSystem)) planesStatistic.put(planesInSystem, 0)
    planesStatistic.put(planesInSystem, planesStatistic(planesInSystem) + 1)
  }

}
