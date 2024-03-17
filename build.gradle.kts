plugins {
    kotlin("jvm") version "1.8.20"
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.byrt.meltdown"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.reflections:reflections:0.10.2")
    implementation("cloud.commandframework:cloud-paper:1.8.4")
    implementation("cloud.commandframework:cloud-annotations:1.8.4")
    implementation("io.github.skytasul:glowingentities:1.3.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
