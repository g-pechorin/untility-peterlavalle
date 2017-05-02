package peterlavalle.untility

import java.io.File

class UntiAssembleEditorTask extends TUntiDLLTask {
  override val csFilter: String => Boolean = str => str.contains("/Editor/")
  override val label: String = "Editor"

  override def dllReferences: Stream[File] =
    dllUnityEditor #:: task[UntiAssemblePluginTask].dllAssembly #:: super.dllReferences

}
