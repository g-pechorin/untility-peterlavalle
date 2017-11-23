package peterlavalle.untility

class UntiAssemblePluginTask extends TUntiDLLTask {
  override val csFilter: String => Boolean = str => !str.contains("/Editor/")
  override val label: String = "Plugin"
}
