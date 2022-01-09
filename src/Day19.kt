import kotlin.math.abs
import kotlin.math.max

// https://adventofcode.com/2021/day/19

fun main() {
  class Position(x: Int, y: Int, z: Int) {
    val x = x
    val y = y
    val z = z

    override fun toString(): String {
      return "($x,$y,$z)"
    }

    override fun equals(other: Any?): Boolean {
      return (other is Position)
          && x == other.x
          && y == other.y
          && z == other.z
    }

    fun rotateX() : Position {
      return Position(x, -z, y)
    }

    fun rotateY() : Position {
      return Position(-z, y, x)
    }

    fun rotateZ() : Position {
      return Position(y, -x, z)
    }

    fun vectorTo(other: Position) : Position {
      return Position(other.x - x, other.y - y, other.z - z)
    }

    fun addVector(vector: Position) : Position {
      return Position(vector.x + x, vector.y + y, vector.z + z)
    }

    fun subtractVector(vector: Position) : Position {
      return Position(x - vector.x, y - vector.y, z - vector.z)
    }

    fun manhattanDistanceTo(other: Position) : Int {
      return abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
    }

    fun allRotations(): Array<Position> {
      return arrayOf(
        Position(x,y,z),
        Position(-z,y,x),
        Position(y,z,x),
        Position(z,-y,x),
        Position(-y,-z,x),
        Position(-z,x,-y),
        Position(x,z,-y),
        Position(z,-x,-y),
        Position(-x,-z,-y),
        Position(-z,-y,-x),
        Position(-y,z,-x),
        Position(z,y,-x),
        Position(y,-z,-x),
        Position(-z,-x,y),
        Position(-x,z,y),
        Position(z,x,y),
        Position(x,-z,y),
        Position(-y,x,z),
        Position(y,-x,z),
        Position(-x,-y,z),
        Position(-y,-x,-z),
        Position(-x,y,-z),
        Position(y,x,-z),
        Position(x,-y,-z)
      )
//      val result = mutableListOf<Position>()
//      var current = this
//
//      for (x in 0..3) {
//        current = current.rotateX()
//        for (y in 0..3) {
//          current = current.rotateY()
//          for (z in 0..3) {
//            current = current.rotateZ()
//            result.add(current)
//          }
//        }
//      }
//
//      return result.distinctBy { Triple(it.x, it.y, it.z) }
    }
  }

  class Scanner(id: String) {
    val id = id
    var beacons = mutableListOf<Position>()
    val alreadyComparedWith = mutableListOf<Scanner>()
    var rotationIndex : Int? = null
    var position: Position? = null

    override fun toString(): String {
      return id
    }
  }

  fun readScanners() : List<Scanner> {
    val scanners = mutableListOf<Scanner>()
    var currentScanner : Scanner? = null

    for (line in readInput("Day19")) {
      if (line.startsWith("---")) {
        currentScanner = Scanner(line)
        scanners.add(currentScanner)
        continue
      }

      if (line.isEmpty()) {
        continue
      }

      var numbers = line.split(",").map { it.toInt() }
      currentScanner!!.beacons.add(Position(numbers[0], numbers[1], numbers[2]))
    }

    return scanners
  }

  fun compare(scannerA: Scanner, scannerB: Scanner) : Boolean {
    scannerB.alreadyComparedWith.add(scannerA)
    for (rotationIndex in 0..23) {

//      println("Checking for rotation $rotationIndex")
      for (ba in scannerA.beacons) {
        for (bb in scannerB.beacons) {
          val rotatedBeaconB = bb.allRotations()[rotationIndex]
          val translationBtoA = rotatedBeaconB.vectorTo(ba)
          val translatedBeaconsB = scannerB.beacons.map { it.allRotations()[rotationIndex].addVector(translationBtoA) }
          val matches = translatedBeaconsB.filter { scannerA.beacons.contains(it) }
          if (matches.count() >= 12) {
            scannerB.beacons = translatedBeaconsB.toMutableList()
            scannerB.rotationIndex = rotationIndex
            scannerB.position = translationBtoA
            println("Match found! Assuming $ba == $bb, rotation: $rotationIndex, translation: $translationBtoA, matches: $matches. Scanner $scannerB position is ${scannerB.position}")
            println("Matching beacons: $matches")
            return true
          }
        }
      }
    }

    return false
  }

  fun part1() {
    val scanners = readScanners()

    // 1. Assume that first scanner's coords are (0,0) and rotation is 0
    // 2. For rotation 0 assume that s1 b0 == s2 b0 and compute translation between them
    // 3. Apply the transformation to other beacons of s2 and check how many beacons are matching
    // 4. If there are no matches assume that s1 b0 == s2 b1 and compute translation between them
    // 5. Apply the transformation to other beacons of s2 and check how many beacons are matching (if there's e.g. more than 3 store that transformation assuming that it might be the correct rotation)
    // 6. ... repeat for each beacon of s2
    // 7. If there's >= 12 matching beacons mark s2 as matched and save its transformation (or store beacons transformed in relation to s0). Also compute its position.
    // 8. ... repeat for all scanners
    // 9. for all non-matched scanners try matching with newly discovered scanners

    val firstScanner = scanners.first()
    firstScanner.position = Position(0, 0, 0)
    firstScanner.rotationIndex = 0

    val discoveredScanners = mutableListOf(firstScanner)
    var scannersToCheck = listOf(firstScanner)
    while (discoveredScanners.count() < scanners.count()) {
      val newScannersToCheck = mutableListOf<Scanner>()
      for (scannerA in scannersToCheck) {
        for (scannerB in (scanners.minus(discoveredScanners.toSet()).filter { !it.alreadyComparedWith.contains(scannerA) })) {
          println("Comparing $scannerA with $scannerB")
          if (compare(scannerA, scannerB)) {
            discoveredScanners.add(scannerB)
            newScannersToCheck.add(scannerB)
          }
        }
      }

      scannersToCheck = newScannersToCheck
    }

    val uniqueBeaconsCount = scanners.flatMap { it.beacons }.distinctBy { it.toString() }.count()
    println(uniqueBeaconsCount)

    var maxManhattanDistance = 0
    for (scannerA in scanners) {
      for (scannerB in scanners) {
        val distance = scannerA.position!!.manhattanDistanceTo(scannerB.position!!)
        if (distance > maxManhattanDistance) {
          maxManhattanDistance = distance
        }
      }
    }

    println("Max manhattan distance is: $maxManhattanDistance")
  }

  fun part2() {
  }

  part1()
  part2()
}
