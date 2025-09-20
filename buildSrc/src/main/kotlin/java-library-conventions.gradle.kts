plugins {
    id("test-conventions")
    `java-library`
}

dependencies {
    // https://jspecify.dev/
    implementation("org.jspecify:jspecify:1.0.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}