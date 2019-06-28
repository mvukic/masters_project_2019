package models.icp

import models.shared.Euler
import models.shared.Point
import kotlin.math.*

data class RotationMatrix(
    val r11: Double, val r12: Double, val r13: Double,
    val r21: Double, val r22: Double, val r23: Double,
    val r31: Double, val r32: Double, val r33: Double
) {

    operator fun times(point: Point): Point {
        return Point(
            x = this.r11 * point.x + this.r12 * point.y + this.r13 * point.z,
            y = this.r21 * point.x + this.r22 * point.y + this.r23 * point.z,
            z = this.r31 * point.x + this.r32 * point.y + this.r33 * point.z
        )
    }

    fun toEuler(): Euler {
        val roll = atan2(r32, r33)
        val yaw = atan2(r21, r11)
        val pitch = atan2(-r31, r21 / sin(yaw))
        return Euler(
            roll = roll,
            pitch = pitch,
            yaw = yaw
        )
    }

    companion object {
        fun fromString(list: List<List<String>>): RotationMatrix {
            val (a, b, c) = list[0].map { it.toDouble() }
            val (d, e, f) = list[1].map { it.toDouble() }
            val (g, h, i) = list[2].map { it.toDouble() }
            return RotationMatrix(a, b, c, d, e, f, g, h, i)
        }
    }

}