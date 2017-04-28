package peterlavalle.untility

import org.gradle.api.tasks.TaskAction

class UnityPluginTask extends TUnityTask {
  @TaskAction
  def action(): Unit = {
    "compile all code" halt
  }
}
