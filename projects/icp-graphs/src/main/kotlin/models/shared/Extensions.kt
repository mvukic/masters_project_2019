package models.shared

fun List<Point>.toCoordinates(): Triple<List<Double>, List<Double>, List<Double>> {
    return Triple(
        this.map { it.x },
        this.map { it.y },
        this.map { it.z }
    )
}

fun Array<DoubleArray>.timesPoint(point: Point): Point {
    val transposed = arrayOf(
        doubleArrayOf(this[0][0], this[1][0], this[2][0]),
        doubleArrayOf(this[0][1], this[1][1], this[2][1]),
        doubleArrayOf(this[0][2], this[1][2], this[2][2])
    )
    return Point(
        x = transposed[0][0] * point.x + transposed[0][1] * point.y + transposed[0][2] * point.z + this[0][3],
        y = transposed[1][0] * point.x + transposed[1][1] * point.y + transposed[1][2] * point.z + this[1][3],
        z = transposed[2][0] * point.x + transposed[2][1] * point.y + transposed[2][2] * point.z + this[2][3]
    )
}

fun Array<DoubleArray>.x3TimesPoint(point: Point): Point {
    return Point(
        x = this[0][0] * point.x + this[0][1] * point.y + this[0][2] * point.z,
        y = this[1][0] * point.x + this[1][1] * point.y + this[1][2] * point.z,
        z = this[2][0] * point.x + this[2][1] * point.y + this[2][2] * point.z
    )
}

fun multiplySameSizeMat(left: Array<DoubleArray>, right: Array<DoubleArray>): Array<DoubleArray>{
    val list = (0 until left.size).map { 0.0 }
    val result = Array(left.size) { list.toDoubleArray() }
    for (i in 0 until left.size) {
        for (j in 0 until left.size) {
            for (k in 0 until left.size) {
                result[i][j] += left[i][k] * right[k][j]
            }
        }
    }
    return result
}

fun Array<DoubleArray>.muliplyLocations(value: Point) {
    this[0][3] = this[0][3] * value.x
    this[1][3] = this[1][3] * value.y
    this[2][3] = this[2][3] * value.z
}

fun Array<DoubleArray>.addLocations(value: Point) {
    this[0][3] = this[0][3] + value.x
    this[1][3] = this[1][3] + value.y
    this[2][3] = this[2][3] + value.z
}
