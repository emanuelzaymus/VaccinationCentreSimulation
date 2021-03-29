package view

import app.Styles
import controller.MainController
import tornadofx.*
import kotlin.concurrent.thread

class MainView : View("Vaccination Centre Simulation") {

    private val mainController: MainController by inject()

    override val root = vbox {
        label(mainController.txt) {
            addClass(Styles.heading)
        }
        button("Start") {
            action {
                thread {
                    mainController.start()
                }
            }
        }
        button("Stop") {
            action { mainController.stop() }
        }

    }
}