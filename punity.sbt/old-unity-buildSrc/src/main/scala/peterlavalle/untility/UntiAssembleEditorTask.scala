package peterlavalle.untility

import java.io.File

class UntiAssembleEditorTask extends TUntiDLLTask {
  override val csFilter: String => Boolean = str => str.contains("/Editor/")
  override val label: String = "Editor"

  override def dllReferences: Stream[File] =
    task[UntiAssemblePluginTask].dllAssembly match {
      case None =>
        super.dllReferences
      case Some(plugin) =>
        dllUnityEditor #:: plugin #:: super.dllReferences
    }
}
