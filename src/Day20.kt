import kotlin.math.abs
import kotlin.math.max

// https://adventofcode.com/2021/day/20

fun main() {
  class Image(cols: Int, rows: Int, numbers: Array<Int>, mask: String) {
    val cols = cols
    val rows = rows
    var numbers = numbers
    val mask = mask


    fun getMaskAt(index : Int) : Int {
      val point = indexToPoint(index)
      val x = point.first
      val y = point.second

      val values = listOf(
        Pair(x, y),
        Pair(x + 1, y),
        Pair(x + 2, y),
        Pair(x, y + 1),
        Pair(x + 1, y + 1),
        Pair(x + 2, y + 1),
        Pair(x, y + 2),
        Pair(x + 1, y + 2),
        Pair(x + 2, y + 2),
      ).map {
        get(it.first, it.second)!!
      }

      val maskIndex = values.joinToString("").toInt(2)
      return if (mask[maskIndex] == '#') 1 else 0
    }

    fun applyMask() : Image {
      var newNumbers = mutableListOf<Int>()

      numbers.forEachIndexed { index, v ->
        val point = indexToPoint(index)

        if (point.first >= cols - 2 || point.second >= rows - 2) return@forEachIndexed

        newNumbers.add(getMaskAt(index))
      }

      return Image(cols - 2, rows - 2, newNumbers.toTypedArray(), mask)
    }

    fun grow(growBy: Int = 2, fillWith: Int = 0) : Image {
      val newCols = cols + 2 * growBy

      val newNumbers = mutableListOf<Int>()

      newNumbers.addAll((1..(newCols * growBy)).map { fillWith })

      for (row in getRows()) {
        newNumbers.addAll((1..growBy).map { fillWith })
        newNumbers.addAll(row)
        newNumbers.addAll((1..growBy).map { fillWith })
      }

      newNumbers.addAll((1..(newCols * growBy)).map { fillWith })

      return Image(newCols, rows + 2 * growBy, newNumbers.toTypedArray(), mask)
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

    fun getRows() : List<List<Int>> {
      val result = mutableListOf<List<Int>>()

      for (row in 0 until rows) {
        result.add(numbers.slice((row * cols) until row * cols + cols))
      }

      return result
    }

    fun printMe() {
      numbers.forEachIndexed { index, v ->
        if (index % cols == 0) {
          println("")
          print("${index / rows }: ")
        }

        if (v == 0) {
          print(".")
        } else {
          print("#")
        }
      }

      println()
    }
  }

  fun readImage() : Image {
    var mask: String? = null
    val numbers = mutableListOf<Int>()
    var cols = 0

    for (line in readInput("Day20")) {
      if (mask == null) {
        mask = line
        continue
      }

      if (line.isEmpty()) {
        continue
      }

      var nums = line.split("").filter { it != "" }.map { if (it == ".") 0 else 1  }
      cols = nums.count()
      numbers.addAll(nums)
    }

    return Image(cols, numbers.count() / cols, numbers.toTypedArray(), mask!!)
  }

  fun maskStep(image: Image, step: Int) : Image {
    return image.grow(3, if (step == 0 && image.mask.first() == '#') 1 else 0).applyMask()
//    return image.grow(3 , 0).applyMask()
  }

  fun part1() {
    val image = readImage()
    image.printMe()

    val imageAfterOneStep = maskStep(image, 0)
    imageAfterOneStep.printMe()
    val imageAfterTwoSteps = maskStep(imageAfterOneStep, 1)
    imageAfterTwoSteps.printMe()
    println(imageAfterTwoSteps.numbers.count { it == 1 })
  }

  fun part2() {
    var image = readImage()
//    image.printMe()

    for (step in 0 until 50) {
      println("")
      print("Step $step:")
      image = image.grow(2, if (image.mask.first() == '.' || step % 2 == 0) 0 else 1)
//      image.printMe()
      image = image.applyMask()
//      image.printMe()
    }

    println(image.numbers.count { it == 1 })
  }

//  part1()
  part2()
}
