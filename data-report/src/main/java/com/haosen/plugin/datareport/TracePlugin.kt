package com.haosen.plugin.datareport

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.example.miaow.plugin.asm.ScanClassVisitorFactory
import com.example.miaow.plugin.asm.TimeClassVisitorFactory
import com.example.miaow.plugin.asm.TraceClassVisitorFactory
import com.example.miaow.plugin.bean.ScanBean
import com.example.miaow.plugin.bean.TimeBean
import com.example.miaow.plugin.bean.TraceBean
import com.haosen.plugin.datareport.bean.TraceConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.com.google.gson.Gson
import org.objectweb.asm.Opcodes

class  TracePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        project.extensions.create("tracePointConfig",TraceConfig::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                ScanClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                it.ignoreOwners.set(
                    listOf("com/example/fragment/project/utils/SSIDUtils")
                )
                it.listOfScans.set(
                    listOf(
                        ScanBean(
                            "android/net/wifi/WifiInfo",
                            "getSSID",
                            "()Ljava/lang/String;",
                            Opcodes.INVOKESTATIC,
                            "com/example/fragment/project/utils/SSIDUtils",
                            "getSSID",
                            "()Ljava/lang/String;"
                        ),
                        ScanBean(
                            "android/net/NetworkInfo",
                            "getExtraInfo",
                            "()Ljava/lang/String;",
                            Opcodes.INVOKESTATIC,
                            "com/example/fragment/project/utils/SSIDUtils",
                            "getSSID",
                            "()Ljava/lang/String;"
                        )
                    )
                )
            }
            variant.instrumentation.transformClassesWith(
                TimeClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                it.listOfTimes.set(
                    listOf(
                        TimeBean(
                            "com/haosen/asmtest/MainActivity",
                            "onCreate",
                            "(Landroid/os/Bundle;)V"
                        ),
                        TimeBean(
                            owner = "com/example/fragment/library/base",
                            time = 50L
                        )
                    )
                )
            }
            val extension = project.properties["tracePointConfig"] as TraceConfig
            for (i in extension.configs) {
                variant.instrumentation.transformClassesWith(
                    TraceClassVisitorFactory::class.java,
                    InstrumentationScope.ALL
                ) {
                    it.packageName.addAll(i.packages)
                    it.listOfTraces.set(
                        listOf(
                            TraceBean(
                                traceOwner = i.methods.traceOwner,
                                traceName = i.methods.traceName,
                                traceDesc = i.methods.traceDesc, //参数应在desc范围之内
                                owner = i.methods.owner,
                                name = i.methods.name,
                                desc = i.methods.desc
                            )
                        )
                    )
                }
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }

}