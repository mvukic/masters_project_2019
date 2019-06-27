package charts

import models.shared.Point
import models.shared.toCoordinates
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import stats.calculateCoordinateDifferences

fun locationDifferencesCharts(timestampsFull: List<Double>, locations: List<Point>, calculated: List<Point>) {
    val timestamps = timestampsFull.drop(1)
    val realPointsDiff = calculateCoordinateDifferences(locations)
    val icpPointsDiff = calculateCoordinateDifferences(calculated)

    // Differences between real points
    val realXDelta = realPointsDiff.map { it.x }
    val realYDelta = realPointsDiff.map { it.y }
    val realZDelta = realPointsDiff.map { it.z }

//    realLocationsDiffCharts(timestamps, realXDelta, realYDelta, realZDelta)

    // Differences between calculated points
    val icpXDelta = icpPointsDiff.map { it.x }
    val icpYDelta = icpPointsDiff.map { it.y }
    val icpZDelta = icpPointsDiff.map { it.z }

    calculatedLocationsDiffChart(timestamps, icpXDelta, icpYDelta, icpZDelta)
    coordinatesDiffChart(timestamps, Pair(realXDelta, icpXDelta), Pair(realYDelta, icpYDelta), Pair(realZDelta, icpZDelta))
    overlaidLocations(locations, calculated)
}

fun overlaidLocations(locations: List<Point>, calculated: List<Point>) {
    val realCoords = locations.toCoordinates()
    val calculatedCoords = calculated.toCoordinates()
    val locationsChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Real and calculated locations overlaid"
        xAxisTitle("X coordinates]")
        yAxisTitle("Y coordinates")
    }.build()
    locationsChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    locationsChart.addSeries("Real location", realCoords.first, realCoords.second)
    locationsChart.addSeries("Calculated location", calculatedCoords.first, calculatedCoords.second)
    SwingWrapper(locationsChart).displayChart()
}

fun coordinatesDiffChart(timestamps: List<Double>, x: Pair<List<Double>, List<Double>>, y: Pair<List<Double>, List<Double>>, z: Pair<List<Double>, List<Double>>) {
    val xDiffChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Difference between x coordinates"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Delta x")
    }.build()
    xDiffChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    xDiffChart.addSeries("Delta X", timestamps, x.first)
    xDiffChart.addSeries("Delta X calculated", timestamps, x.second)
    SwingWrapper(xDiffChart).displayChart()

    val yDiffChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Difference between y coordinates"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Delta y")
    }.build()
    yDiffChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    yDiffChart.addSeries("Delta Y", timestamps, y.first)
    yDiffChart.addSeries("Delta Y calculated", timestamps, y.second)
    SwingWrapper(yDiffChart).displayChart()

    val zDiffChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Difference between z coordinates"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Delta z")
    }.build()
    zDiffChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    zDiffChart.addSeries("Delta Z", timestamps, z.first)
    zDiffChart.addSeries("Delta Z calculated", timestamps, z.second)
    SwingWrapper(zDiffChart).displayChart()
}

fun calculatedLocationsDiffChart(timestamps: List<Double>, deltaX: List<Double>, deltaY: List<Double>, deltaZ: List<Double>) {
    val locationsDiffChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Locations differences between two consecutive points (calculated)"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Delta x, y and z")
    }.build()
    locationsDiffChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    locationsDiffChart.addSeries("Delta X", timestamps, deltaX)
    locationsDiffChart.addSeries("Delta Y", timestamps, deltaY)
    locationsDiffChart.addSeries("Delta Z", timestamps, deltaZ)
    SwingWrapper(locationsDiffChart).displayChart()
}

fun realLocationsDiffCharts(timestamps: List<Double>, deltaX: List<Double>, deltaY: List<Double>, deltaZ: List<Double>) {
    val locationsDiffChart = XYChartBuilder().apply {
        height(600)
        width(1000)
        chartTheme = Styler.ChartTheme.Matlab
        title = "Locations differences between two consecutive points"
        xAxisTitle("timestamps [s]")
        yAxisTitle("Delta x, y and z")
    }.build()
    locationsDiffChart.styler.legendPosition = Styler.LegendPosition.OutsideS
    locationsDiffChart.addSeries("Delta X", timestamps, deltaX)
    locationsDiffChart.addSeries("Delta Y", timestamps, deltaY)
    locationsDiffChart.addSeries("Delta Z", timestamps, deltaZ)
    SwingWrapper(locationsDiffChart).displayChart()
}