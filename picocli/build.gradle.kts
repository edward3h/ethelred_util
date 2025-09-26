plugins {
    id("java-library-conventions")
//    id "publishing-conventions" TODO not ready to publish, but other modules are
}

dependencies {
    implementation(project(":common"))

    api("info.picocli:picocli:4.7.7")
}
