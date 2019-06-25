import models.basic.EulerAngles
import models.basic.TransformMatrix
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.absoluteValue

fun icpCharts(timestamps: List<Double>, icpTransformations: List<TransformMatrix>) {
    val icpEulerQuat = icpTransformations.map { it.angles.toQuaternion(radians = false) }
    val icpXQuat = icpEulerQuat.map { it.x }
    val icpYQuat = icpEulerQuat.map { it.y }
    val icpZQuat = icpEulerQuat.map { it.z }
    val icpWQuat = icpEulerQuat.map { it.w }

    val icpCoordsQuatChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotacija icp Quaternion"
        xAxisTitle("timestamp [s]")
        yAxisTitle("rotacije")
    }.build()
    icpCoordsQuatChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpCoordsQuatChart.addSeries("X", timestamps.drop(1), icpXQuat)
    icpCoordsQuatChart.addSeries("Y", timestamps.drop(1), icpYQuat)
    icpCoordsQuatChart.addSeries("Z", timestamps.drop(1), icpZQuat)
    icpCoordsQuatChart.addSeries("W", timestamps.drop(1), icpWQuat)
    SwingWrapper(icpCoordsQuatChart).displayChart()

    val icpRollConversionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotacija roll (conversion)"
        xAxisTitle("timestamp [s]")
        yAxisTitle("Roll")
    }.build()
    icpRollConversionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpRollConversionChart.addSeries("Roll icp real", timestamps.drop(1), icpTransformations.map { it.angles.roll.absoluteValue })
    icpRollConversionChart.addSeries("Roll icp calc", timestamps.drop(1), icpTransformations.map { it.rotation.toEulerAngles().roll.absoluteValue })
    SwingWrapper(icpRollConversionChart).displayChart()

    val icpPitchConversionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotacija pitch (conversion)"
        xAxisTitle("timestamp [s]")
        yAxisTitle("Pitch")
    }.build()
    icpPitchConversionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpPitchConversionChart.addSeries("Pitch real", timestamps.drop(1), icpTransformations.map { it.angles.pitch.absoluteValue })
    icpPitchConversionChart.addSeries("Pitch calc", timestamps.drop(1), icpTransformations.map { it.rotation.toEulerAngles().pitch.absoluteValue })
    SwingWrapper(icpPitchConversionChart).displayChart()

    val icpYawConversionChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Rotacija yaw (conversion)"
        xAxisTitle("timestamp [s]")
        yAxisTitle("Yaw")
    }.build()
    icpYawConversionChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    icpYawConversionChart.addSeries("Yaw real", timestamps.drop(1), icpTransformations.map { it.angles.yaw.absoluteValue })
    icpYawConversionChart.addSeries("Yaw calc", timestamps.drop(1), icpTransformations.map { it.rotation.toEulerAngles().yaw.absoluteValue })
    SwingWrapper(icpYawConversionChart).displayChart()
}