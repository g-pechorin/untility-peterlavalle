package peterlavalle

import java.io.{File, FileReader, Writer}

import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask

import scala.beans.BeanProperty
import scala.languageFeature.implicitConversions._
import scala.reflect.ClassTag

package object untility extends peterlavalle.padle.TPackage {

  implicit def wrapProject(project: Project): TWrappedProject =
    new TWrappedProject {
      override val value: Project = project
    }

  trait TWrappedProject extends peterlavalle.padle.TWrappedProject {
    def unityName = value.getName.replaceAll("\\.unity$", "")
  }


}
