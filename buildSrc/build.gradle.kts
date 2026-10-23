plugins {
    `kotlin-dsl`
    id("java")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
    implementation("org.danilopianini:publish-on-central:9.2.15")
    implementation("io.freefair.gradle:maven-plugin:9.8.2")
}