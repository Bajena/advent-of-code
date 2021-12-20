import java.util.*

// https://adventofcode.com/2021/day/13

fun main() {
  class Table(dots: List<Pair<Int, Int>>) {
    var dots = dots

    fun fold(direction: String, at: Int) {
      var newDots = mutableListOf<Pair<Int, Int>>()
      when (direction) {
        "x" -> {
          dots.forEach {
            if (it.first > at) {
              newDots.add(Pair(it.first - 2 * (it.first - at), it.second))
            } else {
              newDots.add(it)
            }
          }
        }
        "y" -> {
          dots.forEach {
            if (it.second > at) {
              newDots.add(Pair(it.first, it.second - 2 * (it.second - at)))
            } else {
              newDots.add(it)
            }
          }
        }
        else -> println("OOOPS")
      }

      dots = newDots.distinct()
    }

    fun pointToIndex(x : Int, y : Int) : Int {
      return y * cols() + x
    }

    fun indexToPoint(index : Int) : Pair<Int, Int> {
      return Pair(index % cols(), index / cols())
    }

    fun cols() : Int {
      return dots.maxOf { it.first } + 1
    }

    fun rows() : Int {
      return dots.maxOf { it.second } + 1
    }

    fun printMe() {
      val dotIndices = dots.map { pointToIndex(it.first, it.second) }.sorted()
      (0..(cols() * rows() - 1)).forEach { index ->
        if (index % cols() == 0) {
          println("")
//          print("${index / rows() }: ")
        }

        if (dotIndices.contains(index)) {
          print("#")
        } else print(".")
      }

      println()
    }
  }

  fun part1() {
    val dots = mutableListOf<Pair<Int, Int>>()
    val folds = mutableListOf<Pair<String, Int>>()

    for (line in readInput("Day13")) {
      if (line.isEmpty()) continue

      if (line.startsWith("fold")) {
        val command = line.replace("fold along ", "").split("=")
        folds.add(Pair(command.first(), command.last().toInt()))
        continue
      }

      val dot = line.split(",").map { it.toInt() }
      dots.add(Pair(dot.first(), dot.last()))
    }

    println(dots)
    println(folds)

    val t = Table(dots)

    t.printMe()

    t.fold(folds.first().first, folds.first().second)

    t.printMe()

    println(t.dots.size)
  }

  fun part2() {
    val dots = mutableListOf<Pair<Int, Int>>()
    val folds = mutableListOf<Pair<String, Int>>()

    for (line in readInput("Day13")) {
      if (line.isEmpty()) continue

      if (line.startsWith("fold")) {
        val command = line.replace("fold along ", "").split("=")
        folds.add(Pair(command.first(), command.last().toInt()))
        continue
      }

      val dot = line.split(",").map { it.toInt() }
      dots.add(Pair(dot.first(), dot.last()))
    }

    println(dots)
    println(folds)

    val t = Table(dots)

    for (fold in folds) {
      t.fold(fold.first, fold.second)
    }

    t.printMe()

    println(t.dots.size)
  }

  part1()
  part2()
}
