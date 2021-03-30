package view

import app.Styles
import controller.MainController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class MainView : View("Vaccination Centre Simulation") {

    private val smallSpaces = 10
    private val largeSpaces = 30
    private val preferredWidth = 180.0

    private val mainController: MainController by inject()

    override val root = vbox {
        hbox(largeSpaces) {
            addClass(Styles.biggerPadding)
            gridpane {
                vgap = smallSpaces.toDouble()
                hgap = smallSpaces.toDouble()
                row {
                    label("Replications count:")
                    textfield(mainController.replicationsCount)
                }
                row {
                    label("Skip (%):")
                    textfield(mainController.skip)
                }
                row {
                    label("Number of patients per replication:")
                    textfield(mainController.numberOfPatients)
                }
                row {
                    label("Number of administrative workers:")
                    textfield(mainController.numberOfAdminWorkers)
                }
                row {
                    label("Number of doctors:")
                    textfield(mainController.numberOfDoctors)
                }
                row {
                    label("Number of nurses:")
                    textfield(mainController.numberOfNurses)
                }
            }
            vbox(smallSpaces) {
                checkbox("With animation", mainController.withAnimation)
                hbox(smallSpaces) {
                    label("Delay every (sim. seconds): ")
                    label(mainController.delayEvery)
                }
                slider(1..180) {
                    bind(mainController.delayEvery)
                    prefWidth = preferredWidth
                    isShowTickLabels = true
                    majorTickUnit = 89.0
                }
                hbox(smallSpaces) {
                    label("Delay for (milliseconds): ")
                    label(mainController.delayFor)
                }
                slider(10..3000) {
                    bind(mainController.delayFor)
                    prefWidth = preferredWidth
                    isShowTickLabels = true
                    majorTickUnit = 1490.0
                }
            }
            vbox(smallSpaces) {
                hbox(smallSpaces) {
                    label("Status: ")
                    label(mainController.state)
                }
                button("Start/Pause") {
                    action { mainController.startPause() }
                    prefWidth = preferredWidth
                }
                button("Stop") {
                    action { mainController.stop() }
                    prefWidth = preferredWidth
                }
            }
        }
        separator()

        hbox(largeSpaces) {
            addClass(Styles.biggerPadding)
            vbox(smallSpaces) {
                label("Registration Queue") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Actual length:")
                    label(mainController.regQueueActualLength)
                }
                hbox(smallSpaces) {
                    label("Average length:")
                    label(mainController.regQueueAvgLength)
                }
                hbox(smallSpaces) {
                    label("Average waiting time:")
                    label(mainController.regQueueAvgWaitingTime)
                    label("min")
                }
                label("Registration Room") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Busy workers:")
                    label(mainController.regRoomBusyWorkers)
                }
                hbox(smallSpaces) {
                    label("Average workload:")
                    label(mainController.regRoomWorkload)
                }
            }
            vbox(smallSpaces) {
                label("Examination Queue") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Actual length:")
                    label(mainController.examQueueActualLength)
                }
                hbox(smallSpaces) {
                    label("Average length:")
                    label(mainController.examQueueAvgLength)
                }
                hbox(smallSpaces) {
                    label("Average waiting time:")
                    label(mainController.examQueueAvgWaitingTime)
                    label("min")
                }
                label("Examination Room") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Busy doctors:")
                    label(mainController.examRoomBusyWorkers)
                }
                hbox(smallSpaces) {
                    label("Average workload:")
                    label(mainController.examRoomWorkload)
                }
            }
            vbox(smallSpaces) {
                label("Vaccination Queue") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Actual length:")
                    label(mainController.vacQueueActualLength)
                }
                hbox(smallSpaces) {
                    label("Average length:")
                    label(mainController.vacQueueAvgLength)
                }
                hbox(smallSpaces) {
                    label("Average waiting time:")
                    label(mainController.vacQueueAvgWaitingTime)
                    label("min")
                }
                label("Vaccination Room") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Busy nurses:")
                    label(mainController.vacRoomBusyWorkers)
                }
                hbox(smallSpaces) {
                    label("Average workload:")
                    label(mainController.vacRoomWorkload)
                }
            }
            vbox(smallSpaces) {
                label("Waiting Queue") { addClass(Styles.smallHeading) }
                hbox(smallSpaces) {
                    label("Actual waiting patients:")
                    label(mainController.waitRoomPatientsCount)
                }
                hbox(smallSpaces) {
                    label("Average waiting patients:")
                    label(mainController.waitRoomAvgLength)
                }
//                hbox(smallSpaces) {
//                    label("+ / -:")
//                    label(mainController)
//                }
            }
        }
        separator()

        hbox(smallSpaces) {
            addClass(Styles.smallPadding)
            label("Actual Simulation Time:") { addClass(Styles.smallHeading) }
            label(mainController.actualSimTime)
        }
        separator()

        hbox {
            linechart("Chart", NumberAxis(), NumberAxis()) {
            }
            linechart("Chart", NumberAxis(), NumberAxis()) {
            }
        }
    }

}