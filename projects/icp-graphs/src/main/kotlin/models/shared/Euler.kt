package models.shared

import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

data class Euler(val roll: Double, val pitch: Double, val yaw: Double) {

    fun toQuaternion(): Quaternion {
        val cy = cos(yaw * 0.5)
        val sy = sin(yaw * 0.5)
        val cp = cos(pitch * 0.5)
        val sp = sin(pitch * 0.5)
        val cr = cos(roll * 0.5)
        val sr = sin(roll * 0.5)
        return Quaternion(
            w = cy * cp * cr + sy * sp * sr,
            x = cy * cp * sr - sy * sp * cr,
            y = sy * cp * sr + cy * sp * cr,
            z = sy * cp * cr - cy * sp * sr
        )
    }

    companion object {
        fun fromString(list: List<String>): Euler {
            val roll = list[0].toDouble()
            val pitch = list[1].toDouble()
            val yaw = list[2].toDouble()
            return Euler(
                roll = roll,
                pitch = pitch,
                yaw = yaw
            )
        }

    }
}