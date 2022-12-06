import java.util.*

// https://adventofcode.com/2021/day/10

fun main() {
  fun checkLine(line: String) : Int {
    val stack = Stack<Char>()

    for (c in line) {
      when (c) {
        '(', '[', '{', '<' -> stack.push(c)
        ')' -> if (stack.peek() == '(') stack.pop() else return 3
        ']' -> if (stack.peek() == '[') stack.pop() else return 57
        '}' -> if (stack.peek() == '{') stack.pop() else return 1197
        '>' -> if (stack.peek() == '<') stack.pop() else return 25137
        else -> println("Ooops")
      }
    }

    return 0
  }

  fun part1() {
    var result = 0

    for (line in readInput("Day10")) {
      result += checkLine(line)
    }

    println(result)
  }

  fun isIncomplete(line: String) : Boolean {
    val stack = Stack<Char>()

    for (c in line) {
      when (c) {
        '(', '[', '{', '<' -> stack.push(c)
        ')' -> if (stack.peek() == '(') stack.pop() else return false
        ']' -> if (stack.peek() == '[') stack.pop() else return false
        '}' -> if (stack.peek() == '{') stack.pop() else return false
        '>' -> if (stack.peek() == '<') stack.pop() else return false
        else -> println("Ooops")
      }
    }

    return !stack.empty()
  }

  fun complete(line: String) : String {
    val stack = Stack<Char>()
    var completionString = ""

    for (c in line) {
      when (c) {
        '(', '[', '{', '<' -> stack.push(c)
        ')' -> if (stack.peek() == '(') stack.pop()
        ']' -> if (stack.peek() == '[') stack.pop()
        '}' -> if (stack.peek() == '{') stack.pop()
        '>' -> if (stack.peek() == '<') stack.pop()
      }
    }

    while (!stack.empty()) {
      when (stack.pop()) {
        '(' -> completionString += ')'
        '[' -> completionString += ']'
        '{' -> completionString += '}'
        '<' -> completionString += '>'
        else -> println("Ooops")
      }
    }

    return completionString
  }

  fun completionScore(completion : String) : Long {
    var score: Long = 0
    for (c in completion) {
      score *= 5

      score += when (c) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> 0
      }
    }

    return score
  }

  fun part2() {
    val incomplete = mutableListOf<String>()

    for (line in readInput("Day10")) {
      if (isIncomplete(line)) {
        incomplete.add(line)
      }
    }

    var scores = mutableListOf<Long>()
    for (line in incomplete) {
      val completion = complete(line)
      val score = completionScore(completion)
      println("Line:$line, completion:$completion, score:$score")
      scores.add(score)
    }

    println(scores.sorted()[scores.size / 2])
  }

  part1()
  part2()
}
