// https://adventofcode.com/2021/day/2

import java.io.File

fun main() {
  fun readLines(fileName: String): List<String>
      = File(fileName).bufferedReader().readLines()

  fun part1() {
    var horizontal = 0
    var depth = 0

    for (commandString in readLines("src/Day02.txt")) {
      val command = commandString.split(" ")[0]
      val value = commandString.split(" ")[1].toInt()

      when (command) {
        "forward" -> horizontal += value
        "up" -> depth -= value
        "down" -> depth += value
        else -> {
          throw Exception("Not a valid command: $command")
        }
      }
    }

    println("Horizontal: $horizontal, Depth: $depth, Result: ${horizontal * depth}")
  }

  fun part2() {
    var horizontal = 0
    var depth = 0
    var aim = 0

    for (commandString in readLines("src/Day02.txt")) {
      val command = commandString.split(" ")[0]
      val value = commandString.split(" ")[1].toInt()

      when (command) {
        "forward" -> {
          horizontal += value
          depth +=  aim * value
        }
        "up" -> aim -= value
        "down" -> aim += value
        else -> {
          throw Exception("Not a valid command: $command")
        }
      }
    }

    println("Horizontal: $horizontal, Depth: $depth, Result: ${horizontal * depth}")
  }

  part1()
  part2()
}
