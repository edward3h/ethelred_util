import org.ethelred.util.task.EdhlFacadeGenerator

plugins {
    id("java-library-conventions")
    id("publishing-conventions")
}

val generateFacade by tasks.registering(EdhlFacadeGenerator::class) {
    tags = layout.projectDirectory.file("src/main/resources/tags")
    template = layout.projectDirectory.file("src/template/java/org/ethelred/util/edhl/__template__.java")
    outputDirectory = layout.buildDirectory.dir("generated/")
}

sourceSets {
    main {
        java {
            srcDir(generateFacade)
        }
    }
    create("template") {
        compileClasspath += sourceSets.main.get().output
    }
}

// TODO haven't figured out why this is happening
tasks.named("sourcesJar") {
    dependsOn(tasks.named("compileJava"))
    dependsOn(tasks.named("compileTestJava"))
    dependsOn(tasks.named("compileGroovy"))
    dependsOn(tasks.named("compileTestGroovy"))
}

tasks.named("javadoc") {
    dependsOn(tasks.named("compileTestGroovy"))
}