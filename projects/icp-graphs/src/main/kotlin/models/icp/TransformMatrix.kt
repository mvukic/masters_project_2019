package models.icp

import models.shared.Euler
import models.shared.Point

data class TransformMatrix(
    val between: Pair<String, String>,
    val translation: TranslationMatrix,
    val fitness: Double,
    val rotation: RotationMatrix,
    val angles: Euler
){
    fun toEuler() = this.rotation.toEuler()

    operator fun times(point: Point): Point {
        return rotation * point + Point(translation.x, translation.y, translation.z)
    }
}
