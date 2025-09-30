plugins {
    id("java-library-conventions")
//    id "publishing-conventions" TODO not ready to publish, but other modules are
}

dependencies {
    implementation(project(":common"))
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    api("info.picocli:picocli:4.7.7")
}
