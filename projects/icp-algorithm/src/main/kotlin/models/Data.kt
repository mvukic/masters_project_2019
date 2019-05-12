package models

import models.basic.Point
import models.basic.Transform

data class Data(
    // List of frames
    val frames: List<Frame>,
    // Relative transform between vehicle and lidar sensor
    val lidarToVehicleTransform: Transform
)

data class Frame(
    // Scan frame id
    val frameId: String,
    // Vehicle transform at this scan
    val transform: Transform,
    // Point cloud points
    val points: List<Point>
)