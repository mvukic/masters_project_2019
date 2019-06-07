package models.basic

class RotationMatrix private constructor(private val row0: List<Double>, private val row1: List<Double>, private val row3: List<Double>) {

    val pitch: Double by lazy {
        0.toDouble()
    }

    val roll: Double by lazy {
        0.toDouble()
    }

    val yaw: Double by lazy {
        0.toDouble()
    }


    companion object {
        fun fromStringList(list: List<List<String>>): RotationMatrix {
            return RotationMatrix(
                list[0].map { it.toDouble() }.take(3),
                list[1].map { it.toDouble() }.take(3),
                list[2].map { it.toDouble() }.take(3)
            )
        }
    }

}