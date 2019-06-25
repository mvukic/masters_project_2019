package stats

import kotlin.math.absoluteValue

/**
 * Calculates mean error between real coordinates and calculated coordinates
 */
fun meaLocation(realx: List<Double>, realy: List<Double>, realz: List<Double>, icpx: List<Double>, icpy: List<Double>, icpz: List<Double>): Triple<Double, Double, Double> {
    val meaz = realz.zip(icpz).map { (it.first - it.second).absoluteValue }.average()
    val meax = realx.zip(icpx).map { (it.first - it.second).absoluteValue }.average()
    val meay = realy.zip(icpy).map { (it.first - it.second).absoluteValue }.average()
    val result = Triple( meax, meay, meaz)
    println("MEA for location deltas: $result")
    return result
}

/**
 * Calculates mean error between real angles and calculated angles
 */
fun meaAngles(realRoll: List<Double>, realPitch: List<Double>, realYaw: List<Double>, icpRoll: List<Double>, icpPitch: List<Double>, icpYaw: List<Double>): Triple<Double, Double, Double> {
    val meaRoll = realRoll.zip(icpRoll).map { (it.first - it.second).absoluteValue }.average()
    val meaPitch = realPitch.zip(icpPitch).map { (it.first - it.second).absoluteValue }.average()
    val meaYaw = realYaw.zip(icpYaw).map { (it.first - it.second).absoluteValue }.average()
    val result = Triple( meaRoll, meaPitch, meaYaw)
    println("MEA for angle deltas: $result")
    return result
}