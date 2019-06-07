import models.basic.Location
import models.basic.Point
import models.basic.Rotation
import models.basic.Transform

fun String.toPoint(): Point {
    val split = this.split(" ")
    return Point(
        split[0].toFloat(),
        split[1].toFloat(),
        split[2].toFloat()
    )
}

fun String.toTransform(): Transform {
    val split = this.split(Regex(" +"))
    return Transform(
        location = Location(
            x = split[0].toFloat(),
            y = split[1].toFloat(),
            z = split[2].toFloat()
        ),
        rotation = Rotation(
            roll = split[3].toFloat(),
            pitch = split[4].toFloat(),
            yaw = split[5].toFloat()
        )
    )
}
