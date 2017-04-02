package peterlavalle

import java.io.{File, FileReader, Writer}

import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask

import scala.beans.BeanProperty
import scala.languageFeature.implicitConversions._
import scala.reflect.ClassTag

package object untility {

  lazy val osName: String =
    System.getProperty("os.name").split("[^\\w]")(0).toLowerCase

  lazy val gdbPath: String = {
    import sys.process._
    osName match {
      case "windows" => "C:/tools/mingw64/bin/gdb.exe"
      case "linux" | "mac" => new File((Seq("which", "gdb") !!).trim).AbsolutePath
    }
  }

  def ??? : Nothing = {
    val base = new NotImplementedError
    base.setStackTrace(base.getStackTrace.tail)

    val notImplementedError = new NotImplementedError(s"${base.getMessage} @ ${base.getStackTrace.head.toString.trim}")
    notImplementedError.setStackTrace(base.getStackTrace)
    throw notImplementedError
  }

  implicit def wrapFile(file: File): TWrappedFile =
    if (file != file.getAbsoluteFile)
      wrapFile(file.getAbsoluteFile)
    else
      new TWrappedFile {
        override val value: File = file
      }


  sealed trait TWrappedFile {
    val value: File

    def shell(feedback: Feedback)(commands: String*): Int = {
      require(value.isDirectory || value.mkdirs())
      import sys.process._
      Process(
        osName match {
          case "windows" => Seq("CMD", "/C") ++ commands
        },
        value
      ) ! ProcessLogger(feedback.out, feedback.err)
    }

    def ** : Stream[String] = {

      def recu(todo: List[String]): Stream[String] =
        todo match {
          case Nil => Stream.Empty

          case path :: tail =>
            val file = value / path

            if (file.isDirectory)
              recu(tail ++ file.list().map(path + '/' + _))
            else
              path #:: recu(tail)
        }

      require(null != value)
      recu(value.list() match {
        case null => Nil
        case list => list.toList
      })
    }

    def **(pattern: String): Stream[String] = ** filter (_ matches pattern)

    def /(path: String): File = {

      def recu(file: File, todo: List[String]): File =
        todo match {
          case ".." :: tail =>
            recu(file.getParentFile, tail)
          case next :: tail =>
            recu(
              new File(file, next),
              tail
            )
          case Nil =>
            file
        }

      recu(
        value,
        path.split("/").toList
      )
    }

    def AbsolutePath: String =
      value.getAbsolutePath.replace('\\', '/')

    def isNewer(other: File): Boolean =
      !(other.exists() && value.lastModified() < other.lastModified())

    def isOlder(stamp: Long): Boolean =
      value.lastModified() <= stamp

    import scala.language.postfixOps

    def makeFolder: File = {
      require(value.exists() || value.mkdirs())
      value
    }

    def makeParentFolder: File = {
      require(value.getParentFile.exists() || value.getParentFile.mkdirs())
      value
    }

    def makeString: String = {
      val fileReader = new FileReader(value)

      def recu: String = {
        val buffer = Array.ofDim[Char](64)
        fileReader.read(buffer) match {
          case -1 =>
            fileReader.close()
            ""
          case read =>
            new String(buffer.take(read)) + recu
        }

      }

      recu
    }
  }

  implicit def wrapIterable[T](list: Iterable[T]): TWrappedIterable[T] =
    new TWrappedIterable[T] {
      override val value: Iterable[T] = list
    }

  sealed trait TWrappedIterable[T] {
    val value: Iterable[T]

    def foldIn(fold: (T, T) => T): T =
      value.tail.foldLeft(value.head)(fold)
  }

  implicit def wrapWriter[W <: Writer](writer: W): TWrappedWriter[W] =
    new TWrappedWriter[W] {
      override val value: W = writer
    }

  sealed trait TWrappedWriter[W <: Writer] {
    val value: W

    def appund[T](monad: Option[T])(tostr: T => String): W =
      monad match {
        case None => value
        case Some(thing) =>
          value.appund(tostr(thing))
      }

    def appund[E](many: Iterable[E])(tostr: E => String): W =
      appund(many.iterator)(tostr)

    def appund[E](many: Iterator[E])(tostr: E => String): W =
      many.foldLeft(value)((w: W, e: E) => w.appund(tostr(e)))

    def appund[E](text: String): W =
      value.append(text).asInstanceOf[W]
  }

  implicit def wrapProject(project: Project): TWrappedProject =
    new TWrappedProject {
      override val value: Project = project
    }

  sealed trait TWrappedProject {
    val value: Project

    def findTask[T <: AbstractTask](implicit classTag: ClassTag[T]): T = {

      val runtimeClass = classTag.runtimeClass

      def dwarf(text: List[Char]): String =
        text match {
          case f :: s :: tail if f.isUpper && s.isUpper => s"${f.toLower}${dwarf(s :: tail)}"
          case h :: tail => s"$h${new String(tail.toArray)}"
        }

      runtimeClass.cast(
        value.getTasks.getByPath(dwarf(runtimeClass.getSimpleName.toList))
      ).asInstanceOf[T]
    }
  }

  implicit def wrapString(string: String): TWrappedString =
    new TWrappedString {
      override val value: String = string
    }

  sealed trait TWrappedString {
    val value: String

    def stripTrim: String = value.stripMargin.trim + '\n'

    def tailTrim: String = value.stripMargin.replaceAll("\\s*$", "\n")

    def halt: Nothing = {

      val base = new RuntimeException
      base.setStackTrace(base.getStackTrace.tail.tail)

      val runtimeException = new RuntimeException(s"$value @ ${base.getStackTrace.head.toString.trim}")
      runtimeException.setStackTrace(base.getStackTrace)
      throw runtimeException
    }
  }

}
