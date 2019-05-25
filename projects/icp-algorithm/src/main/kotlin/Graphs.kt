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
        title = "Lokacija vozila"
        xAxisTitle("X koordinata [m]")
        yAxisTitle("Y koordinata [m]")
    }.build()
    chartLocation.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartLocation.addSeries("Lokacija vozila", coordX, coordY)
    SwingWrapper(chartLocation).displayChart()

    val chartLocationX = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Y koordinate vozila u vremenu"
        xAxisTitle("Vrijeme [s]")
        yAxisTitle("Y koordinata [m]")
    }.build()
    chartLocationX.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartLocationX.addSeries("Y koordinate", timestamps, coordY)
    SwingWrapper(chartLocationX).displayChart()

    val chartLocationY = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "X koordinate vozila u vremenu"
        xAxisTitle("Vrijeme [s]")
        yAxisTitle("X koordinata [m]")
    }.build()
    chartLocationY.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartLocationY.addSeries("X koordinate", timestamps, coordX)
    SwingWrapper(chartLocationY).displayChart()

    val yawY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.yaw.absoluteValue }
    val rollY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.roll.absoluteValue }
    val pitchY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.pitch.absoluteValue }
    val chartRotation = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Rotacija vozila"
        xAxisTitle("Vrijeme [s]")
        yAxisTitle("Rotacija [°]")
    }.build()
    chartRotation.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartRotation.addSeries("Valjanje", timestamps, rollY)
    chartRotation.addSeries("Poniranje", timestamps, pitchY)
    chartRotation.addSeries("Skretanje", timestamps, yawY)
    SwingWrapper(chartRotation).displayChart()
}