// https://adventofcode.com/2021/day/6

fun main() {
  fun part1() {
    var fishDays = mutableMapOf<Int, Long>().withDefault { 0 }
    val days = 256
    val newFishDayCount = 8
    val resetFishDayCount = 6

    for (line in readInput("Day06")) {
      for (fish in line.split(",")) {
        fishDays[fish.toInt()] = fishDays.getValue(fish.toInt()) + 1
      }
    }

    for (i in 1..days) {
      var newFishDays = mutableMapOf<Int, Long>().withDefault { 0 }

      for (days in fishDays.keys) {
        if (days == 0) {
          newFishDays[resetFishDayCount] = fishDays.getValue(days)
          newFishDays[newFishDayCount] = fishDays.getValue(days)
        } else {
          newFishDays[days - 1] = fishDays.getValue(days) + newFishDays.getValue(days - 1)
        }
      }

      fishDays = newFishDays.toSortedMap()
      val total = fishDays.values.sum()
      println("After $i days: $total - $fishDays")
    }

    println(fishDays.values.sum())
  }


  fun part2() {
  }

  part1()
  part2()
}
