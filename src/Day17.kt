import java.util.*

// https://adventofcode.com/2021/day/17

fun main() {
  fun computeY(vy: Int, currX: Int, currVx : Int, currentStep : Int, minX: Int, maxX: Int, minY: Int, maxY: Int) : Int {
    var steps = currentStep
    var currentX = currX
    var currentVx = currVx

    var currentVy = vy - currentStep
    var currentY = ((2 * vy - currentStep + 1) / 2.0 * currentStep).toInt()

    while (currentX in minX..maxX && currentY > maxY) {
      steps++
      currentX += currentVx
      currentY += currentVy
      if (currentVx > 0) currentVx--
      currentVy--
    }

    return if (currentX in minX..maxX && currentY in minY..maxY) currentStep else -1
  }

  fun height(vy: Int, maxStep: Int) : Int {
    return (0..1000).maxOf { currentStep ->
      ((2 * vy - currentStep + 1) / 2.0 * currentStep).toInt()
    }
  }

  fun calcStepOfHit(vx: Int, minX: Int, maxX: Int, minY: Int, maxY: Int) : Int {
    var currentVx = vx
    var steps = 0
    var currentX = 0

    while (currentX < minX && currentVx > 0) {
      steps++
      currentX += currentVx
      if (currentVx > 0) currentVx--
    }

    if (currentX !in minX..maxX) return -1000
    var maxHeight = -1000

    for (vy in minY..-minY) {
      val maxStep = computeY(vy, currentX, currentVx, steps, minX, maxX, minY, maxY)

      if (maxStep > 0) {
        val height = height(vy, maxStep)
        if (height > maxHeight) {
          maxHeight = height
        }
        println("vx:$vx, vy:$vy, step:$maxStep, height:${height}")
      }
    }

    return maxHeight
  }

  fun calcCoords(vx: Int, minX: Int, maxX: Int, minY: Int, maxY: Int) : List<Pair<Int, Int>> {
    var currentVx = vx
    var steps = 0
    var currentX = 0

    while (currentX < minX && currentVx > 0) {
      steps++
      currentX += currentVx
      if (currentVx > 0) currentVx--
    }

    if (currentX !in minX..maxX) return listOf()

    var result = mutableListOf<Pair<Int, Int>>()
    for (vy in minY..-minY) {
      val maxStep = computeY(vy, currentX, currentVx, steps, minX, maxX, minY, maxY)

      if (maxStep > 0) {
        result.add(Pair(vx, vy))
        println("vx:$vx, vy:$vy")
      }
    }

    return result
  }

  fun part1() {
//    var minX = 20
//    var maxX = 30
//    var minY = -10
//    var maxY = -5
//
//    var minVx = 6 // https://www.wolframalpha.com/input/?i=x2%2Bx-40%3D0

    var minX = 25
    var maxX = 67
    var minY = -260
    var maxY = -200

    var minVx = 7 // https://www.wolframalpha.com/input/?i=x2%2Bx-50%3D0

    var maxHeight = -1000
    for (vx in minVx..maxX) {
      val height = calcStepOfHit(vx, minX, maxX, minY, maxY)
      if (height > maxHeight) maxHeight = height
    }

    println(maxHeight)
  }

  fun part2() {
    var minX = 20
    var maxX = 30
    var minY = -10
    var maxY = -5

    var minVx = 6 // https://www.wolframalpha.com/input/?i=x2%2Bx-40%3D0

//    var minX = 25
//    var maxX = 67
//    var minY = -260
//    var maxY = -200
//
//    var minVx = 7 // https://www.wolframalpha.com/input/?i=x2%2Bx-50%3D0

    var result = mutableListOf<Pair<Int, Int>>()
    for (vx in minVx..maxX) {
      result.addAll(calcCoords(vx, minX, maxX, minY, maxY))
    }

    println(result.count())
  }

//  part1()
  part2()
}
