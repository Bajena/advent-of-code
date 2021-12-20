import java.util.*

// https://adventofcode.com/2021/day/12

fun main() {
  class Graph(vertexes: Set<String>, edges: List<Pair<String, String>>) {
    val edges = edges
    val bigCaves = vertexes.filter { isBigCave(it) }
    val smallCaves = vertexes.filter { !isBigCave(it) }
    val START = "start"
    val END = "end"

    val paths = mutableListOf<List<String>>()

    fun isBigCave(v : String) : Boolean {
      return v.first().isUpperCase()
    }

    fun findPaths(currentPath: List<String> = listOf(START)) {
      if (currentPath.contains(END)) {
        paths.add(currentPath)
        return
      }

      allowedVertexes(currentPath.last()).filter { bigCaves.contains(it) || !currentPath.contains(it) }.forEach {
        val path = currentPath.toMutableList()
        path.add(it)
        findPaths(path)
      }
    }

    fun findPathsPart2(currentPath: List<String> = listOf(START)) {
      if (currentPath.contains(END)) {
        paths.add(currentPath)
        return
      }

      allowedVertexes(currentPath.last()).filter { av ->
        av != START && (isBigCave(av) || !currentPath.contains(av) || currentPath.filter { !isBigCave(it) && it != START }.groupingBy { it }.eachCount().filterValues { it > 1 }.isEmpty())
      }.forEach {
        val path = currentPath.toMutableList()
        path.add(it)
        findPathsPart2(path)
      }
    }

    fun allowedVertexes(vertex: String) : List<String> {
      return edges.mapNotNull {
        if (it.first == vertex) {
          it.second
        } else if (it.second == vertex) {
          it.first
        } else {
          null
        }
      }
    }
  }

  fun buildGraph() : Graph {
    var edges = mutableListOf<Pair<String, String>>()
    var vertexes = mutableSetOf<String>()

    for (edge in readInput("Day12")) {
      var verts = edge.split("-")
      edges.add(Pair(verts.first(), verts.last()))
      vertexes.addAll(verts)
    }

    return Graph(vertexes, edges)
  }

  fun part1() {
    val g = buildGraph()
    g.findPaths()
    println(g.paths.size)
  }

  fun part2() {
    val g = buildGraph()

    g.findPathsPart2()
    for (p in g.paths) {
      println("${p.joinToString(",")}")
    }

    println(g.paths.size)
  }

//  part1()
  part2()
}
