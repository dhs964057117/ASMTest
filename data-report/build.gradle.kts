plugins {
    id("java-library")
//    alias(libs.plugins.jetbrainsKotlinJvm)
    id("kotlin")
    `maven-publish`
    id("java-gradle-plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    implementation(libs.asm)
    implementation(libs.asm.commons)
    implementation(libs.asm.analysis)
    implementation(libs.asm.util)
    implementation(libs.asm.tree)
    implementation(libs.gradle) {
        exclude(group = "org.ow2.asm", module = "asm")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.haosen.plugin.trace"
            artifactId = "datareport"
            version = "1.0.0"

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri(rootProject.file("repo"))
        }
    }
}

gradlePlugin {
    plugins {
        create("TracePlugin") {
            id = "com.haosen.plugin.trace" //这里是插件的ID
            implementationClass = "com.haosen.plugin.datareport.TracePlugin" //这里是包名+类名
        }
    }
}