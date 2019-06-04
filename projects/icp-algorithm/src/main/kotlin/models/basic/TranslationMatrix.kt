package models.basic

class TranslationMatrix private constructor(private val x: Double, private val y: Double, private val z: Double) {

    fun x(): Double {
        return x
    }

    fun y(): Double {
        return y
    }

    fun z(): Double {
        return z
    }

    companion object {
        fun fromStringList(list: List<List<String>>): TranslationMatrix {
            return TranslationMatrix(
                list[0].last().toDouble(),
                list[1].last().toDouble(),
                list[2].last().toDouble()
            )
        }
    }

}