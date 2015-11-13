package org.dynfabdoc

import scalajs.js
import scalajs.js.JSApp
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.dynfabdoc.styles.Presentation
import scalacss.ScalaCssReact._
/**
 * @author mathi_000
 */
object MainView {
  case class State(
    url: String = "",
    repositories: js.Array[Any] = js.Array())

  val view = ReactComponentB[String]("main-view")
    .initialState_P({ url => State(url) })
    .backend(new Backend(_))
    .renderBackend
    .build

  class Backend($: BackendScope[String, State]) {
    def changeUrl(e: ReactEventI): Callback = $.modState(_.copy(url = e.target.value))

    def render(state: State) = {
      val turl = state.url.trim()
      val ourl = if (turl.length() > 0) Some(turl) else None
      <.div(Presentation.main,
        <.input(Presentation.url, ^.value := state.url, ^.onChange ==> changeUrl),
        ourl.map(url => ViewUrl.view((url, null))))
    }
  }
}