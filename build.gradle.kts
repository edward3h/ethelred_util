import org.ethelred.util.task.MarkdownTask

plugins {
	id("com.github.jakemarsden.git-hooks") version "0.0.2"
	id("com.diffplug.spotless") version "7.2.1"
	id("io.freefair.aggregate-javadoc")
}

group = "org.ethelred.util"
version = "2.4-rc0"

repositories {
	mavenCentral()
}

gitHooks {
	hooks = mapOf("pre-commit" to "build")
}

dependencies {
	javadoc(project(":common"))
	javadoc(project(":picocli"))
}

spotless {
	java {
		target("**/src/**/*.java")
		palantirJavaFormat()
		formatAnnotations()
		importOrder()
		licenseHeader("/* (C) \$YEAR */")
	}

	format("misc") {
		target(".gitattributes", ".gitignore")

		trimTrailingWhitespace()
		leadingTabsToSpaces()
		endWithNewline()
	}

	groovyGradle {
		target("*.gradle")
		greclipse()
	}
}

var markdown = tasks.register<MarkdownTask>("markdown") {
	from = file("docs")
}

tasks.javadoc {
	inputs.dir(markdown.map {it.into})
	options {
		overview = markdown.get().into.file("docs/overview.html").get().getAsFile().toString()
	}
}