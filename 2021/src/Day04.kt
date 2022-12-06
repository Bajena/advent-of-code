// https://adventofcode.com/2021/day/4

fun main() {
  class Table(size: Int, tableNumbers : Collection<Int>) {
    val size = size
    var won = false
    val unselectedNumbers : MutableList<Int> = tableNumbers as MutableList<Int>
    val numberToPosition = mutableMapOf<Int, Pair<Int, Int>>()
    val selectedPositions = mutableListOf<Pair<Int, Int>>()

    init {
      unselectedNumbers.forEachIndexed { index, value ->
        numberToPosition[value] = Pair(index / size, index % size)
      }
    }

    fun selectNumber(number : Int) : Int {
      val position = numberToPosition[number]
      if (position != null) {
        selectedPositions.add(position)
        unselectedNumbers.remove(number)

        val value = checkWinningValue(number, position)
        if (value > -1) won = true
        return value
      }

      return -1
    }

    fun checkWinningValue(lastSelectedNumber : Int, lastSelectedPosition : Pair<Int, Int>) : Int {
      if (isColumnFull(lastSelectedPosition.second) || isRowFull(lastSelectedPosition.first)) {
        return unselectedNumbers.sum() * lastSelectedNumber
      }

      return -1
    }

    fun isColumnFull(column : Int): Boolean {
      for (i in 0 until size) {
        if (selectedPositions.indexOf(Pair(i, column)) > -1) continue

        return false
      }

      return true
    }

    fun isRowFull(row : Int): Boolean {
      for (i in 0 until size) {
        if (selectedPositions.indexOf(Pair(row, i)) > -1) continue

        return false
      }

      return true
    }
  }

  fun part1() {
    var numbers : Array<Int> = arrayOf()

    var tables = mutableListOf<Table>()
    var currentTableNumbers = mutableListOf<Int>()

    for (line in readInput("Day04")) {
      if (numbers.isEmpty()) {
        numbers = line.split(",").map { it.toInt() }.toTypedArray()
        continue
      }

      if (line.isEmpty()) {
        // First empty line after numbers
        if (currentTableNumbers.isEmpty()) continue

        tables.add(Table(5, currentTableNumbers))
        currentTableNumbers = mutableListOf<Int>()
        continue
      }

      currentTableNumbers.addAll(line.split(" ").filter { it != "" }.map { it.toInt() })
    }
    tables.add(Table(5, currentTableNumbers))

    numbers.forEach { number ->
      tables.forEach { table ->
        val result = table.selectNumber(number)

        if (result > -1) {
          println(result)
          return
        }
      }
    }
  }


  fun part2() {
    var numbers : Array<Int> = arrayOf()

    var tables = mutableListOf<Table>()
    var currentTableNumbers = mutableListOf<Int>()

    for (line in readInput("Day04")) {
      if (numbers.isEmpty()) {
        numbers = line.split(",").map { it.toInt() }.toTypedArray()
        continue
      }

      if (line.isEmpty()) {
        // First empty line after numbers
        if (currentTableNumbers.isEmpty()) continue

        tables.add(Table(5, currentTableNumbers))
        currentTableNumbers = mutableListOf<Int>()
        continue
      }

      currentTableNumbers.addAll(line.split(" ").filter { it != "" }.map { it.toInt() })
    }
    tables.add(Table(5, currentTableNumbers))

    var winningResult = -1
    numbers.forEach { number ->
      tables.filter { !it.won }.forEach { table ->
        val result = table.selectNumber(number)

        if (result > -1) {
          winningResult = result
        }
      }
    }

    println(winningResult)
  }

  part1()
  part2()
}
