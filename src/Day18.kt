import java.util.*
import kotlin.math.ceil

// https://adventofcode.com/2021/day/18

enum class SNumOperation {
  EXPLODE, SPLIT
}

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

    fun magnitude() : Long {
      if (isLeaf()) {
        return value!!.toLong()
      }

      return 3 * this.left!!.magnitude() + 2 * this.right!!.magnitude()
    }

    fun explode() {
      val closestLeft = findClosestDigit(true)
      val closestRight = findClosestDigit(false)
      println("Closest left: $closestLeft, closest right: $closestRight")

      if (closestLeft != null) {
        closestLeft.value = closestLeft.value!! + this.left!!.value!!
      }

      if (closestRight != null) {
        closestRight.value = closestRight.value!! + this.right!!.value!!
      }

      this.left = null
      this.right = null
      this.value = 0
    }

    fun findClosestDigit(left: Boolean) : SNum? {
      var current = this.parent
      var last = this

      while (current != null && (if (left) current.left == last else current.right == last)) {
        last = current
        current = current.parent
      }

      if (current == null) {
        return null
      }

      current = if (left) current.left else current.right

      while (!current!!.isLeaf()) {
        current = if (left) current.right else current.left
      }

      return current
    }

    fun split() : SNum {
      val left = SNum()
      val right = SNum()

      left.value = this.value!! / 2
      left.parent = this
      right.value = ceil(this.value!! / 2.0).toInt()
      right.parent = this
      this.value = null
      this.left = left
      this.right = right

      return this
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

//    println("Parsed $string -> $root")
    return root!!
  }

  fun findNodeToExplode(node: SNum, level: Int = 0) : SNum? {
    if (node.isLeaf()) {
      return null
    }

    if (node.left != null && node.left!!.isLeaf() && node.right != null && node.right!!.isLeaf() && level >= 4) {
      return node
    }

    val leftResult = findNodeToExplode(node.left!!, level + 1)
    if (leftResult != null) {
      return leftResult
    }

    return findNodeToExplode(node.right!!, level + 1)
  }

  fun findNodeToSplit(node: SNum) : SNum? {
    if (node.isLeaf()) {
      return if (node.value!! > 9) {
       node
      } else {
        null
      }
    }

    val leftResult = findNodeToSplit(node.left!!)
    if (leftResult != null) {
      return leftResult
    }

    return findNodeToSplit(node.right!!)
  }

  fun reduce(s: SNum) {
    while (true) {
      var nte = findNodeToExplode(s)
      if (nte != null) {
        println("Exploding $nte in $s")
        nte.explode()
        continue
      }
      var nts = findNodeToSplit(s)
      if (nts != null) {
        println("Splitted ${nts} into ${nts.split()}")
        continue
      }

      break
    }
  }

  fun readNumber() : SNum {
    var current:SNum? = null
    for (string in readInput("Day18")) {
      if (current == null) {
        current = parseNum(string)
        reduce(current!!)
        continue
      }

      var currentString = current.toString()
      var parsed = parseNum(string)
      current = current!!.add(parseNum(string))
      reduce(current!!)

      println("  $currentString")
      println("+ $parsed")
      println("= $current")
      println()
    }

    return current!!
  }

  fun part1() {
//    val s = parseNum("[[[[4,3],4],4],[7,[[8,4],9]]]").add(parseNum("[1,1]"))
    val s = readNumber()


    println(s)
    println(s.magnitude())
  }

  fun part2() {
  }

  part1()
  part2()
}
