package charts

import models.Frame
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler

fun groundTruthCharts(frames: List<Frame>) {
    val height = 600
    val width = 1000

    val timestamps = frames.map { it.timestamp }

    // Get ground truth locations
    val locations = frames.map { it.transform.location }
    val x = locations.map { it.x }
    val y = locations.map { it.y }
    val z = locations.map { it.z }

    // Get ground truth rotations
    val angles = frames.map { it.transform.rotation }
    val quaternion = angles.map { it.toQuaternion() }
    val roll = angles.map { it.roll }
    val pitch = angles.map { it.pitch }
    val yaw = angles.map { it.yaw }
    val xQuat = quaternion.map { it.x }
    val yQuat = quaternion.map { it.y }
    val zQuat = quaternion.map { it.z }
    val wQuat = quaternion.map { it.w }

    val rotationsQuaternionChart = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = Styler.ChartTheme.Matlab
        title = "GT Quaternions"
        xAxisTitle("timestamp [s]")
        yAxisTitle("rotations")
    }.build()
    rotationsQuaternionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsQuaternionChart.addSeries("X", timestamps, xQuat)
    rotationsQuaternionChart.addSeries("Y", timestamps, yQuat)
    rotationsQuaternionChart.addSeries("Z", timestamps, zQuat)
    rotationsQuaternionChart.addSeries("W", timestamps, wQuat)
    SwingWrapper(rotationsQuaternionChart).displayChart()

    val rotationsEulerChart = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = Styler.ChartTheme.Matlab
        title = "GT rotations"
        xAxisTitle("timestamp [s]")
        yAxisTitle("rotations")
    }.build()
    rotationsEulerChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsEulerChart.addSeries("GT roll", timestamps, roll)
    rotationsEulerChart.addSeries("GT Pitch", timestamps, pitch)
    rotationsEulerChart.addSeries("GT Yaw", timestamps, yaw)
    SwingWrapper(rotationsEulerChart).displayChart()

    val locationsChart = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = Styler.ChartTheme.Matlab
        title = "GT locations"
        xAxisTitle("x [m]")
        yAxisTitle("y [m]")
    }.build()
    locationsChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    locationsChart.addSeries("GT location", x, y)
    SwingWrapper(locationsChart).displayChart()


}