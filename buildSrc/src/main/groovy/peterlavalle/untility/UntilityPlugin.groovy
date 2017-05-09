package peterlavalle.untility

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION

class UntilityPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        def unity = project.extensions.create('unity', Config)

        unity.owner = project

        def untiMakeSpace =
                project.task(
                        'untiMakeSpace',
                        type: UntiMakeSpaceTask
                )

        def untiImport =
                project.task(
                        'untiImport',
                        type: UntiImportTask,
                        dependsOn: untiMakeSpace
                )

        def untiAssemblePlugin =
                project.task(
                        'untiAssemblePlugin',
                        type: UntiAssemblePluginTask,
                        dependsOn: untiImport
                )

        def untiAssembleEditor =
                project.task(
                        'untiAssembleEditor',
                        type: UntiAssembleEditorTask,
                        dependsOn: untiAssemblePlugin
                )

        def untiAssembleAssets =
                project.task(
                        'untiAssembleAssets',
                        type: UntiAssembleAssetsTask,
                        dependsOn: untiMakeSpace
                )

        def untiPackage =
                project.task(
                        'untiPackage',
                        type: UntiPackageTask,
                        dependsOn: [
                                untiAssembleAssets,
                                untiAssembleEditor,
                                untiAssemblePlugin,
                        ]
                )
        project.configure(project) {

            apply plugin: 'base'

            artifacts {
                unityPackage {

                }
            }
/*
            apply plugin: 'scala'
            apply plugin: 'maven-publish'

            artifacts {
                archives(untiPackage.outputs) {
                    name 'unityPackage'
                    type 'unityPackage'
                    builtBy untiPackage
                }
            }
          */
        }
    }
}
