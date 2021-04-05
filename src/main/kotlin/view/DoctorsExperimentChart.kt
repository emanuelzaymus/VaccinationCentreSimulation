package view

import controller.MainController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class DoctorsExperimentChart : View("Doctors Experiment") {

    private val mainController: MainController by inject()

    override val root = vbox {
        val xAxis = NumberAxis()
        xAxis.label = "Number of Doctors"
        val yAxis = NumberAxis()
        yAxis.label = "Avg. Exam. Queue Length"
        linechart("Doctors - Examination Queue Length Experiment", xAxis, yAxis) {
            series("Patient counts", mainController.doctorsExamQueueChartData)
            isLegendVisible = false
        }
    }
}
