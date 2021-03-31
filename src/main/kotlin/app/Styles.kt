package app

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {

    companion object {
        val heading by cssclass()
        val biggerPadding by cssclass()
        val smallPadding by cssclass()
        val smallHeading by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        biggerPadding {
            padding = box(20.px)
        }

        smallPadding {
            padding = box(10.px, 20.px)
        }

        smallHeading {
            fontWeight = FontWeight.BOLD
        }
    }

}