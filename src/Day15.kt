import java.util.*

// https://adventofcode.com/2021/day/15

fun main() {
  fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .toSet()

  fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(predicate: (T) -> Boolean): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .filter(predicate)
    .toSet()

  data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>
  ) {
    constructor(weights: Map<Pair<T, T>, Int>): this(
      vertices = weights.keys.toList().getUniqueValuesFromPairs(),
      edges = weights.keys
        .groupBy { it.first }
        .mapValues { it.value.getUniqueValuesFromPairs { x -> x !== it.key } }
        .withDefault { emptySet() },
      weights = weights
    )
  }

  fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, Int> {
    class VertexDistancePairComparator<T> : Comparator<Pair<T, Int>> {
      override fun compare(o1: Pair<T, Int>, o2: Pair<T, Int>): Int {
        return o1.second.compareTo(o2.second)
      }
    }

    val priorityQueue = PriorityQueue<Pair<T, Int>>(VertexDistancePairComparator())

    val visited = mutableSetOf<T>()
    val totalRiskLevel = mutableMapOf<T, Int>()

    totalRiskLevel[start] = 0
    priorityQueue.add(Pair(start, 0))

    while (priorityQueue.isNotEmpty()){
      val point = priorityQueue.remove()
      val vertex = point.first
      val distance = point.second

      visited.add(vertex)
      if (totalRiskLevel.getOrDefault(vertex, Int.MAX_VALUE) < distance) continue

      graph.edges.getValue(vertex).minus(visited).forEach { neighbor ->
        val newRiskLevel = totalRiskLevel.getOrDefault(vertex, Int.MAX_VALUE) + graph.weights.getOrDefault(Pair(vertex, neighbor), null)!!
        if (newRiskLevel < totalRiskLevel.getOrDefault(neighbor, Int.MAX_VALUE)){
          totalRiskLevel[neighbor] = newRiskLevel
          priorityQueue.add(Pair(neighbor, newRiskLevel))
        }
      }
    }

    println(totalRiskLevel.count())

    return totalRiskLevel
  }

  class Table(cols: Int, rows: Int, numbers: Array<Int>) {
    val cols = cols
    val rows = rows
    var numbers = numbers

    // Returns list of pairs of <Index, Value>
    fun getNeighbours(index : Int) : List<Pair<Int, Int>> {
      val point = indexToPoint(index)
      val x = point.first
      val y = point.second

      return listOf(
        Pair(x, y - 1),
        Pair(x - 1, y),
        Pair(x + 1, y),
        Pair(x, y + 1),
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

    fun printMe(shortestPath: List<String> = listOf()) {
      numbers.forEachIndexed { index, v ->
        if (index % cols == 0) {
          println("")
          print("${index / rows }: ")
        }

        if (shortestPath.contains("V$index")) {
          print(" *$v* ")
        } else {
          print("  $v  ")
        }
      }

      println()
    }

    fun toGraph() : Graph<String> {
      val weights = mutableMapOf<Pair<String, String>, Int>()
      numbers.forEachIndexed { index, value ->
        var neighbours = getNeighbours(index)
        for (neighbour in neighbours) {
          weights[Pair("V$index", "V${neighbour.first}")] = neighbour.second
        }
      }

      return Graph(weights)
    }
  }

  fun readTable() : Table {
    val numbers = mutableListOf<Int>()
    var cols = -1
    var rows = 0
    for (i in 0..4) {
      for (sline in readInput("Day15")) {
        val ints = sline.split("").filter { it.isNotEmpty() }.map { it.toInt() }
        val lineNums = mutableListOf<Int>()
        for (j in 0..4) {
          lineNums.addAll(ints.map {
            val newNumber = it + i + j
            if (newNumber > 9) newNumber - 9 else newNumber
          })
        }
        cols = lineNums.count()
        rows++
        numbers.addAll(lineNums)
      }
    }

    return Table(cols, rows, numbers.toTypedArray())
  }

  fun part1() {
    val t = readTable()
    val g = t.toGraph()

    val start = "V0"
    val end = "V${t.numbers.count() - 1}"
    println(dijkstra(g, start).getOrDefault(end, null))
  }

  fun part2() {
  }

  part1()
  part2()
}
