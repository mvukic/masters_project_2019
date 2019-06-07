package models.basic

import kotlin.math.absoluteValue

data class TransformMatrix(
    val between: Pair<String, String>,
    val rotation: RotationMatrix,
    val translation: TranslationMatrix,
    val fitness: Double,
    val angles: EulerAngles
)

class EulerAngles private constructor(val roll: Double, val pitch: Double, val yaw: Double) {

    companion object {
        fun fromString(list: List<String>): EulerAngles {
            val rollDeg = list[0].toDouble() * 180 / Math.PI
            val pitchDeg = list[1].toDouble() * 180 / Math.PI
            val yawDeg = list[2].toDouble() * 180 / Math.PI
            return EulerAngles(
                roll = if(rollDeg.absoluteValue > 90) 180 - rollDeg.absoluteValue else 0.toDouble(),
                pitch = if(pitchDeg.absoluteValue > 90) 180 - pitchDeg.absoluteValue else 0.toDouble(),
                yaw = if(yawDeg.absoluteValue > 90) 180 - yawDeg.absoluteValue else 0.toDouble()
            )
        }

    }
}
