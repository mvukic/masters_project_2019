package models.icp

class TranslationMatrix private constructor(val x: Double, val y: Double, val z: Double) {

    companion object {
        fun fromStringList(list: List<List<String>>): TranslationMatrix {
            return TranslationMatrix(
                x = list[0].last().toDouble(),
                y = list[1].last().toDouble(),
                z = list[2].last().toDouble()
            )
        }
    }

}