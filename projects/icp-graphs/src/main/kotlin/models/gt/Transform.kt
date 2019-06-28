package models.gt

import models.shared.Euler
import models.shared.Point
import toRadians

data class Transform(val location: Point, val rotation: Euler) {

    companion object {
        fun fromString(line: String): Transform {
            val split = line.split(Regex(" +"))
            return Transform(
                location = Point(
                    x = split[0].toDouble(),
                    y = split[1].toDouble(),
                    z = split[2].toDouble()
                ),
                rotation = Euler(
                    roll = split[3].toDouble().toRadians(),
                    pitch = split[4].toDouble().toRadians(),
                    yaw = split[5].toDouble().toRadians()
                )
            )
        }
    }
}