package models.gt

data class GroundTruth(
    // List of frames
    val frames: List<GroundTruthFrame>,
    // Relative transform between vehicle and lidar sensor
    val lidarToVehicleTransform: Transform
) {

    fun timestamps() = this.frames.map { it.timestamp }
    fun locations() = this.frames.map { it.transform.location }
    fun rotations() = this.frames.map { it.transform.rotation }
}
