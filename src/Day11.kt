import java.util.*

// https://adventofcode.com/2021/day/11

fun main() {
  class Table(cols: Int, rows: Int, numbers: Array<Int>) {
    val cols = cols
    val rows = rows
    var numbers = numbers

    var fired = mutableSetOf<Int>()
    var toFire = mutableSetOf<Int>()

    fun step() : Int {
      // 1. Increment all by 1 and collect indices of non fired > 9
      // 2. While there are non fired, > 9 for each of them do:
      // 2. a) Set to 0
      // 2. b) Mark as fired
      // 2. c) Bump neighbors by 1 and collect their indices if > 9 and not fired
      // 3. Mark all as not fired
      printMe()
      fired.clear()
      toFire.clear()

      var newNumbers = numbers.toMutableList().toTypedArray()
      numbers.forEachIndexed { index, value ->
        val newValue = value + 1
        newNumbers[index] = newValue

        if (newValue > 9) {
          toFire.add(index)
        }
      }
      numbers = newNumbers

      printMe()

      while (toFire.isNotEmpty()) {
        toFire.forEach { index ->
          numbers[index] = 0
          fired.add(index)
        }
        printMe()

        var newToFire = mutableSetOf<Int>()
        toFire.forEach { index ->
          getNeighbours(index).forEach { neighbour ->
            if (!fired.contains(neighbour.first)) {
              val newValue = neighbour.second + 1
              numbers[neighbour.first] = newValue

              if (newValue > 9) {
                newToFire.add(neighbour.first)
              }
            }
          }
        }

        toFire = newToFire
      }

      printMe()

      return fired.size
    }

    // Returns list of pairs of <Index, Value>
    fun getNeighbours(index : Int) : List<Pair<Int, Int>> {
      val point = indexToPoint(index)
      val x = point.first
      val y = point.second

      return listOf(
        Pair(x - 1, y - 1),
        Pair(x, y - 1),
        Pair(x + 1, y - 1),
        Pair(x - 1, y),
        Pair(x + 1, y),
        Pair(x - 1, y + 1),
        Pair(x, y + 1),
        Pair(x + 1, y + 1),
      ).filter { it.first >= 0 && it.first < cols && it.second >= 0 && it.second < rows }.map {
        Pair(pointToIndex(it.first, it.second), get(it.first, it.second)!!)
      }
    }

    fun pointToIndex(x : Int, y : Int) : Int {
      return y * cols + x
    }

    fun indexToPoint(index : Int) : Pair<Int, Int> {
      return Pair(index % cols, index / cols)
    }

    fun get(x : Int, y : Int) : Int? {
      return numbers.elementAtOrNull(y * cols + x)
    }

    fun printMe() {
      numbers.forEachIndexed { index, v ->
        if (index % cols == 0) {
          println("")
          print("${index / rows }: ")
        }

        if (fired.contains(index)) {
          print(" ^$v^ ")
        } else print("  $v  ")
      }

      println()
    }
  }

  fun part1() {
    val numbers = mutableListOf<Int>()
    var cols = -1
    var rows = 0

    for (line in readInput("Day11")) {
      cols = line.length
      rows++
      numbers.addAll(line.split("").filter { it.isNotEmpty() }.map { it.toInt() })
    }

    val t = Table(cols, rows, numbers.toTypedArray())

    var result = 0
    for (i in 1..100) result += t.step()

    println(result)
  }

  fun part2() {
  }

  part1()
  part2()
}
