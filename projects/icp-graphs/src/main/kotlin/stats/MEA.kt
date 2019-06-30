package stats

import models.shared.Euler
import models.shared.Point
import kotlin.math.absoluteValue
import kotlin.math.pow

fun MEACoordinates(locations: List<Point>, calculated: List<Point>) {
    val meax = locations.zip(calculated).map { (it.first.x - it.second.x).absoluteValue }.average()
    val meay = locations.zip(calculated).map { (it.first.y - it.second.y).absoluteValue }.average()
    val meaz = locations.zip(calculated).map { (it.first.z - it.second.z).absoluteValue }.average()
    val result = Triple( meax, meay, meaz)
    println("MEA for coordinates deltas: $result")
}

fun MEAAngles(angles: List<Euler>, calculated: List<Euler>) {
    val meaRoll = angles.zip(calculated).map { (it.first.roll - it.second.roll).absoluteValue }.average()
    val meaPitch = angles.zip(calculated).map { (it.first.pitch - it.second.pitch).absoluteValue }.average()
    val meaYaw = angles.zip(calculated).map { (it.first.yaw - it.second.yaw).absoluteValue }.average()
    val result = Triple( meaRoll, meaPitch, meaYaw)
    println("MEA for angle deltas: $result")
}

fun MSECoordinates(locations: List<Point>, calculated: List<Point>) {
    val msex = locations.zip(calculated).map { (it.first.x - it.second.x).pow(2) }.average()
    val msey = locations.zip(calculated).map { (it.first.y - it.second.y).pow(2) }.average()
    val msez = locations.zip(calculated).map { (it.first.z - it.second.z).pow(2) }.average()
    val result = Triple( msex, msey, msez)
    println("MSE for coordinates is $result")
}

fun MSEAngles(angles: List<Euler>, calculated: List<Euler>) {
    val mseRoll = angles.zip(calculated).map { (it.first.roll - it.second.roll).pow(2) }.average()
    val msePitch = angles.zip(calculated).map { (it.first.pitch - it.second.pitch).pow(2) }.average()
    val mseYaw = angles.zip(calculated).map { (it.first.yaw - it.second.yaw).pow(2) }.average()
    val result = Triple( mseRoll, msePitch, mseYaw)
    println("MSE for angle deltas: $result")
}