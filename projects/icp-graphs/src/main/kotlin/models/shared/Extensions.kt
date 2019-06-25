package models.shared

fun List<Point>.toCoordinates(): Triple<List<Double>, List<Double>, List<Double>> {
    return Triple(
        this.map { it.x },
        this.map { it.y },
        this.map { it.z }
    )
}