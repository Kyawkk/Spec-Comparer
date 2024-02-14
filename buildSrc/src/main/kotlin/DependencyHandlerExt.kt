import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.implementation(dependency: String) {
    add("implementation",dependency)
}

fun DependencyHandler.test(dependency: String){
    add("test",dependency)
}

fun DependencyHandler.androidTest(dependency: String) {
    add("implementation",dependency)
}

fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation",dependency)
}

fun DependencyHandler.kapt(dependency: String) {
    add("kapt",dependency)
}

fun DependencyHandler.moduleImplementation(dependency: String) {
    add("implementation", project(dependency))
}