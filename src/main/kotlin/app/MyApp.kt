package app

import javafx.stage.Stage
import random.RandomDistributionsWriter
import tornadofx.App
import view.MainView

class MyApp : App(MainView::class, Styles::class) {

    override fun start(stage: Stage) {
//        writeNumbers()
        super.start(stage)
    }

    private fun writeNumbers() {
        RandomDistributionsWriter.writeAll()
        super.stop()
    }

}