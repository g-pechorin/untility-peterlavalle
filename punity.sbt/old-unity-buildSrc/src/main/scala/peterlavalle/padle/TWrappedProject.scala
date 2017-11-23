package peterlavalle.padle

import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask

import scala.reflect.ClassTag

trait TWrappedProject {
  val value: Project

  def findTask[T <: AbstractTask](implicit classTag: ClassTag[T]): T = {

    val runtimeClass = classTag.runtimeClass

    def dwarf(input: String): String =
      if (input.endsWith("Task"))
        dwarf(input.dropRight(4))
      else if (input.head.isUpper && !input.tail.head.isUpper)
        s"${input.head.toLower}${input.tail}"
      else {
        def recu(text: List[Char]): String =
          text match {
            case f :: s :: tail if f.isUpper && s.isUpper => s"${f.toLower}${recu(s :: tail)}"
            case h :: tail => s"$h${new String(tail.toArray)}"
          }

        recu(input.toList)
      }

    require("ccgList" == dwarf("CCGList"))
    require("unityExport" == dwarf("UnityExportTask"))

    runtimeClass.cast(
      value.getTasks.getByPath(dwarf(runtimeClass.getSimpleName))
    ).asInstanceOf[T]
  }
}
