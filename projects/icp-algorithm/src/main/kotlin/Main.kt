import models.Data
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.absoluteValue

fun main(args: Array<String>)  {
    // Load ground truth
    val groundTruth: Data = loadGroundTruth(args[0])
    // Calculate differences between two time frames
    val differences = calculateDifferences(groundTruth)
    val icpResult = loadICPResults(args[0])

    differences.zip(icpResult).forEach {
        println("""
            ${it.second.between.first} - ${it.second.between.second}
            Δx ${it.first.first.first}, ${it.second.translation.x()}
            Δy ${it.first.first.second}, ${it.second.translation.y()}
            Δz ${it.first.first.third}, ${it.second.translation.z()}
            Δroll ${it.first.second.first}, ${it.second.angles.roll}
            Δpitch ${it.first.second.second}, ${it.second.angles.pitch}
            Δyaw ${it.first.second.third}, ${it.second.angles.yaw}
        """.trimIndent())
        println()
    }

    val timestamps = groundTruth.groundTruth.map { it.timestamp }.dropLast(1)
    val realx = differences.map { it.first }.map { it.first.toDouble().absoluteValue }
    val realy = differences.map { it.first }.map { it.second.toDouble().absoluteValue }
    val realz = differences.map { it.first }.map { it.third.toDouble().absoluteValue }
    val icpx = icpResult.map { it.translation.x().absoluteValue }
    val icpy = icpResult.map { it.translation.y().absoluteValue }
    val icpz = icpResult.map { it.translation.z().absoluteValue }
    println("${timestamps.size} ${realx.size} ${icpx.size}")

//    val chartx = XYChartBuilder().apply {
//        height(600)
//        width(1000)
//        chartTheme = Styler.ChartTheme.Matlab
//        title = "Δx comparison"
//        xAxisTitle("Δx value")
//        yAxisTitle("Timestamp [s]")
//    }.build()
//    chartx.styler.legendPosition = Styler.LegendPosition.OutsideS
//    chartx.addSeries("Δx real", realx, timestamps)
//    chartx.addSeries("Δx icp", icpx, timestamps)
//    SwingWrapper(chartx).displayChart()
//
//    val charty = XYChartBuilder().apply {
//        height(600)
//        width(1000)
//        chartTheme = Styler.ChartTheme.Matlab
//        title = "Δy comparison"
//        xAxisTitle("Δy value")
//        yAxisTitle("Timestamp [s]")
//    }.build()
//    charty.styler.legendPosition = Styler.LegendPosition.OutsideS
//    charty.addSeries("Δy real", realy, timestamps)
//    charty.addSeries("Δy icp", icpy, timestamps)
//    SwingWrapper(charty).displayChart()
//
//    val chartz = XYChartBuilder().apply {
//        height(600)
//        width(1000)
//        chartTheme = Styler.ChartTheme.Matlab
//        title = "Δz comparison"
//        xAxisTitle("Δz value")
//        yAxisTitle("Timestamp [s]")
//    }.build()
//    chartz.styler.legendPosition = Styler.LegendPosition.OutsideS
//    chartz.addSeries("Δz real", realz, timestamps)
//    chartz.addSeries("Δz icp", icpz, timestamps)
//    SwingWrapper(chartz).displayChart()

    val realRoll = differences.map { it.second }.map { it.first.toDouble().absoluteValue }
    val realPitch = differences.map { it.second }.map { it.second.toDouble().absoluteValue }
    val realYaw = differences.map { it.second }.map { it.third.toDouble().absoluteValue }
    val icpRoll = icpResult.map { it.angles.roll.absoluteValue }
    val icpPitch = icpResult.map { it.angles.pitch.absoluteValue }
    val icpYaw = icpResult.map { it.angles.yaw.absoluteValue }

//    val chartyaw = XYChartBuilder().apply {
//        height(600)
//        width(1000)
//        chartTheme = Styler.ChartTheme.Matlab
//        title = "Δyaw comparison"
//        xAxisTitle("Δyaw value")
//        yAxisTitle("Timestamp [s]")
//    }.build()
//    chartyaw.styler.legendPosition = Styler.LegendPosition.OutsideS
//    chartyaw.addSeries("Δyaw real", realYaw, timestamps)
//    chartyaw.addSeries("Δyaw icp", icpYaw, timestamps)
//    SwingWrapper(chartyaw).displayChart()
//
//    val chartroll = XYChartBuilder().apply {
//        height(600)
//        width(1000)
//        chartTheme = Styler.ChartTheme.Matlab
//        title = "Δroll comparison"
//        xAxisTitle("Δroll value")
//        yAxisTitle("Timestamp [s]")
//    }.build()
//    chartroll.styler.legendPosition = Styler.LegendPosition.OutsideS
//    chartroll.addSeries("Δroll real", realRoll, timestamps)
//    chartroll.addSeries("Δroll icp", icpRoll, timestamps)
//    SwingWrapper(chartroll).displayChart()
//
//    val chartpitch = XYChartBuilder().apply {
//        height(600)
//        width(1000)
//        chartTheme = Styler.ChartTheme.Matlab
//        title = "Δpitch comparison"
//        xAxisTitle("Δpitch value")
//        yAxisTitle("Timestamp [s]")
//    }.build()
//    chartpitch.styler.legendPosition = Styler.LegendPosition.OutsideS
//    chartpitch.addSeries("Δpitch real", realPitch, timestamps)
//    chartpitch.addSeries("Δpitch icp", icpPitch, timestamps)
//    SwingWrapper(chartpitch).displayChart()

//    showGraphs(groundTruth)

}

fun calculateDifferences(data: Data): List<Pair<Triple<Float, Float, Float>, Triple<Float, Float, Float>>> {
    return data.groundTruth.windowed(2, 1, false).map {
        val first = it[0]
        val second = it[1]
        val deltax = second.transform.location.x - first.transform.location.x
        val deltay = second.transform.location.y - first.transform.location.y
        val deltaz = second.transform.location.z - first.transform.location.z

        val deltaYaw = second.transform.rotation.yaw - first.transform.rotation.yaw
        val deltaRoll = second.transform.rotation.roll - first.transform.rotation.roll
        val deltaPitch = second.transform.rotation.pitch - first.transform.rotation.pitch

        Pair(Triple(deltax, deltay, deltaz), Triple(deltaRoll, deltaPitch, deltaYaw))
    }
}



