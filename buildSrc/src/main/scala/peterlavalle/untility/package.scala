
package peterlavalle

import java.io.File

import scala.languageFeature.implicitConversions._
import scala.reflect.ClassTag

package object untility extends peterlavalle.padle.TPackage {

  implicit def requyre[E <: Exception](condition: Boolean, messsage: => String)(implicit tag: ClassTag[E]): Unit = {
    if (!condition)
      throw tag.runtimeClass.getConstructor(classOf[String]).newInstance(messsage).asInstanceOf[E]
  }

  implicit def wrapFile2(file: File): TWrappedFile2 =
    if (file != file.getAbsoluteFile)
      wrapFile2(file.getAbsoluteFile)
    else
      new TWrappedFile2 {
        override val value: File = file
      }

  sealed trait TWrappedFile2 {
    val value: File

    def unlink(): Boolean =
      (!value.exists()) || (
        if (value.isDirectory)
          value.listFiles().foldLeft(true)((v, f) => f.unlink() && v) && value.delete()
        else
          value.delete()
        )
  }

}
