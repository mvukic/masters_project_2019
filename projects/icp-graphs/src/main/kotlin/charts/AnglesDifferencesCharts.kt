package charts

import models.shared.Euler
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler

fun anglesDifferencesCharts(timestampsFull: List<Double>, angles: List<Euler>, calculated: List<Euler>) {
    val timestamps = timestampsFull.drop(1)

    // Calculate points and angles using real data and icp rotation matrix
    val realAnglesDiff = stats.calculateAnglesDifferences(angles)
    val icpAnglesDiff = stats.calculateAnglesDifferences(calculated)

    // Real euler angles differences
    val realRollDelta = realAnglesDiff.map { it.roll }
    val realPitchDelta = realAnglesDiff.map { it.pitch }
    val realYawDelta = realAnglesDiff.map { it.yaw }

    realAngleDeltasChart(timestamps, realRollDelta, realPitchDelta, realYawDelta)

    // Calculated euler angles differences
    val icpRollDelta = icpAnglesDiff.map { it.roll }
    val icpPitchDelta = icpAnglesDiff.map { it.pitch }
    val icpYawDelta = icpAnglesDiff.map { it.yaw }

    calculatedAngleDeltasChart(timestamps, icpRollDelta, icpPitchDelta, icpYawDelta)

}

fun calculatedAngleDeltasChart(timestamps: List<Double>, roll: List<Double>, pitch: List<Double>, yaw: List<Double>) {
    val realAngleDeltasChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Differences between two consecutive points in angles (calculated)"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Euler angles")
    }.build()
    realAngleDeltasChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    realAngleDeltasChart.addSeries("Delta roll", timestamps, roll)
    realAngleDeltasChart.addSeries("Delta pitch", timestamps, pitch)
    realAngleDeltasChart.addSeries("Delta yaw", timestamps, yaw)
    SwingWrapper(realAngleDeltasChart).displayChart()
}

fun realAngleDeltasChart(timestamps: List<Double>, roll: List<Double>, pitch: List<Double>, yaw: List<Double>) {
    val realAngleDeltasChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Differences between two consecutive points in angles"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Euler angles")
    }.build()
    realAngleDeltasChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    realAngleDeltasChart.addSeries("Delta roll", timestamps, roll)
    realAngleDeltasChart.addSeries("Delta pitch", timestamps, pitch)
    realAngleDeltasChart.addSeries("Delta yaw", timestamps, yaw)
    SwingWrapper(realAngleDeltasChart).displayChart()
}