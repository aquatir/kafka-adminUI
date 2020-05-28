package ru.kafkaadmin

import tornadofx.*


fun main(args: Array<String>) {
    launch<MyApp>(args)
}

class MyApp: App(MyView::class)

class MyView: View() {
    override val root = vbox {
        button("press me!")
        label("waiting")
    }
}
