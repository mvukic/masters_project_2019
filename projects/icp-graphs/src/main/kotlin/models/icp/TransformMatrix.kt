package models.icp

import models.shared.Euler
import models.shared.Point

class TransformMatrix(
    val between: Pair<String, String>,
    val translation: TranslationMatrix,
    val fitness: Double,
    val rotation: RotationMatrix,
    val angles: Euler,
    val matrix: Array<DoubleArray>
){
    fun toEuler() = this.rotation.toEuler()

    /**
     * Multiply 4x4 matrix and point
     */
    operator fun times(point: Point): Point {
        return rotation * point + Point(translation.x, translation.y, translation.z)
    }

    /** multiply two 4x4 matrix and 4x4 matrix */
    operator fun times(matrix: Array<DoubleArray>): Array<DoubleArray> {
        val result = arrayOf(
            doubleArrayOf(0.0, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 0.0)
        )
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                for (k in 0 until 4) {
                    result[i][j] += this.matrix[i][k] * matrix[k][j]
                }
            }
        }
        return result
    }
}
