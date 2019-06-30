package charts

import models.shared.Euler
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import stats.calculateEulerDifference
import stats.wrapRadians
import kotlin.math.absoluteValue

fun anglesDifferencesCharts(timestamps: List<Double>, angles: List<Euler>, calculated: List<Euler>) {
    val diffs = calculateEulerDifference(angles, calculated)
    diffBetweenRealAndCalc(timestamps, diffs)
    overlayRotations(timestamps, angles, calculated)

}

fun overlayRotations(timestamps: List<Double>, angles: List<Euler>, calculated: List<Euler>) {
    val valjanje = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Referentno i estimirano valjanje"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("valjanje [rad]")
    }.build()
    valjanje.styler.legendPosition = Styler.LegendPosition.OutsideS
    valjanje.addSeries("Estimirano valjanje", timestamps, calculated.map { it.roll })
    valjanje.addSeries("Referentno valjanje", timestamps, angles.map { it.roll })
    SwingWrapper(valjanje).displayChart()

    val poniranje = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Referentno i estimirano poniranje"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("poniranje [rad]")
    }.build()
    poniranje.styler.legendPosition = Styler.LegendPosition.OutsideS
    poniranje.addSeries("Estimirano poniranje", timestamps, calculated.map { it.pitch })
    poniranje.addSeries("Referentno poniranje", timestamps, angles.map { it.pitch })
    SwingWrapper(poniranje).displayChart()
    val skretanje = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Referentno i estimirano skretanje"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("skretanje [rad]")
    }.build()
    skretanje.styler.legendPosition = Styler.LegendPosition.OutsideS
    skretanje.addSeries("Estimirano skretanje", timestamps, calculated.map { it.yaw })
    skretanje.addSeries("Referentno skretanje", timestamps, angles.map { it.yaw })
    SwingWrapper(skretanje).displayChart()
}

fun diffBetweenRealAndCalc(timestamps: List<Double>, diffs: List<Euler>) {
    val roll = diffs.map { it.roll }
    val pitch = diffs.map { it.pitch }
    val yaw = diffs.map { it.yaw }
    val rotationsEulerChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Razlike referentnih i estimiranih rotacija"
        xAxisTitle("vrijeme [s]")
        yAxisTitle("Δ rotacije [rad]")
    }.build()
    rotationsEulerChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    rotationsEulerChart.addSeries("ΔValjanje", timestamps, roll)
    rotationsEulerChart.addSeries("ΔPoniranje", timestamps, pitch)
    rotationsEulerChart.addSeries("ΔSkretanje", timestamps, yaw)
    SwingWrapper(rotationsEulerChart).displayChart()
}