pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Spec Comparer"
include(":app")
include(":core")
include(":core:network")
include(":feature")
include(":core:domain")
include(":feature:home")
include(":feature:search")
include(":feature:details")
include(":feature:compare")
include(":core:data")
include(":core:model")
include(":core:design-system")
include(":core:components")
