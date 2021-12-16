import kotlin.math.abs

// https://adventofcode.com/2021/day/7

fun main() {
  fun part1() {
    var crabPositions = listOf<Int>()

    for (line in readInput("Day07")) {
      crabPositions = line.split(",").map { it.toInt() }.sorted()
    }

    println(crabPositions.size)

    var minSum = Int.MAX_VALUE
    var result = 0
    val min = crabPositions.minOrNull()!!
    val max = crabPositions.maxOrNull()!!
    for (i in min..max) {
      var sum = 0
      for (position in crabPositions) {
        sum += abs(position - i)
      }

      if (sum < minSum) {
        minSum = sum
        result = i
      }
      println("Result for position $i is $sum. Current min is $minSum")
    }

    println(result)
    println(minSum)
  }


  fun part2() {
    var crabPositions = listOf<Int>()

    for (line in readInput("Day07")) {
      crabPositions = line.split(",").map { it.toInt() }.sorted()
    }

    println(crabPositions.size)

    var minSum = Int.MAX_VALUE
    var result = 0
    val min = crabPositions.minOrNull()!!
    val max = crabPositions.maxOrNull()!!
    for (i in min..max) {
      var sum = 0
      for (position in crabPositions) {
        val distance = abs(position - i)
        sum += distance * (distance + 1) / 2
      }

      if (sum < minSum) {
        minSum = sum
        result = i
      }
      println("Result for position $i is $sum. Current min is $minSum")
    }

    println(result)
    println(minSum)
  }

  part1()
  part2()
}
