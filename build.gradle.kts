plugins {
    id("java")
    id("application")
    id("io.freefair.lombok") version "6.5.1"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.inditex.icdmsuscon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("com.opencsv:opencsv:5.5.2")
    implementation("org.openjfx:javafx-controls:17.0.2")
}

configurations {
    named("compileOnly") {
        extendsFrom(configurations.named("annotationProcessor").get())
    }
}

application {
    mainClass.set("com.inditex.icdmsuscon.Main")
}

javafx {
    version = "17.0.2"
    modules("javafx.controls")
}

tasks.withType<JavaExec> {
    // Ensure JavaFX dependencies are added to the runtime classpath
    jvmArgs = listOf(
        "--module-path", "${System.getProperty("java.home")}/lib",
        "--add-modules", "javafx.controls"
    )
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("application-${project.name}")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("Main-Class" to application.mainClass.get()))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.processResources {
    filesMatching("logback.xml") {
        expand("projectName" to project.name, "projectVersion" to project.version)
    }
}
