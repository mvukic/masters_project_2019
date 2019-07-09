package models.shared

data class Point(val x: Double, val y: Double, val z: Double) {

    operator fun plus(point: Point): Point {
        return Point(
            x = this.x + point.x,
            y = this.y + point.y,
            z = this.z + point.z
        )
    }

    operator fun minus(point: Point): Point {
        return Point(
            x = this.x - point.x,
            y = this.y - point.y,
            z = this.z - point.z
        )
    }

}
