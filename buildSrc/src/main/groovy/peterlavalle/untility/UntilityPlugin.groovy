package peterlavalle.untility

import org.gradle.api.Plugin
import org.gradle.api.Project

class UntilityPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task('unityExport', type: UnityExportTask)
    }
}
