plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:flow"))
        }
    }
}
