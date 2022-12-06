import kotlin.math.abs
import kotlin.math.max

// https://adventofcode.com/2021/day/20

fun main() {
  class Dice {
    var points = 0
    var rolls = 0

    fun roll() : Int {
      rolls++

      if (points == 100) {
        points = 0
      }

      points++
      return points
    }
  }
  fun part1() {
//    var player1Position = 4 - 1

    var player1Position = 9 - 1
    var player1Score = 0
//    var player2Position = 8 - 1
        var player2Position = 4 - 1
    var player2Score = 0

    val dice = Dice()

    while (true) {
      val player1RollSum = arrayOf(dice.roll(), dice.roll(), dice.roll()).sum()
      player1Position = (player1Position + player1RollSum) % 10
      player1Score += player1Position + 1
      if (player1Score >= 1000) {
        break
      }

      val player2RollSum = arrayOf(dice.roll(), dice.roll(), dice.roll()).sum()
      player2Position = (player2Position + player2RollSum) % 10
      player2Score += player2Position + 1
      if (player2Score >= 1000) {
        break
      }

      println("Player 1 score: $player1Score, position: ${player1Position + 1}")
      println("Player 2 score: $player2Score, position: ${player2Position + 1}")
    }

    println("Player 1 score: $player1Score, position: $player1Position")
    println("Player 2 score: $player2Score, position: $player2Position")
    println("Rolls: ${dice.rolls}")

    val loserScore = if (player2Score > player1Score) player1Score else player2Score

    println(loserScore * dice.rolls)
  }

  var p1Wins: Long = 0
  var p2Wins: Long = 0

  fun move(player1Position: Int, player2Position: Int, isPlayer1Current: Boolean, player1Score: Int = 0, player2Score: Int = 0, clones: Long = 0) {
    val availableResults = arrayOf(3, 4, 5, 6, 7, 8, 9)
//    val availableResults = arrayOf(3, 4, 5, 4, 5, 6, 5, 6, 7, 4, 5, 6, 5, 6, 7, 6, 7, 8, 5, 6, 7, 6, 7, 8, 7, 8, 9)
    val occurences = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)


    if (isPlayer1Current) {
      if (player2Score >= 21) {
        p2Wins+=clones
        val thousand: Long = 1000000
        val zero: Long = 0
        if (p2Wins % thousand == zero) println("P2 wins: $p1Wins")
        return
      }

      for (sum in availableResults) {
        val newPosition = (player1Position + sum) % 10
        move(newPosition, player2Position, !isPlayer1Current, player1Score + newPosition + 1, player2Score, clones * occurences.get(sum)!!)
      }

      return
    }

    if (player1Score >= 21) {
      p1Wins+=clones
      val thousand: Long = 1000000
      val zero: Long = 0
      if (p1Wins % thousand == zero) println("P1 wins: $p1Wins")
      return
    }

    for (sum in availableResults) {
      val newPosition = (player2Position + sum) % 10
      move(player1Position, newPosition, !isPlayer1Current, player1Score, player2Score + newPosition + 1, clones * occurences.get(sum)!!)
    }
  }

  fun part2() {
//    var player1Position = 4 - 1
    var player1Position = 9 - 1
    var player1Score = 0
//    var player2Position = 8 - 1
    var player2Position = 4 - 1
    var player2Score = 0

    move(player1Position, player2Position, true, 0, 0, 1)

    println(p1Wins)
    println(p2Wins)
  }

//  part1()
  part2()
}
