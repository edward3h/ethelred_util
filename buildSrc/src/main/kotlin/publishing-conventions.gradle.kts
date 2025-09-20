plugins {
    java
    id("io.freefair.javadocs")
    id("org.danilopianini.publish-on-central")

}

group = rootProject.group
version = rootProject.version

tasks.javadoc {
    if(JavaVersion.current().isJava9Compatible()) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishOnCentral {
    repoOwner = "edward3h"
    projectDescription = "Utility classes"
    projectLongName = "Ethelred Util"
    licenseName = "GNU General Public License, Version 3"
    licenseUrl = "https://choosealicense.com/licenses/gpl-3.0/"
    projectUrl = "https://github.com/edward3h/ethelred_util"
    scmConnection = "https://github.com/edward3h/ethelred_util.git"
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name = "Edward Harman"
                        email = "jaq@ethelred.org"
                    }
                }
            }
        }
    }
}

signing {
    val signingKey = findProperty("signingKey") as String?
    val signingPassword = findProperty("signingPassword") as String?
    useInMemoryPgpKeys(signingKey, signingPassword)
}
