// https://adventofcode.com/2021/day/1

import java.io.File

fun main() {
  fun readLines(fileName: String): List<String> = File(fileName).bufferedReader().readLines()

  fun part1() {
    var previous = 0
    var result = 0

    for ((index, l) in readLines("src/Day01.txt").withIndex()) {
      val value = l.toInt()
      if (index > 0 && value > previous) {
        result++
      }

      previous = value
    }

    println(result)
  }

  fun part2() {
    var previous = 0
    var result = 0

    for ((index, values) in readLines("src/Day01.txt").windowed(3).withIndex()) {
      val value = values.sumOf { it.toInt() }
      if (index > 0 && value > previous) {
        result++
      }

      previous = value
    }

    println(result)
  }

  part1()
  part2()
}
