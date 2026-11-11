plugins {
    groovy
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.apache.groovy:groovy-bom:6.0.0"))
    testImplementation("org.apache.groovy:groovy")
    testImplementation(platform("org.spockframework:spock-bom:2.4-groovy-5.0"))
    testImplementation("org.spockframework:spock-core")
    testImplementation("org.apache.groovy:groovy-nio")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}