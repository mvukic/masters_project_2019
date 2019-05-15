
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.absoluteValue


fun main(args: Array<String>)  {
    val data = setupData(args[0], onlyTransforms = true)

    val locations = data.frames.map { it.transform.location }
    val coordX = locations.map { it.x }
    val coordY = locations.map { it.y }
    val vehicleX = data.frames.map { it.timestamp }
    val chartLocation = XYChartBuilder().apply {
        height = 800
        width = 1000
        chartTheme = Styler.ChartTheme.Matlab
        title = "Vehicle movement"
        xAxisTitle("X coordinate")
        yAxisTitle("Y coordinate")
    }.build()
//    chartLocation.addSeries("Vehicle x location", coordX, vehicleX)
//    chartLocation.addSeries("Vehicle y location", coordY, vehicleX)
    chartLocation.addSeries("Vehicle location", coordX, coordY)
    SwingWrapper(chartLocation).displayChart()

    val yawY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.yaw.absoluteValue }
    val rollY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.roll.absoluteValue }
    val pitchY = data.frames.sortedBy { it.frameId }.map { it.transform.rotation.pitch.absoluteValue }
    val frameX = data.frames.sortedBy { it.frameId }.map { it.timestamp }
    val chartRotation = XYChartBuilder().apply {
        height = 800
        width = 1000
        chartTheme = Styler.ChartTheme.Matlab
        title = "Vehicle rotation"
        xAxisTitle("Frame")
        yAxisTitle("Yaw")
    }.build()
    chartRotation.addSeries("Yaw", frameX, yawY)
    chartRotation.addSeries("Roll", frameX, rollY)
    chartRotation.addSeries("Pitch", frameX, pitchY)
    SwingWrapper(chartRotation).displayChart()
}



