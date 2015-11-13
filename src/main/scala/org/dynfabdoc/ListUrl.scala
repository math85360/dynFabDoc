package org.dynfabdoc

import scalajs.js
import scalajs.js.JSApp
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import scalacss.ScalaCssReact._
import org.dynfabdoc.styles.Presentation
/**
 * @author mathi_000
 */
object ListUrl {
  case class State(data: js.UndefOr[js.Dictionary[String]] = js.undefined, base: String = "", active: String = "")

  type Props = (String, Branch)

  val view = ReactComponentB[Props]("include")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .componentDidMount(scope => Callback {
      val baseUrl = scope.props._1.dropWhile(_ == '/')
      val prefix = "https://raw.githubusercontent.com/dynFabDoc/"
      val suffix = (baseUrl match {
        case x if x.endsWith("?") =>
          val index = x.lastIndexOf('/') + 1
          val r = (x.take(index), "index")
          val name = x.drop(index).takeWhile(_ != '?')
          dom.console.info(name)
          scope.modState(_.copy(active = name)).runNow()
          r
        case x if x.endsWith("/") =>
          val r = (x, "index")
          r
        case x => (x, "")
      })
      val repo = suffix._1.takeWhile(_ != '/')
      val url = prefix + repo + "/master/" + suffix._1.drop(repo.length + 1)
      scope.modState(_.copy(base = url)).runNow()
      ViewUrl.downloadJsonFirst[js.UndefOr[js.Dictionary[String]]](
        Context.langs.map(x => url + suffix._2 + (if (x.trim.length > 0) ("-" + x) else "") + ".json"),
        (response: js.UndefOr[js.Dictionary[String]]) => scope.modState(s => s.copy(
          data = response,
          active = response.flatMap(
            _.collectFirst({
              case (k, v) if k.startsWith(s.active) => k
            })).getOrElse(None).
            getOrElse(s.active))).runNow(),
        () => ())
      //scope.modState(_.copy(data = result))
    })
    .build

  class Backend($: BackendScope[Props, State]) {
    def changeActive(e: ReactEventI) = {
      $.modState(_.copy(active = e.target.value))
    }
    def render(source: Props, state: State): ReactTagOf[org.scalajs.dom.html.Element] = {
      <.div(Presentation.includeHowto,
        state.data.map(map =>
          <.select(^.key:="fileChoice", Presentation.fileChoice, ^.onChange ==> changeActive, ^.value := state.active,
            <.option(^.value := "", ""), map.collect({
              case (value, title) if value != "title" => <.option(^.value := value, title)
            }))),
        state.data.map(_ =>
          state.active.trim.length != 0 ?= ViewUrl.view.withKey("content")((state.base + state.active, source._2))))
    }
  }
}
