import models.Data
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.absoluteValue

fun showGraphs(data: Data) {
    val height = 600
    val width = 1000
    val style = Styler.ChartTheme.Matlab

    val locations = data.groundTruth.map { it.transform.location }
    val coordX = locations.map { it.x }
    val coordY = locations.map { it.y }
    val coordZ = locations.map { it.z }
    val timestamps = data.groundTruth.map { it.timestamp }

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
        title = "X koordinate vozila u vremenu"
        xAxisTitle("Vrijeme [s]")
        yAxisTitle("X koordinata [m]")
    }.build()
    chartLocationX.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartLocationX.addSeries("X koordinate", timestamps, coordX)
    SwingWrapper(chartLocationX).displayChart()

    val chartLocationZ = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Z koordinate vozila u vremenu"
        xAxisTitle("Vrijeme [s]")
        yAxisTitle("Z koordinata [m]")
    }.build()
    chartLocationZ.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartLocationZ.addSeries("Z koordinate", timestamps, coordZ)
    SwingWrapper(chartLocationZ).displayChart()

    val chartLocationY = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = style
        title = "Y koordinate vozila u vremenu"
        xAxisTitle("Vrijeme [s]")
        yAxisTitle("Y koordinata [m]")
    }.build()
    chartLocationY.styler.legendPosition = Styler.LegendPosition.OutsideS
    chartLocationY.addSeries("Y koordinate", timestamps, coordY)
    SwingWrapper(chartLocationY).displayChart()

    val yawY = data.groundTruth.sortedBy { it.frameId }.map { it.transform.rotation.yaw.absoluteValue }
    val rollY = data.groundTruth.sortedBy { it.frameId }.map { it.transform.rotation.roll }
    val pitchY = data.groundTruth.sortedBy { it.frameId }.map { it.transform.rotation.pitch }
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