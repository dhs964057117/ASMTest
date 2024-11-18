import com.haosen.plugin.datareport.bean.Config
import com.haosen.plugin.datareport.bean.TraceMethod

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    id("com.haosen.plugin.trace")
}

android {
    namespace = "com.haosen.asmtest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.haosen.asmtest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    tracePointConfig {
        configs = listOf(
            Config(
                packages = listOf("com.haosen.asmtest"),
                methods = TraceMethod(
                    traceOwner = "com/haosen/asmtest/utils/DataReportHelper",
                    traceName = "viewOnClick",
                    traceDesc = "(Landroid/view/View;)V",
                    owner = "Landroid/view/View\$OnClickListener;",
                    name = "onClick",
                    desc = "(Landroid/view/View;)V",
                    onMethod = 0,
                )
            ), Config(
                packages = listOf("com.haosen.test"),
                methods = TraceMethod(
                    traceOwner = "com/haosen/asmtest/utils/DataReportHelper",
                    traceName = "viewOnClick",
                    traceDesc = "(Landroid/view/View;)V",
                    owner = "Landroid/view/View\$OnClickListener;",
                    name = "onClick",
                    desc = "(Landroid/view/View;)V",
                    onMethod = 0,
                )
            ),
            Config(
                packages = listOf("com.haosen.asmtest"),
                methods = TraceMethod(
                    traceOwner = "com/haosen/asmtest/utils/DataReportHelper",
                    traceName = "onCheckedChanged",
                    traceDesc = "(Landroid/widget/CompoundButton;)V",
                    owner = "Landroid/widget/CompoundButton\$OnCheckedChangeListener;",
                    name = "onCheckedChanged",
                    desc = "(Landroid/widget/CompoundButton;Z)V",
                    onMethod = 0,
                )
            )
        )
    }
}

dependencies {
    implementation(project(":library-base"))
    implementation(project(":floating"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    //compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.leakcanary.android)
    // Java 8支持的 DefaultLifecycleObserver
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.1")
    // ProcessLifecycleOwner给整个app进程提供一个lifecycle
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
}