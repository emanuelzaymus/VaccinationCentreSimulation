package view

import app.Styles
import controller.MainController
import controller.Worker
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
//                row {
//                    label("Skip (%):")
//                    textfield(mainController.skip)
//                }
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

        vbox {
            hbox {
                addClass(Styles.biggerPaddingHeading)
                label("Current Replication Statistics") { addClass(Styles.bigHeading) }
            }
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
                    label("Average waiting time")
                    hbox(smallSpaces) {
                        label("In hours:")
                        label(mainController.regQueueAvgWaitingTimeInHours)
                    }
                    hbox(smallSpaces) {
                        label("In seconds:")
                        label(mainController.regQueueAvgWaitingTime)
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
                    label("Administrative workers") { addClass(Styles.smallHeading) }
                    tableview(mainController.regRoomPersonalWorkloads) {
                        prefWidth = 170.0
                        readonlyColumn("Num", Worker::id).prefWidth = 40.0
                        readonlyColumn("Working", Worker::working).prefWidth = 60.0
                        readonlyColumn("Workload", Worker::avgWorkload).prefWidth = 70.0
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
                    label("Average waiting time")
                    hbox(smallSpaces) {
                        label("In hours:")
                        label(mainController.examQueueAvgWaitingTimeInHours)
                    }
                    hbox(smallSpaces) {
                        label("In seconds:")
                        label(mainController.examQueueAvgWaitingTime)
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
                    label("Doctors") { addClass(Styles.smallHeading) }
                    tableview(mainController.examRoomPersonalWorkloads) {
                        prefWidth = 170.0
                        readonlyColumn("Num", Worker::id).prefWidth = 40.0
                        readonlyColumn("Working", Worker::working).prefWidth = 60.0
                        readonlyColumn("Workload", Worker::avgWorkload).prefWidth = 70.0
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
                    label("Average waiting time")
                    hbox(smallSpaces) {
                        label("In hours:")
                        label(mainController.vacQueueAvgWaitingTimeInHours)
                    }
                    hbox(smallSpaces) {
                        label("In seconds:")
                        label(mainController.vacQueueAvgWaitingTime)
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
                    label("Nurses") { addClass(Styles.smallHeading) }
                    tableview(mainController.vacRoomPersonalWorkloads) {
                        prefWidth = 170.0
                        readonlyColumn("Num", Worker::id).prefWidth = 40.0
                        readonlyColumn("Working", Worker::working).prefWidth = 60.0
                        readonlyColumn("Workload", Worker::avgWorkload).prefWidth = 70.0
                    }
                }

                vbox(smallSpaces) {
                    label("Waiting Room") { addClass(Styles.smallHeading) }
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
        }
        separator()

        hbox(smallSpaces) {
            addClass(Styles.smallPadding)
            label("Actual Simulation Time:") { addClass(Styles.smallHeading) }
            label(mainController.actualSimTime)
        }
        separator()


        vbox {
            hbox {
                addClass(Styles.biggerPaddingHeading)
                label("All Replications Statistics") { addClass(Styles.bigHeading) }
            }
            hbox(largeSpaces) {
                addClass(Styles.biggerPadding)
                vbox(smallSpaces) {
                    label("Registration Queue") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average length:")
                        label(mainController.allRegQueueAvgLength)
                    }
                    label("Average waiting time")
                    hbox(smallSpaces) {
                        label("In hours:")
                        label(mainController.allRegQueueAvgWaitingTimeInHours)
                    }
                    hbox(smallSpaces) {
                        label("In seconds:")
                        label(mainController.allRegQueueAvgWaitingTime)
                    }
                }
                vbox(smallSpaces) {
                    label("Administrative Workers") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average workload:")
                        label(mainController.allRegRoomWorkload)
                    }
                }

                vbox(smallSpaces) {
                    label("Examination Queue") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average length:")
                        label(mainController.allExamQueueAvgLength)
                    }
                    label("Average waiting time")
                    hbox(smallSpaces) {
                        label("In hours:")
                        label(mainController.allExamQueueAvgWaitingTimeInHours)
                    }
                    hbox(smallSpaces) {
                        label("In seconds:")
                        label(mainController.allExamQueueAvgWaitingTime)
                    }
                }
                vbox(smallSpaces) {
                    label("Doctors") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average workload:")
                        label(mainController.allExamRoomWorkload)
                    }
                }
                vbox(smallSpaces) {
                    label("Vaccination Queue") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average length:")
                        label(mainController.allVacQueueAvgLength)
                    }
                    label("Average waiting time")
                    hbox(smallSpaces) {
                        label("In hours:")
                        label(mainController.allVacQueueAvgWaitingTimeInHours)
                    }
                    hbox(smallSpaces) {
                        label("In seconds:")
                        label(mainController.allVacQueueAvgWaitingTime)
                    }
                }
                vbox(smallSpaces) {
                    label("Nurses") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average workload:")
                        label(mainController.allVacRoomWorkload)
                    }
                }
                vbox(smallSpaces) {
                    label("Waiting Room") { addClass(Styles.smallHeading) }
                    hbox(smallSpaces) {
                        label("Average waiting patients:")
                        label(mainController.allWaitRoomAvgLength)
                    }
//                hbox(smallSpaces) {
//                    label("+ / -:")
//                    label(mainController)
//                }
                }
            }
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