package org.dynfabdoc

import scalajs.js
import scalajs.js.JSApp
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom

/**
 * @author mathi_000
 */

@js.native
class File extends Branch {
  var models: js.UndefOr[js.Array[Model]] = js.native
  var title: js.UndefOr[String] = js.native
}

@js.native
sealed abstract class Instruction extends Identified {
  var action: js.UndefOr[String] = js.native
}

@js.native
trait HasResult extends js.Object {
  var result: js.UndefOr[js.Array[String]] = js.native
}

@js.native
trait HasHowTo extends js.Object {
  var howto: js.UndefOr[HowTo] = js.native
}

@js.native
class HowTo extends js.Object with Branch {
  var source: js.UndefOr[String] = js.native
}

@js.native
class HasResultOrHowTo extends Instruction with HasResult with HasHowTo {
}
object HasResultOrHowTo {
  def unapply(x: Instruction): Option[HasResultOrHowTo] = if (x.action.getOrElse(null) == "resultOrHowto") Some(x.asInstanceOf[HasResultOrHowTo]) else None
}

@js.native
class Model extends Branch with Identified {
  var content: String = js.native
}

@js.native
trait Branch extends js.Object {
  var instructions: js.UndefOr[js.Array[Instruction]] = js.native
}

@js.native
trait Identified extends js.Object {
  var id: js.UndefOr[String] = js.native
}
@js.native
trait MayHaveModel extends js.Object {
  var modelId: js.UndefOr[String] = js.native
}
@js.native
class AddedInstruction extends Instruction with MayHaveModel {
  var insertBefore: js.UndefOr[String] = js.native
  var insertAfter: js.UndefOr[String] = js.native
}
object AddedInstruction {
  def unapply(x: Instruction): Option[AddedInstruction] = if (x.asInstanceOf[MayHaveModel].modelId.isDefined) Some(x.asInstanceOf[AddedInstruction]) else None
}