plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "io.github.lfdevs.vm"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.lfdevs.vm"
        minSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

// 添加编译器选项以允许访问隐藏API
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjvm-default=all",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.commons.compress)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.okhttp)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Protobuf 和 gRPC 的运行时库
    implementation(libs.protobuf.javalite)
    implementation(libs.grpc.protobuf.lite)
    // gRPC 生成的代码可能需要这个注解库
    compileOnly(libs.javax.annotation.api)

    compileOnly(files("libs/framework.jar"))
    compileOnly(files("libs/framework-virtualization.jar"))
    compileOnly(files("libs/framework-annotations-lib.jar"))
    compileOnly(files("libs/android_system_server_stubs_current.jar"))
    compileOnly(files("libs/VmTerminalApp.jar"))
}

// protobuf 插件的配置
protobuf {
    protoc {
        // 指定 protoc 编译器的版本
        artifact = "com.google.protobuf:protoc:21.0-rc-1"
    }
    plugins {
        // 配置 gRPC 代码生成插件
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.75.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc") {
                    option("lite")
                }
            }
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
