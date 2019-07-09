package models.shared

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

    fun toRz(): Array<DoubleArray> {
        return arrayOf(
            doubleArrayOf(cos(yaw), -sin(yaw), 0.0),
            doubleArrayOf(sin(yaw), cos(yaw), 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )
    }

    fun toRy(): Array<DoubleArray> {
        return arrayOf(
            doubleArrayOf(cos(pitch), 0.0, sin(pitch)),
            doubleArrayOf(0.0, 1.0, 0.0),
            doubleArrayOf(-sin(pitch), 0.0, cos(pitch))
        )
    }

    fun toRx(): Array<DoubleArray> {
        return arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0),
            doubleArrayOf(0.0, cos(roll), -sin(roll)),
            doubleArrayOf(0.0, sin(roll), cos(roll))
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

fun Double.toRz(): Array<DoubleArray> {
    return arrayOf(
        doubleArrayOf(cos(this), -sin(this), 0.0),
        doubleArrayOf(sin(this), cos(this), 0.0),
        doubleArrayOf(0.0, 0.0, 1.0)
    )
}

fun Double.toRy(): Array<DoubleArray> {
    return arrayOf(
        doubleArrayOf(cos(this), 0.0, sin(this)),
        doubleArrayOf(0.0, 1.0, 0.0),
        doubleArrayOf(-sin(this), 0.0, cos(this))
    )
}

fun Double.toRx(): Array<DoubleArray> {
    return arrayOf(
        doubleArrayOf(1.0, 0.0, 0.0),
        doubleArrayOf(0.0, cos(this), -sin(this)),
        doubleArrayOf(0.0, sin(this), cos(this))
    )
}