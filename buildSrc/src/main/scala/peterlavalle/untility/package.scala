
package peterlavalle

import java.io.File

import scala.collection.immutable.Stream.Empty
import scala.languageFeature.implicitConversions._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

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
      (!value.exists()) || {
        requyre[Exception](
          value.isDirectory,
          s"Tried to unlink non-dir ${value.AbsolutePath}"
        )

        (value **).foreach {
          path =>
            require((value / path).delete())
        }
        value.delete()
      }


  }

}
