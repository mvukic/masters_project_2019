package models.shared

fun List<Point>.toCoordinates(): Triple<List<Double>, List<Double>, List<Double>> {
    return Triple(
        this.map { it.x },
        this.map { it.y },
        this.map { it.z }
    )
}

fun Array<DoubleArray>.timesPoint(point: Point): Point {
    return Point(
        x = this[0][0] * point.x + this[0][1] * point.y + this[0][2] * point.z + this[0][3],
        y = this[1][0] * point.x + this[1][1] * point.y + this[1][2] * point.z + this[1][3],
        z = this[2][0] * point.x + this[2][1] * point.y + this[2][2] * point.z + this[2][3]
    )
}