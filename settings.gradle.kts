pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Tambahkan jcenter sebagai cadangan jika maven sedang bermasalah
        @Suppress("DEPRECATION")
        jcenter()
    }
}
rootProject.name = "FreshCycle"
include(":app")