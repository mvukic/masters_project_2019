package charts

import models.shared.Euler

fun anglesDifferencesCharts(angles: List<Euler>, calculated: List<Euler>) {

    // Calculate points and angles using real data and icp rotation matrix
    val realAnglesDiff = stats.calculateAnglesDifferences(angles)
    val icpAnglesDiff = stats.calculateAnglesDifferences(calculated)
}