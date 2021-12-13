import java.lang.Math.abs

// https://adventofcode.com/2021/day/4

fun main() {
  fun part1() {
    val lines = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    for (line in readInput("Day05")) {
      val points = line.split(" -> ").map { points ->
        val values = points.split(",").map { it.toInt() }
        Pair(values.first(), values.last())
      }

      val a = points.first()
      val b = points.last()
      if (a.first == b.first || a.second == b.second) lines.add(Pair(a, b))
    }

    val selectedPoints = mutableMapOf<Pair<Int, Int>, Int>().withDefault { 0 }
    lines.forEach { line ->
      val a = line.first
      val b = line.second

      if (a.first == b.first) {
        val range = if (b.second >= a.second) a.second..b.second else b.second..a.second
        for (i in range) {
          val p = Pair(a.first, i)
          selectedPoints[p] = selectedPoints.getValue(p) + 1
        }
      }

      if (a.second == b.second) {
        val range = if (b.first >= a.first) a.first..b.first else b.first..a.first
        for (i in range) {
          val p = Pair(i, a.second)
          selectedPoints[p] = selectedPoints.getValue(p) + 1
        }
      }
    }

    println(selectedPoints.values.filter { it > 1 }.size)
  }


  fun part2() {
    val lines = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    for (line in readInput("Day05")) {
      val points = line.split(" -> ").map { points ->
        val values = points.split(",").map { it.toInt() }
        Pair(values.first(), values.last())
      }

      val a = points.first()
      val b = points.last()
      if (a.first == b.first || a.second == b.second ||  kotlin.math.abs(a.first - b.first) == kotlin.math.abs(a.second - b.second)) lines.add(Pair(a, b))
    }

    val selectedPoints = mutableMapOf<Pair<Int, Int>, Int>().withDefault { 0 }
    lines.forEach { line ->
      val a = line.first
      val b = line.second

      if (a.first == b.first) {
        val range = if (b.second >= a.second) a.second..b.second else b.second..a.second
        for (i in range) {
          val p = Pair(a.first, i)
          selectedPoints[p] = selectedPoints.getValue(p) + 1
        }
      } else if (a.second == b.second) {
        val range = if (b.first >= a.first) a.first..b.first else b.first..a.first
        for (i in range) {
          val p = Pair(i, a.second)
          selectedPoints[p] = selectedPoints.getValue(p) + 1
        }
      } else if (kotlin.math.abs(a.first - b.first) == kotlin.math.abs(a.second - b.second)) {
        val smallerXPoint =  if (a.first < b.first) a else b
        val biggerXPoint =  if (a.first >= b.first) a else b
        val length = kotlin.math.abs(a.first - b.first)
        val direction = if (biggerXPoint.second - smallerXPoint.second > 0) 1 else -1
        for (i in 0..length) {
          val p = Pair(smallerXPoint.first + i, smallerXPoint.second + i * direction)
          selectedPoints[p] = selectedPoints.getValue(p) + 1
        }
      }
    }

    println(selectedPoints.values.filter { it > 1 }.size)
  }

  part1()
  part2()
}
