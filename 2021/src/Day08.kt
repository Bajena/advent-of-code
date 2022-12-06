import kotlin.math.abs

// https://adventofcode.com/2021/day/8

fun main() {
  fun part1() {
    var result = 0

    for (line in readInput("Day08")) {
      val data = line.split(" | ")
      result += data[1].split(" ").filter { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }.size
    }

    println(result)
  }


  fun computeEntry(input: List<String>, output: List<String>): Int {
    val one = input.find { it.length == 2 }!!
    val four = input.find { it.length == 4 }!!
    val seven = input.find { it.length == 3 }!!
    val eight = input.find { it.length == 7 }!!

    val lFiveSegments = input.filter { it.length == 5 }.toMutableList()
    val lSixSegments = input.filter { it.length == 6 }.toMutableList()

    val three = lFiveSegments.find { it.replace("[$one]".toRegex(), "").length == 3 }!!
    lFiveSegments.remove(three)
    val six = lSixSegments.find { it.replace("[$one]".toRegex(), "").length == 5 }!!
    lSixSegments.remove(six)

    val topRightSegment = eight.replace("[$six]".toRegex(), "")
    val five = lFiveSegments.find { !it.contains(topRightSegment) }!!
    lFiveSegments.remove(five)
    val two = lFiveSegments.first()!!

    val nine = lSixSegments.find { three.replace("[$it]".toRegex(), "").length == 0 }!!
    lSixSegments.remove(nine)
    val zero = lSixSegments.last()!!

    val numbers = mapOf(zero to 0, one to 1, two to 2, three to 3, four to 4, five to 5, six to 6, seven to 7, eight to 8, nine to 9)

    return output.map { numbers[it] }.joinToString("").toInt()
  }

  fun part2() {
    var allData = mutableListOf<Pair<List<String>, List<String>>>()

    for (line in readInput("Day08")) {
      val data = line.split(" | ")
      val input = data[0].split(" ").map { it.toSortedSet().joinToString("") }
      val output = data[1].split(" ").map { it.toSortedSet().joinToString("") }

      allData.add(Pair(input, output))
    }

    var result : Long = 0

    for (data in allData) {
      result += computeEntry(data.first, data.second)
    }

    println(result)
  }

  part1()
  part2()
}
