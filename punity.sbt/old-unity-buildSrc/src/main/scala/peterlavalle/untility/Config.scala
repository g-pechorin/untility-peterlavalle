package peterlavalle.untility

import java.io.File
import java.util

import org.gradle.api.Project

import scala.beans.BeanProperty

class Config {
  @BeanProperty
  var owner: Project = null

  /**
    * Unity won't import if there are compiler errors ... even when those errors would be fixed by an import
    */
  @BeanProperty
  var batchMode: Boolean = true

  val links = new util.LinkedList[AnyRef]()

  def setLink(other: Project): Unit = {

    // I depend on youuuuuu ...
    owner.findTask[UntiImportTask].dependsOn(other.findTask[UntiPackageTask])

    links.add(other)
  }


  @BeanProperty
  var unityHome: File =
    (for (version <- List("/5.1.4f1", "/5.6.0f3", ""); folder <- List("Program Files (x86)", "Program Files"); drive <- 'A' to 'Z')
      yield new File(s"$drive:/$folder/Unity$version"))
      .find { home: File => (home / "Editor/Unity.exe").exists() } match {
      case Some(file: File) => file
    }

  @BeanProperty
  var monoCompiler: File =
    osName match {
      case "windows" =>
        //unityHome / "Editor/Data/MonoBleedingEdge/bin/mcs.bat"
        //unityHome / "Editor/Data/Mono/lib/mono/2.0/gmcs"
        unityHome / "Editor/Data/MonoBleedingEdge/lib/mono/4.5/mcs.exe"
    }

  @BeanProperty
  var monoLibraries: Array[String] =
    Array(
      s"-r:${
        '"' + (osName match {
          case "windows" =>
            (unityHome / "Editor/Data/Mono/lib/mono/2.0/mscorlib.dll").AbsolutePath
        }) + '"'
      }",
      s"-r:${
        '"' + (osName match {
          case "windows" =>
            (unityHome / "Editor/Data/Mono/lib/mono/2.0/System.Core.dll").AbsolutePath
        }) + '"'
      }",
      "-nostdlib"
    )

  @BeanProperty
  var monoOptions: Array[String] =
    Array(
      "-target:library"
    )
}
