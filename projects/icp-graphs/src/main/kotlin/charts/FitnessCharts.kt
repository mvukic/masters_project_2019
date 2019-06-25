package charts

import models.basic.TransformMatrix
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler

fun fitnessCharts(icpTransformations: List<TransformMatrix>, timestamps : List<Double>) {
    val height = 600
    val width = 1000

    val fitness = icpTransformations.map { it.fitness }
    // Average fitness
    println("Average fitness: ${fitness.average()}")
    val fitnessChart = XYChartBuilder().apply {
        height(height)
        width(width)
        chartTheme = Styler.ChartTheme.Matlab
        title = "ICP fitness"
        xAxisTitle("timestamp [s]")
        yAxisTitle("fitness")
    }.build()
    fitnessChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    fitnessChart.addSeries("fitness", timestamps.drop(1), fitness)
    SwingWrapper(fitnessChart).displayChart()

}