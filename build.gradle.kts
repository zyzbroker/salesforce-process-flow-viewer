//import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    //used for generate fat jar (excluding JRE, ie executable jar)
    id("com.github.johnrengelman.shadow") version "5.2.0"

    // Apply the java plugin to add support for Java
    id("java-library")

    // Apply the application plugin to add support for building a CLI application.
    id("application")
}

repositories {
    jcenter()
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-inject-java:1.3.0.RC1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:28.1-jre")
    implementation("io.micronaut:micronaut-inject:1.3.0.RC1")
    implementation("io.micronaut:micronaut-runtime:1.3.0.RC1")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0-RC8")
    //xml parser
    implementation("dom4j:dom4j:1.6.1")

    //markdown parser
    implementation("com.vladsch.flexmark:flexmark-all:0.50.44")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testAnnotationProcessor("io.micronaut:micronaut-inject-java:1.3.0.RC1")
    testImplementation("io.micronaut.test:micronaut-test-junit5:1.1.2")
    //testImplementation("org.mockito:mockito-junit-jupiter:2.22.0")
}

application {
    // Define the main class for the application.
    mainClassName = "micronaut.starter.kit.App"
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}

//generate uber/fat jar: comment in when in need
//tasks.withType<ShadowJar> {
//    minimize()
//    mergeServiceFiles()
//}