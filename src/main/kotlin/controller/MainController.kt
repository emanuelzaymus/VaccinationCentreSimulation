package controller

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller
import java.util.concurrent.atomic.AtomicBoolean

class MainController : Controller() {

    private var stopped: AtomicBoolean = AtomicBoolean(false)

    val txt = SimpleStringProperty("Nothing")

    fun start() {
        stopped.set(false)
        run()
    }

    fun stop() {
        stopped.set(true)
    }

    private fun run() {
        for (i: Int in 1..100) {
            if (stopped.get()) {
                break
            }
            Platform.runLater { txt.value = i.toString() }
//            txt.value = i.toString()
            Thread.sleep(100)
        }
    }

}