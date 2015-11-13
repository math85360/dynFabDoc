package org.dynfabdoc

import scalajs.js
import scalajs.js.JSApp
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.raw.ErrorEvent
import scala.language.implicitConversions
import scala.util.matching.Regex
import scalacss.ScalaCssReact._
import org.dynfabdoc.styles.Presentation
/**
 * @author mathi_000
 */
object ViewUrl {
  case class State(data: js.UndefOr[File] = js.undefined)

  type Props = (String, Branch)

  def downloadJsonFirst[A](urls: Seq[String], found: A => _, error: () => _) {
    val url = urls.head
    val req = new dom.XMLHttpRequest()
    req.open("GET", url, true)
    /*if(url.endsWith(".json")){
        
      }*/
    // TODO add API requirement req.setRequestHeader("Accept", "")
    def failed(ev: dom.raw.Event) {
      if (urls.tail.isEmpty)
        error()
      else
        downloadJsonFirst(urls.tail, found, error)
    }
    def loadData(ev: dom.raw.Event) {
      val response = req.response
      val status = req.status match {
        case 1223 => 204
        case 0 if response != null => 200
        case _ => req.status
      }
      if (status == 200) {
        val result = js.JSON.parse(response.asInstanceOf[String]).asInstanceOf[A]
        found(result)
      } else {
        failed(ev)
      }
    }
    req.onload = loadData _
    req.onerror = failed _
    req.onabort = (x: Any) => failed(null)
    req.send()
  }

  val view = ReactComponentB[Props]("view")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .componentDidMount(scope => Callback {
      // Make a request
      downloadJsonFirst[js.UndefOr[File]](
        Context.langs.map(x => scope.props._1 + (if (x.trim.length > 0) ("-" + x) else "") + ".json"),
        (response: js.UndefOr[File]) => scope.modState(_.copy(data = response)).runNow(),
        () => ())
      //val url = scope.props._1
      //

      //scope.modState(_.copy(data = result))
    })
    .build

  //implicit def asDict(x: Instruction) = x.asInstanceOf[js.Dictionary[String]]

  class Backend($: BackendScope[Props, State]) {
    val x = Seq().mkString("")
    def render(source: Props, state: State): ReactTagOf[org.scalajs.dom.html.Element] = {
      def e(value: String, cls: TagMod, transformValue: PartialFunction[String, TagMod]): TagMod = <.span(cls, if (transformValue.isDefinedAt(value)) transformValue(value) else value)
      def i(name: String, cls: TagMod, separator: String = null, newSeparator: String = null, transformValue: PartialFunction[String, TagMod] = PartialFunction.empty)(implicit in: js.Dictionary[String]): Option[TagMod] =
        in.get(name).map(j(_, cls, separator, newSeparator, transformValue))
      def j(content: String, cls: TagMod, separator: String = null, newSeparator: String = null, transformValue: PartialFunction[String, TagMod] = PartialFunction.empty): TagMod = {
        if (separator != null) {
          content.split(separator).map(e(_, cls, transformValue)).
            reduceLeft((o1, o2) => Seq(o1, <.span(newSeparator), o2))
        } else {
          e(content, cls, transformValue)
        }
      }
      def keys: PartialFunction[String, TagMod] = {
        case "SHIFT" => "MAJ"
        case "ENTER" => "ENTREE"
      }
      def suffixes()(implicit instr: js.Dictionary[String]): TagMod =
        instr.get("to").map(" pour " + _) +
          //i("shortcut",)
          instr.get("shortcut").map(s => <.span(Presentation.shortcut, " (Raccourci : ", j(s, Presentation.key, "\\+", "+", transformValue = keys), ")"))
      def convertInstruction()(implicit instr: js.Dictionary[String]): PartialFunction[String, TagMod] = {
        case "runProgram" =>
          <.div(Presentation.stepInstruction, "Ouvrir le programme ", i("program", Presentation.runProgram), suffixes)
        case "menu" =>
          <.div(Presentation.stepInstruction, "Cliquer sur le menu ",
            i("options", Presentation.menu, "\\|", " puis ") /*instr.get("options").map(options =>
              options.split("\\|").map(s(_, "menu")).reduceLeft((o1, o2) =>
                Seq(o1, <.span(" puis "), o2)))*/ , suffixes)
        case "keyboardInput" => //if (instr.contains("value"))
          <.div(Presentation.stepInstruction,  "Taper ", i("value", Presentation.keyboardInput), suffixes)
        case "keyboard" =>
          <.div(Presentation.stepInstruction,  "Appuyer sur ",
            i("key", Presentation.key, "\\+", "+", transformValue = keys) /*instr.get("key").map(options =>
              options.split("\\+").map(s(_, "key")).reduceLeft((o1, o2) =>
                Seq(o1, <.span(" + "), o2)))*/ , suffixes)
        case "mouseClick" =>
          <.div(Presentation.stepInstruction,  "Cliquer sur ", instr.get("icon").map("l'icône (" + _ + ")"),
            i("where", Presentation.clickOn, transformValue = {
              case "topLeft" => " en haut à gauche "
              case "topCenter" => " au centre en haut "
              case "topRight" => " en haut à droite "
              case "middleLeft" => " au milieu à gauche "
              case "middleCenter" => " au centre "
              case "middleRight" => " au milieu à droite "
              case "bottomLeft" => " en bas à gauche "
              case "bottomCenter" => " au centre en bas "
              case "bottomRight" => " en bas à droite "
            }), suffixes)
        //else
        //<.div("Appuyer sur ", i("program", "runProgram"))
        case "custom" =>
          <.div(Presentation.stepInstruction,  instr.get("content"))
        case action => <.div(Presentation.stepInstruction,  <.div(action), <.hr(), <.div(js.JSON.stringify(instr)), suffixes)
      }
      <.div(state.data.map({ data =>
        //val addedSteps = 
        var steps = data.instructions.getOrElse(js.Array())
        if (source._2 != null)
          source._2.instructions.map(_.foreach({ instructionToAdd =>
            val addedInstr = instructionToAdd.asInstanceOf[AddedInstruction]
            var insertBefore = addedInstr.insertBefore.map(_.split(",")).getOrElse(Array.empty)
            val hasBefore = insertBefore.nonEmpty
            var insertAfter = addedInstr.insertAfter.map(_.split(",")).getOrElse(Array.empty)
            val hasAfter = insertAfter.nonEmpty
            val afterIndex = steps.indexWhere({ item =>
              val id = item.id.getOrElse(null)
              if (insertAfter.contains(id)) {
                insertAfter = insertAfter.filterNot(_ == id)
                insertAfter.isEmpty
              } else {
                false
              }
            })
            val beforeIndex = steps.lastIndexWhere({ item =>
              val id = item.id.getOrElse(null)
              if (insertBefore.contains(id)) {
                insertBefore = insertBefore.filterNot(_ == id)
                insertBefore.isEmpty
              } else {
                false
              }
            })
            //
            if (hasBefore && beforeIndex != -1) {
              steps = (steps.take(beforeIndex) :+ instructionToAdd) ++ steps.drop(beforeIndex)
            } else if (hasAfter && afterIndex != -1) {
              steps = (steps.take(afterIndex) :+ instructionToAdd) ++ steps.drop(afterIndex)
            } else {
              steps += instructionToAdd
            }
          }))
        <.div(Presentation.content,
          data.title.map(<.h2(_)),
          //<.h2(data.title),
          <.ol(Presentation.instructions, data.instructions.map(instructions =>
            steps.zipWithIndex.map(instruction =>
              <.li(^.key := instruction._2.toString, Presentation.step, <.span(Presentation.stepNumber, instruction._2 + 1),
                instruction._1 match {
                  case AddedInstruction(v) =>
                    val customSteps = data.models.getOrElse(js.Array()).
                      find(_.id == v.modelId.getOrElse(null)).
                      map(_.instructions.getOrElse(js.Array()))
                      .getOrElse(js.Array())
                      //.toSeq
                      //
                      .map(step =>
                        convertInstruction()(
                          js.Dictionary((step.asInstanceOf[js.Dictionary[String]].toSeq ++ v.asInstanceOf[js.Dictionary[String]].toSeq): _*))(step.action.getOrElse(null)))
                      .zipWithIndex
                    customSteps.length match {
                      case 0 => <.div("non trouvé")
                      case 1 => <.div(customSteps.head._1)
                      case _ => <.div(<.ol(Presentation.instructions, customSteps.map(item => <.li(^.key := item._2.toString, Presentation.step, <.span(Presentation.stepNumber, item._2 + 1), item._1))))
                    }
                  case HasResultOrHowTo(v) => Seq[TagMod](
                    ViewResult.view.withKey("result")(v.result),
                    v.howto.map(howto =>
                      ListUrl.view.withKey("howto")((howto.source.getOrElse(null), v.howto.getOrElse(null)))))
                  case v =>
                    convertInstruction()(v.asInstanceOf[js.Dictionary[String]])(v.action.getOrElse(null))
                })))))
      }))
    }

  }
}
