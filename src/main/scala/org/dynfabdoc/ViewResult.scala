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

object ViewResult {
  private val images = Seq(
    ".jpg",
    ".jpeg",
    ".bmp",
    ".png",
    ".gif",
    ".svg")
  case class State()

  type Props = js.UndefOr[js.Array[String]]
  val view = ReactComponentB[Props]("result")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .build

  class Backend($: BackendScope[Props, State]) {

    def render(props: Props, state: State) = {
      /*val turl = state.url.trim()
      val ourl = if (turl.length() > 0) Some(turl) else None
      <.div(
        <.input(^.value := state.url, ^.onChange ==> changeUrl),
        ourl.map(ViewUrl.view(_)))*/
      <.div(Presentation.result,props.map(_.map(url => url.toLowerCase() match {
        case x if x.startsWith("image:") => <.img(^.src := x.dropWhile(_ != ":").drop(1))
        case x if images.exists(x.endsWith(_)) => <.img(^.src := url)
        case x => <.a(^.href := url, url)
      })))
    }
  }
}