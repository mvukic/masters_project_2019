package models.basic

import kotlin.math.*

data class Point(val x: Float, val y: Float, val z: Float) {

    fun distance(): Float {
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }
}