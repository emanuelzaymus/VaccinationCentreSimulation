package view

import controller.MainController
import javafx.scene.chart.NumberAxis
import tornadofx.View
import tornadofx.linechart
import tornadofx.series
import tornadofx.vbox

class ExaminationQueueChart : View("Examination Queue Chart") {

    private val mainController: MainController by inject()

    override val root = vbox {
        val xAxis = NumberAxis()
        xAxis.label = "Time"
        val yAxis = NumberAxis()
        yAxis.label = "Examination Queue Length"
        linechart("Examination Queue", xAxis, yAxis) {
            series("Patient counts", mainController.examinationQueueChartData)
            createSymbols = false
        }
    }
}
