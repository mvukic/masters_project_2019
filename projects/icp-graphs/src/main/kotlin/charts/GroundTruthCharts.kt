package charts

import models.gt.GroundTruth
import models.gt.GroundTruthFrame
import models.shared.Euler
import models.shared.Point
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.absoluteValue

fun groundTruthCharts(gt: GroundTruth) {

    val timestamps = gt.timestamps()
    val angles = gt.rotations()
    val locations = gt.locations()

    locationsChart(timestamps, locations)
    rotationsChart(timestamps, angles)
}

fun locationsChart(timestamps: List<Double>, locations: List<Point>) {
    val x = locations.map { it.x }
    val y = locations.map { it.y }
    val z = locations.map { it.z }

    val locationsChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Koordinate vozila"
        xAxisTitle("x [m]")
        yAxisTitle("y [m]")
    }.build()
    locationsChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    locationsChart.addSeries("Koordinate vozila", x, y)
    SwingWrapper(locationsChart).displayChart()

    val coordinatesChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Koordinate vozila"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("koordinate [m]")
    }.build()
    coordinatesChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    coordinatesChart.addSeries("X koordinate", timestamps, x)
    coordinatesChart.addSeries("Y koordinate", timestamps, y)
    coordinatesChart.addSeries("Z koordinate", timestamps, z)
    SwingWrapper(coordinatesChart).displayChart()
}

fun rotationsChart(timestamps: List<Double>, angles: List<Euler>) {
    val quaternion = angles.map { it.toQuaternion() }
    val roll = angles.map { it.roll }
    val pitch = angles.map { it.pitch }
    val yaw = angles.map { it.yaw.absoluteValue }
    val xQuat = quaternion.map { it.x }
    val yQuat = quaternion.map { it.y }
    val zQuat = quaternion.map { it.z.absoluteValue }
    val wQuat = quaternion.map { it.w }

    val rotationsQuaternionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotacija prikazana pomoću kvaterniona"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("rotacija [rad]")
    }.build()
    rotationsQuaternionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsQuaternionChart.addSeries("X", timestamps, xQuat)
    rotationsQuaternionChart.addSeries("Y", timestamps, yQuat)
    rotationsQuaternionChart.addSeries("Z", timestamps, zQuat)
    rotationsQuaternionChart.addSeries("W", timestamps, wQuat)
    SwingWrapper(rotationsQuaternionChart).displayChart()

    val rotationsEulerChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotacije"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("rotacija [rad]")
    }.build()
    rotationsEulerChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsEulerChart.addSeries("Valjanje", timestamps, roll)
    rotationsEulerChart.addSeries("Poniranje", timestamps, pitch)
    rotationsEulerChart.addSeries("Skretanje", timestamps, yaw)
    SwingWrapper(rotationsEulerChart).displayChart()
}