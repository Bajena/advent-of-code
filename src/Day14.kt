import java.util.*

// https://adventofcode.com/2021/day/14

fun main() {
  fun step(string: String, rules: Map<String, Char>) : String {
    var result = ""
    string.zipWithNext().forEach {
      result += it.first
      result += rules.getOrDefault("${it.first}${it.second}", "")
    }

    result += string.last()

    return result
  }
  var cache = mutableMapOf<Pair<String, Int>, Map<Char, Long>>()
  fun compute(pair: String, rules: Map<String, Char>, maxLevel : Int = 10, current: Map<Char, Long> = mutableMapOf(), level: Int = 0) : Map<Char, Long> {
    var newMap = current.toMutableMap()

    if (cache.getOrDefault(Pair(pair, level), null) != null) {
      cache[Pair(pair, level)]!!.forEach { (letter, count) -> newMap[letter] = newMap.getOrDefault(letter, 0) + count }
      return newMap
    }


    val product = rules.getOrDefault(pair, null)

    if (level == maxLevel || product == null) {
      newMap[pair.first()] = newMap.getOrDefault(pair.first(), 0) + 1
      newMap[pair.last()] = newMap.getOrDefault(pair.last(), 0) + 1

//      cache[Pair(pair, level)] = newMap

      return newMap
    }

    var resultLeft = compute("${pair.first()}$product", rules, maxLevel, newMap, level + 1).toMutableMap()
    cache[Pair("${pair.first()}$product", level + 1)] = resultLeft
    var resultRight = compute("$product${pair.last()}", rules, maxLevel, newMap, level + 1).toMutableMap()
    cache[Pair("$product${pair.last()}", level + 1)] = resultRight

    resultLeft.forEach { (letter, count) -> newMap[letter] = newMap.getOrDefault(letter, 0) + count }
    resultRight.forEach { (letter, count) -> newMap[letter] = newMap.getOrDefault(letter, 0) + count }
    newMap[product] = newMap.getOrDefault(product, 0) - 1

    return newMap
  }

  fun part1() {
    val rules = mutableMapOf<String, Char>()
    var initial = ""

    for (line in readInput("Day14")) {
      if (line.isEmpty()) continue

      if (initial.isEmpty()) {
        initial = line
        continue
      }

      val rule = line.split(" -> ")
      rules[rule.first()] = rule.last().first()
    }

    var result = initial
    println(initial)
    for (i in 1..2) {
      result = step(result, rules)
    }

    val occurences = result.groupingBy { it }.eachCount()
    println(occurences)
    println(occurences.values.maxOrNull()!! - occurences.values.minOrNull()!!)
  }

  fun part2() {
    val rules = mutableMapOf<String, Char>()
    var initial = ""

    for (line in readInput("Day14")) {
      if (line.isEmpty()) continue

      if (initial.isEmpty()) {
        initial = line
        continue
      }

      val rule = line.split(" -> ")
      rules[rule.first()] = rule.last().first()
    }

    var result = mutableMapOf<Char, Long>()

    initial.zipWithNext().forEach {
      var counts = compute("${it.first}${it.second}", rules, 40)
      counts.forEach { (letter, count) -> result[letter] = result.getOrDefault(letter, 0) + count }

      result[it.second] = result.getOrDefault(it.second, 0) - 1
    }
    result[initial.last()] = result.getOrDefault(initial.last(), 0) + 1

    println(result.values.maxOrNull()!! - result.values.minOrNull()!!)
  }

  part1()
  part2()
}
