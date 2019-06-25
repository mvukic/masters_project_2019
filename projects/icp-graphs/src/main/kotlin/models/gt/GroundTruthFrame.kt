package models.gt

data class GroundTruthFrame(
    // Scan frame id
    val frameId: String,
    // Frame timestamp
    val timestamp: Double,
    // Vehicle transform at this scan
    val transform: Transform
)