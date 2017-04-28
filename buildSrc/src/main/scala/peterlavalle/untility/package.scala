
package peterlavalle

import java.io.File

import scala.collection.immutable.Stream.Empty
import scala.languageFeature.implicitConversions._

package object untility extends peterlavalle.padle.TPackage {


  implicit def wrapFile2(file: File): TWrappedFile2 =
    if (file != file.getAbsoluteFile)
      wrapFile2(file.getAbsoluteFile)
    else
      new TWrappedFile2 {
        override val value: File = file
      }

  sealed trait TWrappedFile2 {
    val value: File

    def unlink: Boolean =
      (!value.exists()) || {
        require(value.isDirectory)

        (value **).foreach {
          path =>
            require((value / path).delete())
        }
        value.delete()
      }




  }

}
