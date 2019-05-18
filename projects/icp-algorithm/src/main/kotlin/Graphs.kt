import models.Data
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.absoluteValue

fun showGraphs(data: Data) {
    val height = 600
    val width = 1000
    val style = Styler.ChartTheme.Matlab

    val locations = data.frames.map { it.transform.location }
    val coordX = locations.map { it.x }
    val coordY = locations.map { it.y }
    val timestamps = data.frames.map { it.timestamp }

    val chartLocation = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Vehicle Location"
        xAxisTitle("X coordinate")
        yAxisTitle("Y coordinate")
    }.build()
    chartLocation.addSeries("Vehicle location", coordX, coordY)
    SwingWrapper(chartLocation).displayChart()

    val chartLocationX = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Vehicle Coordinate"
        xAxisTitle("Timestamp")
        yAxisTitle("Y coordinate")
    }.build()
    chartLocationX.addSeries("Vehicle y location", timestamps, coordY)
    SwingWrapper(chartLocationX).displayChart()

    val chartLocationY = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Vehicle Coordinate"
        xAxisTitle("Timestamp")
        yAxisTitle("X coordinate")
    }.build()
    chartLocationY.addSeries("Vehicle x location", timestamps, coordX)
    SwingWrapper(chartLocationY).displayChart()

    val yawY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.yaw.absoluteValue }
    val rollY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.roll.absoluteValue }
    val pitchY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.pitch.absoluteValue }
    val frameX = data.frames.sortedBy { it.frameId }.map { it.timestamp }
    val chartRotation = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Vehicle rotation"
        xAxisTitle("Frame")
        yAxisTitle("Rotation")
    }.build()
    chartRotation.addSeries("Roll", frameX, rollY)
    chartRotation.addSeries("Pitch", frameX, pitchY)
    chartRotation.addSeries("Yaw", frameX, yawY)
    SwingWrapper(chartRotation).displayChart()
}