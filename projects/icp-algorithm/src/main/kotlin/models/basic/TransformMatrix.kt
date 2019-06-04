package models.basic

data class TransformMatrix(
    val between: Pair<String, String>,
    val rotation: RotationMatrix,
    val translation: TranslationMatrix,
    val fitness: Double
)