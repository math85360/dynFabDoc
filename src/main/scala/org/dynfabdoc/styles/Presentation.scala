package org.dynfabdoc.styles

import scalacss.Defaults._
import scalacss.Attr
import scalacss.ValueT
import scalacss.Transform
import scalacss.CanIUse
import scala.language.postfixOps
import scalatags.Text.implicits._
import scalatags.Text.Util
import scalatags.Text.{ svgTags => S, svgAttrs => s }
import java.nio.charset.StandardCharsets
/**
 * @author mathi_000
 */
object Presentation extends StyleSheet.Inline {
  import dsl._
  val myLightGray = "#D9D9D9"
  val myHover = "#FFC107"
  //val myGray = 

  val roundedBorder = borderRadius(0.33.em)

  val main = style( //fontFamily:="sans-serif"
    display.flex,
    flexDirection.column,
    lineHeight(1.5),
    color(c"#9E9E9E"),
    media.print(fontSize(8.pt)))

  val url = style()

  val content = style()

  val includeHowto = style(display.flex, flexDirection.column, flex := "1")

  val fileChoice = style(media.print(display.none))

  val key = style(color.black,
    backgroundColor :=! myLightGray,
    padding(0.1.em, 0.2.em),
    margin(0.1.em, 0.2.em),
    border(0.1.em, solid, black),
    roundedBorder,
    boxShadow := "0.1em 0.1em 0.1em #000")

  val shortcut = style(fontSize(0.75.em))

  val menu = style(
    color.white,
    backgroundColor(c"#3F51B5"),
    padding(0.2.em),
    roundedBorder,
    boxShadow := "0.1em 0.1em 0.3em " + myLightGray)

  val runProgram = style()

  val keyboardInput = style(
    fontFamily := "monospace",
    color.black,
    fontWeight.bold,
    boxShadow := "0.1em 0.1em 0.3em #9C27B0",
    padding(0.1.em, 0.2.em))

  val clickOn = style()

  val step = style(
    display.flex,
    flexDirection.row,
    borderTop(1.px, solid, black),
    &.firstChild(
      borderTop.none),
    padding(0.25.em, 0.1.em),
    borderLeft(0.25.em, solid, c"#FFF"),
    borderRight(0.25.em, solid, c"#FFF"),
    transition := "border 0.5s ease,background-color 0.5s ease",
    media.print.hover(
      borderLeftColor.white.important,
      borderRightColor.white.important,
      backgroundColor.white.important),
    media.screen.hover(
      borderLeftColor :=! myHover,
      borderRightColor :=! myHover,
      color.black,
      backgroundColor :=! "rgba(255,193,7,0.25)"),

    flexWrap.wrap)

  val stepInstruction = style(flex := "1")

  val instructions = style(margin.`0`, padding.`0`, display.flex, flexDirection.column)

  val stepNumber = style(fontSize(2.em), marginRight(0.1.em)) // How to have a minimal size

  val contentWrapper = style()

  val noSubStep = style()
  val oneSubStep = style()
  val multiSubSteps = style(display.flex, flexDirection.column, flex := "1")

  val result = style()
}
