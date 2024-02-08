import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.compose}"
    const val runtimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.compose}"
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigation}"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.navCompose}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.dagger}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.dagger}"
    const val coil = "io.coil-kt:coil-compose:${Versions.coil}"
    const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"
    const val htmlUnitParser = "org.htmlunit:htmlunit-cssparser:${Versions.htmlUnitCssParser}"
    const val materialExtendedIcons = "androidx.compose.material:material-icons-extended:${Versions.materialExtendedIcons}"

}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gsonConverter)
}

fun DependencyHandler.compose() {
    implementation(Dependencies.viewmodelCompose)
    implementation(Dependencies.runtimeCompose)
    implementation(Dependencies.navigationCompose)
}

fun DependencyHandler.hilt() {
    implementation(Dependencies.hiltNavigation)
    implementation(Dependencies.hiltAndroid)
    kapt(Dependencies.hiltCompiler)
}

fun DependencyHandler.coil() {
    implementation(Dependencies.coil)
}

fun DependencyHandler.jsoup() {
    implementation(Dependencies.jsoup)
}

fun DependencyHandler.htmlUnitParser() {
    implementation(Dependencies.htmlUnitParser)
}

fun DependencyHandler.materialExtendedIcons() {
    implementation(Dependencies.materialExtendedIcons)
}