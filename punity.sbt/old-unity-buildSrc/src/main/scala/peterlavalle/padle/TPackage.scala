package peterlavalle.padle

import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask

import scala.reflect.ClassTag

/**
  * Gradle stuff
  */
trait TPackage extends peterlavalle.TPackage {

  import scala.language.implicitConversions

  implicit def wrapProject(project: Project): TWrappedProject =
    new TWrappedProject {
      override val value: Project = project
    }

  trait TWrappedProject extends peterlavalle.padle.TWrappedProject {
    def unityName = value.getName.replaceAll("\\.unity$", "")
  }


}
