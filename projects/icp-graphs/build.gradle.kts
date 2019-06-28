import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.40"
}

group = "icp-graphs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("org.knowm.xchart:xchart:3.5.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}