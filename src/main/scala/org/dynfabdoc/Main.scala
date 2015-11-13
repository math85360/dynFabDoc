package org.dynfabdoc

import scalajs.js
import scalajs.js.JSApp
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.dynfabdoc.styles.Presentation
import scalacss.ScalatagsCss._
import scalacss.ScalatagsJsDomImplicits
import scalacss.Defaults._
import scalatags.JsDom.TypedTag
import org.scalajs.dom.raw.HTMLStyleElement

/**
 * @author mathi_000
 */

object Main extends JSApp {
  def main(): Unit = {
    import scalatags.JsDom.all._
    val mainDiv = div().render
    dom.document.body.appendChild(mainDiv)
    dom.document.head.appendChild(Presentation.render[TypedTag[HTMLStyleElement]].render)
    ReactDOM.render(
      MainView.view(
        "https://raw.githubusercontent.com/math85360/dynFabDoc-tutorials/master/chapin-de-noel"), mainDiv)
  }
}