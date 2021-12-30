import java.util.*

// https://adventofcode.com/2021/day/18

fun main() {
  class SNum {
    var left : SNum? = null
    var right : SNum? = null
    var parent : SNum? = null
    var value : Int? = null

    fun isLeaf() : Boolean {
      return (value != null)
    }

    fun add(b: SNum) : SNum {
      val n = SNum()
      n.left = this
      n.right = b

      this.parent = n
      b.parent = n

      return n
    }

    override fun toString(): String {
      if (isLeaf()) {
        return value.toString()
      }

      return "[${left},${right}]"
    }
  }

  fun parseNum(string: String) : SNum {
    val stack = Stack<SNum>()
    var root : SNum? = null

    for (c in string) {
      if (c == '[') {
        val node = SNum()
        if (!stack.empty()) {
          node.parent = stack.peek()
          if (node.parent!!.left == null) {
            node.parent!!.left = node
          } else {
            node.parent!!.right = node
          }
        }

        stack.push(node)

        if (root == null) root = node
        continue
      }

      val digit = c.digitToIntOrNull()
      if (digit != null) {
        val leaf = SNum()
        leaf.value = digit
        val currentNum = stack.peek()
        leaf.parent = currentNum
        if (currentNum.left == null) {
          currentNum.left = leaf
        } else {
          currentNum.right = leaf
        }

        continue
      }

      if (c == ']') {
        stack.pop()
      }
    }

    return root!!
  }

  fun part1() {
    val stringA = "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]"
    val stringB = "[[[[4,3],4],4],[7,[[8,4],9]]]"
    val s = parseNum(stringA).add(parseNum(stringB))
    println(s)
  }

  fun part2() {
  }

  part1()
  part2()
}