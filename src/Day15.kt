import java.util.*

// https://adventofcode.com/2021/day/14

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

  fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val S: MutableSet<T> = mutableSetOf() // a subset of vertices, for which we know the true distance

    val delta = graph.vertices.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    delta[start] = 0

    val previous: MutableMap<T, T?> = graph.vertices.map { it to null }.toMap().toMutableMap()

    while (S != graph.vertices) {
      val v: T = delta.filter { !S.contains(it.key) }.minByOrNull { it.value }!!.key

      graph.edges.getValue(v).minus(S).forEach { neighbor ->
        val deltaValue = delta.getOrDefault(v, null)
        val weightValue = graph.weights.getOrDefault(Pair(v, neighbor), null)
        if (deltaValue == null || weightValue == null) return@forEach

        val newPath = deltaValue + weightValue

        if (newPath < delta.getValue(neighbor)) {
          delta[neighbor] = newPath
          previous[neighbor] = v
        }
      }

      S.add(v)
    }

    return previous.toMap()
  }

  fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
      if (shortestPathTree[end] == null) return listOf(end)
      return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
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

    for (line in readInput("Day15")) {
      cols = line.length
      rows++
      numbers.addAll(line.split("").filter { it.isNotEmpty() }.map { it.toInt() })
    }

    return Table(cols, rows, numbers.toTypedArray())
  }

  fun part1() {
    val t = readTable()

    val g = t.toGraph()

    val start = "V0"
    val shortestPathTree = dijkstra(g, start)
    val path = shortestPath(shortestPathTree, start, "V${t.numbers.count() - 1}")

    println(path)
    var pathSum = 0
    path.zipWithNext().forEach {
      var weight = g.weights[Pair(it.first, it.second)]
      println("$it: $weight")
      pathSum += weight!!
    }

    t.printMe(path)
    println(pathSum)
  }

  fun part2() {
  }

  part1()
  part2()
}
