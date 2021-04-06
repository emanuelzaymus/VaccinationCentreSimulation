package app

import tornadofx.App
import view.MainView

/**
 * Main Application - Entry point
 */
class MyApp : App(MainView::class, Styles::class)