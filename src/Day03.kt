// https://adventofcode.com/2021/day/3

import java.io.File
import kotlin.math.pow

fun main() {
  fun readLines(fileName: String): List<String>
      = File(fileName).bufferedReader().readLines()

  fun part1() {
    val ones = sortedMapOf<Int, Int>()
    val zeros = sortedMapOf<Int, Int>()

    for (line in readLines("src/Day03.txt")) {
      line.forEachIndexed { index, c ->
        when(c) {
          '1' -> ones[index] = ones.getOrDefault(index, 0) + 1
          '0' -> zeros[index] = zeros.getOrDefault(index, 0) + 1
        }
      }
    }

    println(ones)
    println(zeros)

    var gamma = ""
    var epsilon = ""

    ones.values.toList().zip(zeros.values.toList()) { onesInBit, zerosInBit -> if (onesInBit > zerosInBit) { gamma += '1'; epsilon += '0' } else { gamma += '0'; epsilon += '1' } }

    println(gamma)
    println(epsilon)
    println(gamma.toInt(2) * epsilon.toInt(2))
  }

  fun part2() {
    var decimals = mutableListOf<Int>()

    var numbers = mutableListOf<String>()
    var positions = 0
    for (line in readLines("src/Day03.txt")) {
      decimals.add(line.toInt(2))
      numbers.add(line)
      positions = line.length
    }

    var leftNumbers = numbers.toList()
    var i = 0

    while (leftNumbers.size > 1 && i < positions) {
      val withBit = leftNumbers.filter { it[i] == '1' }
      val withoutBit = leftNumbers.filter { it[i] == '0' }

      if (withBit.size >= withoutBit.size) {
        leftNumbers = withBit
      } else {
        leftNumbers = withoutBit
      }
      i++
    }

    var oxygen = leftNumbers.first().toInt(2)

    leftNumbers = numbers.toList()
    i = 0

    while (leftNumbers.size > 1 && i < positions) {
      val withBit = leftNumbers.filter { it[i] == '1' }
      val withoutBit = leftNumbers.filter { it[i] == '0' }

      if (withBit.size < withoutBit.size) {
        leftNumbers = withBit
      } else {
        leftNumbers = withoutBit
      }
      i++
    }
    val co2 = leftNumbers.first().toInt(2)

    println(oxygen * co2)
  }

  part1()
  part2()
}
