import java.util.*

// https://adventofcode.com/2021/day/14

fun main() {
  fun step(string: String, rules: Map<String, String>) : String {
    var result = ""
    string.zipWithNext().forEach {
      result += it.first
      result += rules.getOrDefault("${it.first}${it.second}", "")
    }

    result += string.last()

    return result
  }

  fun part1() {
    val rules = mutableMapOf<String, String>()
    var initial = ""

    for (line in readInput("Day14")) {
      if (line.isEmpty()) continue

      if (initial.isEmpty()) {
        initial = line
        continue
      }

      val rule = line.split(" -> ")
      rules[rule.first()] = rule.last()
    }

    var result = initial
    println(initial)
    for (i in 1..10) {
      result = step(result, rules)
      println("After $i steps")
    }

    val occurences = result.groupingBy { it }.eachCount().values
    println(occurences.maxOrNull()!! - occurences.minOrNull()!!)
  }

  fun part2() {
  }

  part1()
  part2()
}
