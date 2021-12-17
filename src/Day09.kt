// https://adventofcode.com/2021/day/9

fun main() {
  class Table(cols: Int, rows: Int, numbers: Array<Int>) {
    val cols = cols
    val rows = rows
    val numbers = numbers

    fun computeLowPoints() {
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

      println("Result:$result, lowPointIndices:$lowPointIndices")
    }

    // Returns list of pairs of <Index, Value>
    fun getNeighbours(index : Int) : List<Pair<Int, Int>> {
      val point = indexToPoint(index)
      val x = point.first
      val y = point.second

      return listOf(
        Pair(pointToIndex(x - 1, y), get(x - 1, y)),
        Pair(pointToIndex(x + 1, y), get(x + 1, y)),
        Pair(pointToIndex(x, y - 1), get(x, y - 1)),
        Pair(pointToIndex(x, y + 1), get(x, y + 1)),
      ).filter { it.second != null } as List<Pair<Int, Int>>
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

    t.computeLowPoints()
  }

  part1()
  part2()
}
