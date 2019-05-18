package samples

import models.Data
import models.Frame
import models.basic.Point
import models.basic.Transform

fun SampleData(): Data {
    return Data(
        frames = listOf(
            Frame(
                frameId = "0",
                transform = Transform(),
                timestamp = 0.0,
                points = listOf(
                    Point(10f, 10f, 30f)
                )
            ),
            Frame(
                frameId = "1",
                transform = Transform(),
                timestamp = 1.0,
                points = listOf(
                    Point(2.5f, 3f, 30f)
                )
            )
        ),
        lidarToVehicleTransform = Transform()
    )
}