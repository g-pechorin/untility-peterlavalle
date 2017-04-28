package peterlavalle.untility

import org.gradle.api.Plugin
import org.gradle.api.Project

class UntilityPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def unity = project.extensions.create('unity', Config)

        unity.owner = project

        def unityExport = project.task('unityExport', type: UnityExportTask)

        def unityImport = project.task('unityImport', type: UnityImportTask)

        def unityPlugin = project.task('unityPlugin', type: UnityPluginTask)

        unityExport.dependsOn(unityImport)
    }
}
