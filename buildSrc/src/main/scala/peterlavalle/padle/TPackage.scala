package peterlavalle.padle

import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask

import scala.reflect.ClassTag

/**
  * Gradle stuff
  */
trait TPackage extends peterlavalle.TPackage {

  lazy val osName: String =
    System.getProperty("os.name").split("[^\\w]")(0).toLowerCase
}
