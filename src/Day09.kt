// https://adventofcode.com/2021/day/9

fun main() {
  class Table(cols: Int, rows: Int, numbers: Array<Int>) {
    val cols = cols
    val rows = rows
    val numbers = numbers

    fun computeBasins() {
      val lowPointIndices = computeLowPoints()
      val basinSizes = mutableListOf<Int>()
      val basins = mutableListOf<List<Pair<Int, Int>>>()
      val basinIndices = mutableListOf<Int>()

      lowPointIndices.forEach {
        val basin = computeBasin(it)

        basinSizes.add(basin.size)
        basins.add(basin.map { indexToPoint(it) })
        basinIndices.addAll(basin)
      }

      numbers.forEachIndexed { index, v ->
        if (index % cols == 0) {
          println("")
          print("${index / rows }: ")
        }

        if (lowPointIndices.contains(index)) {
          print(" ^$v^ ")
        } else if (basinIndices.contains(index)) {
          print(" *$v* ")
        } else print("  $v  ")
      }

      println()
      println("Result: ${basinSizes.sortedDescending().take(3).fold(1) { acc, i -> acc * i }}")
    }

    fun computeBasin(index : Int, currentBasin : List<Int> = listOf()) : List<Int> {
      var newBasin = currentBasin.toMutableList()
      newBasin.add(index)

      getNeighbours(index).filter { it.second < 9 }.forEach {
        if (!newBasin.contains(it.first)) {
          newBasin = computeBasin(it.first, newBasin).toMutableList()
        }
      }

      return newBasin
    }

    fun computeLowPoints() : List<Int> {
      var result = 0
      val lowPointIndices = mutableListOf<Int>()
      val surelyNotLowPointIndices = mutableMapOf<Int, Boolean>()

      numbers.forEachIndexed { i, value ->
        if (surelyNotLowPointIndices.getOrDefault(i, false)) return@forEachIndexed

       if (value == 0) {
          lowPointIndices.add(i)
          result++

          getNeighbours(i).forEach { surelyNotLowPointIndices[it.first] = true }

          return@forEachIndexed
        }

        if (value < 9) {
          val neighbours = getNeighbours(i)
          if (neighbours.all { it.second > value }) {
            lowPointIndices.add(i)
            result += value + 1
            neighbours.forEach { surelyNotLowPointIndices[it.first] = true }
          }
        }
      }

      return lowPointIndices
    }

    // Returns list of pairs of <Index, Value>
    fun getNeighbours(index : Int) : List<Pair<Int, Int>> {
      val point = indexToPoint(index)
      val x = point.first
      val y = point.second

      return listOf(
        Pair(x - 1, y),
        Pair(x + 1, y),
        Pair(x, y - 1),
        Pair(x, y + 1)
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
  }

  fun part1() {
    val numbers = mutableListOf<Int>()
    var cols = -1
    var rows = 0

    for (line in readInput("Day09")) {
      cols = line.length
      rows++
      numbers.addAll(line.split("").filter { it.isNotEmpty() }.map { it.toInt() })
    }

    val t = Table(cols, rows, numbers.toTypedArray())

    t.computeLowPoints()
  }

  fun part2() {
    val numbers = mutableListOf<Int>()
    var cols = -1
    var rows = 0

    for (line in readInput("Day09")) {
      cols = line.length
      rows++
      numbers.addAll(line.split("").filter { it.isNotEmpty() }.map { it.toInt() })
    }

    val t = Table(cols, rows, numbers.toTypedArray())

    t.computeBasins()
  }

  part1()
  part2()
}
