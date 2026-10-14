plugins {
	id("io.freefair.settings.plugin-versions").version("9.8.2")
	id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}
rootProject.name = "ethelred_util"
include("common", "picocli", "edhl")
