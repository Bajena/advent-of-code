import java.util.*

// https://adventofcode.com/2021/day/16

fun main() {
  class Packet(typeId: Int, version: Int) {
    val typeId = typeId
    val version = version

    var literal : Long = -1
    var packets = mutableListOf<Packet>()

    fun isLiteral() : Boolean {
      return (typeId == 4)
    }

    override fun toString() : String {
      if (isLiteral()) return "PACKET (v. $version): Literal with value:$literal"

      return "PACKET (v. $version): Operator with ${packets.count()} sub-packets: (${packets}"
    }

    fun totalVersions() : Int {
      return version + packets.sumOf { it.totalVersions() }
    }
  }

  fun hexToBin(input: String) : String {
    return input.map {
      val binary = it.toString().toInt(16).toString(2)
      binary.padStart(4, '0')
    }.joinToString("")
  }

  // Returns <Literal value, Rest of input>
  fun readLiteralPacket(inputWithoutHeader: String) : Pair<Long, String> {
    var rest = inputWithoutHeader
    var literalBinary = ""
    var lastLiteralPart = ""
    var literalParts = 0
    do {
      lastLiteralPart = rest.take(5)
      literalBinary += lastLiteralPart.drop(1)
      literalParts++
      rest = rest.drop(5)
    } while(lastLiteralPart.first() != '0')

//    rest = rest.drop((literalParts) % 4)

    return Pair(literalBinary.toLong(2), rest)
  }

  // Returns <Packet, Rest of input>
  fun readPacket(input: String, level: Int = 0) : Pair<Packet, String> {
    println("Reading packet from $input (level: $level)")

    var rest = input
    var version = rest.take(3).toInt(2)
    rest = rest.drop(3)

    var typeId = rest.take(3).toInt(2)
    rest = rest.drop(3)
    val packet = Packet(typeId, version)

    if (packet.isLiteral()) {
      val (literal, r) = readLiteralPacket(rest)
      packet.literal = literal
      rest = r
    } else {
      val lengthType = rest.take(1)
      rest = rest.drop(1)

      if (lengthType == "0") {
        val subPacketsLength = rest.take(15).toInt(2)
        rest = rest.drop(15)

        val initialRestLength = rest.length

        while (initialRestLength - rest.length < subPacketsLength) {
          val (p, r) = readPacket(rest, level + 1)
          packet.packets.add(p)
          rest = r
        }
      } else {
        val subPacketsCount = rest.take(11).toInt(2)
        rest = rest.drop(11)

        for (i in 1..subPacketsCount) {
          val (p, r) = readPacket(rest, level + 1)
          packet.packets.add(p)
          rest = r
        }
      }
    }


    println("Read $packet (level:$level)")
    return Pair(packet, rest)
  }

  fun part1() {
    var input = readInput("Day16").first()
    var binary = hexToBin(input)
    var packets = mutableListOf<Packet>()

    while (binary.isNotEmpty() && binary.contains("1")) {
      var (packet, rest) = readPacket(binary)
      packets.add(packet)
      binary = rest
    }

    println("Sum of versions: ${packets.sumOf { it.totalVersions() }}")
  }

  fun part2() {
  }

  part1()
  part2()
}
