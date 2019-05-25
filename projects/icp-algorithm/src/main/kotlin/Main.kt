import models.Frame
import models.basic.Point
import samples.SampleData

fun main(args: Array<String>)  {
    val data = setupData(args[0], onlyTransforms = true)
    showGraphs(data)
}





