package ru.kafkaadmin

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.PasswordField
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import tornadofx.*
import java.time.LocalDate
import java.time.Period


fun main(args: Array<String>) {
    launch<MyApp>(args)
}

class MyApp : App(MainView::class)

class MainView : View() {
    // val bottomView: BottomView by inject()
    //val bottomView = find(BottomView::class)

    val model: ListOfValuesModel by inject()


    // TODO: Make 2 views switch places on button press. Can be fun!

    var login: TextField by singleAssign()
    var password: PasswordField by singleAssign()
    var passwordPlain: TextField by singleAssign()
    val booleanProperty = SimpleBooleanProperty()

    val colors = FXCollections.observableArrayList("Red", "Green", "Blue")
    val colorPicked = SimpleStringProperty()
    val userBirthDate = SimpleObjectProperty<LocalDate>()

    override val root = vbox {
        hbox {
            label("Login") {
                textFill = Color.BLACK
            }
            login = textfield()
        }
        hbox {
            label("Password") {
                textFill = Color.DEEPPINK
            }
            password = passwordfield {
                visibleProperty().bind(!booleanProperty)
                managedProperty().bind(!booleanProperty)
                requestFocus()
            }
            passwordPlain = textfield {
                visibleProperty().bind(booleanProperty)
                managedProperty().bind(booleanProperty)
            }
            booleanProperty.set(false)
            checkbox("show", booleanProperty)

            passwordPlain.textProperty().bindBidirectional(password.textProperty())
        }


        hbox {
            label("Favourite color") { textFill = Color.DARKGREEN }
            combobox(values = colors, property = colorPicked)
            colorPicked.set(colors.first())
        }
        hbox {
            label("Date of birth") { textFill = Color.FUCHSIA }
            datepicker(userBirthDate) {
                value = LocalDate.now()
            }
        }
        button("LOGIN") {
            useMaxWidth = true
            textFill = Color.PURPLE
            action {
                requestFocus()
                println("Logging is with login '${login.text}' and password '${password.text}' and color: '${colorPicked.get()}' and DOB: '${userBirthDate.get()}'")
            }
            shortcut("ENTER")
        }

        listview<String> {
            items.add("Alpha")
            items.add("Beta")
            items.add("Gamma")
            items.add("Delta")
            items.add("Epsilon")
            selectionModel.selectionMode = SelectionMode.MULTIPLE
        }

        val persons = listOf(
                Person(1,"Samantha Stuart",LocalDate.of(1981,12,4)),
                Person(2,"Tom Marks",LocalDate.of(2001,1,23)),
                Person(3,"Stuart Gills",LocalDate.of(1989,5,23)),
                Person(3,"Nicole Williams",LocalDate.of(1998,8,11))
        ).asObservable()

        tableview(persons) {
            column("ID",Person::id).makeEditable()
            readonlyColumn("Name", Person::name)
            readonlyColumn("Birthday", Person::birthday)
            readonlyColumn("Age",Person::age)
        }
    }
//    override val root = vbox {
//        add(TopView::class)
//        button("Press Me") {
//            action {
//                openInternalWindow<TopView>()
//            }
//        }
//    }

//    override val root = vbox {
//        label("My items")
//        listview(model.values)
//
//        button("Reset pick") {
//            action {
//                runAsync {
//                    println("kek")
//                } ui {
//                    if (model.values.isNotEmpty()) model.values.remove(model.values.last())
//                }
//            }
//        }
//    }

//    override val root = form {
//
//        fieldset {
//            field("input") {
//                textfield(input)
//            }
//
//            button("Action!") {
//                action {
//                    controller.writeStuff(input.value)
//                    input.value = ""
//                }
//            }
//        }
//
////        top<TopView>()
////        bottom<BottomView>()
//    }

}


// TaskStatus ViewModel - display progress bar

class Person(id: Int, val name: String, val birthday: LocalDate) {

    var id by property(id)
    fun idProperty() = getProperty(Person::id)

    val age: Int get() = Period.between(birthday, LocalDate.now()).years
}

class TopView : View() {
    override val root = label("Top view")
}


class BottomView : View() {
    override val root = label("Bottom view")
}

class TextFieldController : Controller() {
    fun writeStuff(input: String) {
        println("button with '$input' pressed!")
    }
}

class ListOfValuesModel : Controller() {
    val values = FXCollections.observableArrayList("Red", "Green", "MAHOGANY", "Blue")
}

class MyFragment : Fragment() {
    override val root = label("This is a popup")
}

class MyView1 : View() {
    override val root = vbox {
        button("Go to MyView2") {
            action {
                replaceWith(MyView2::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }

    override fun onDock() {
        println("Docking MyView1!")
    }

    override fun onUndock() {
        println("Undocking MyView1!")
    }
}

class MyView2 : View() {
    override val root = vbox {
        button("Go to MyView1") {
            action {
                replaceWith(MyView1::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
            }
        }
    }

    override fun onDock() {
        println("Docking MyView2!")
    }

    override fun onUndock() {
        println("Undocking MyView2!")
    }
}
