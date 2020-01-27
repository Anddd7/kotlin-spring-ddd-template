import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern

/** -------------- project's properties -------------- */

group = "com.github.anddd7"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()
}

buildscript {
  repositories {
    jcenter()
  }
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

/** -------------- import & apply plugins -------------- */

// import plugins into this project
plugins {
  val kotlinVersion = "1.3.61"

  java
  idea
  jacoco

  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion

  id("org.springframework.boot") version "2.2.2.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"

  id("org.flywaydb.flyway") version "6.1.4"

  id("io.gitlab.arturbosch.detekt") version "1.3.0"
}
/** -------------- configure imported plugin -------------- */

val sourceSets = the<SourceSetContainer>()

sourceSets {
  create("apiTest") {
    java.srcDir("src/apiTest/kotlin")
    resources.srcDir("src/apiTest/resources")
    compileClasspath += sourceSets["test"].compileClasspath
    runtimeClasspath += sourceSets["test"].runtimeClasspath
  }
}

idea {
  project {
    jdkName = "11"
  }
  module {
    outputDir = file("$buildDir/idea-compiler/main")
    testOutputDir = file("$buildDir/idea-compiler/test")
  }
}

flyway {
  url = "jdbc:postgresql://localhost:5432/local?user=test&password=test"
}

detekt {
  toolVersion = "1.1.1"
  input = files("src/main/kotlin")
}

jacoco {
  toolVersion = "0.8.3"
}

/** -------------- dependencies management -------------- */

dependencies {
  /* kotlin */
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))
  testImplementation("io.mockk:mockk:1.9.3")
  testImplementation("org.assertj:assertj-core:3.14.0")

  /* junit5 */
  testImplementation("org.junit.jupiter:junit-jupiter-engine")

  /* spring mvc */
  implementation("org.springframework.boot:spring-boot-starter-web")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "junit")
    exclude(group = "org.mockito")
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.+")
  testImplementation("com.ninja-squad:springmockk:1.1.3")

  /* security */
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("io.jsonwebtoken:jjwt:0.9.1")
  testImplementation("org.springframework.security:spring-security-test")

  /* monitoring x logging */
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  runtimeOnly("net.logstash.logback:logstash-logback-encoder:5.2")

  /* swagger */
  implementation("io.springfox:springfox-swagger2:2.9.2")
  runtimeOnly("io.springfox:springfox-swagger-ui:2.9.2")

  /* db */
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("com.vladmihalcea:hibernate-types-52:2.3.2")
  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.postgresql:postgresql")
  testImplementation("io.zonky.test:embedded-database-spring-test:1.5.2")

  /* utils */
  implementation("com.google.guava:guava:28.1-jre")
}

/** -------------- configure tasks -------------- */

tasks.register<Test>("apiTest") {
  description = "Runs the api tests."
  group = "verification"
  testClassesDirs = sourceSets["apiTest"].output.classesDirs
  classpath = sourceSets["apiTest"].runtimeClasspath
  mustRunAfter(tasks["test"])
}

tasks.withType<KotlinCompile>().all {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
  // include("**/special/package/**") // only analyze a sub package inside src/main/kotlin
  // exclude("**/special/package/internal/**") // but exclude our legacy internal package
}

task("newMigration") {
  group = "flyway"
  description = """
        ./gradlew newMigration -Ptype=<ddl,dml> -Poperation=<operation>. Please ensure you already have dir `db/migration`
        """.trim()

  doLast {
    val (operation, type) = properties["operation"] to properties["type"]
    val resourcesPath = sourceSets["main"].resources.sourceDirectories.singleFile.path
    val timestamp = now().format(ofPattern("yyyyMMddHHmm"))
    val filename = "V${timestamp}__${type}_$operation.sql"
    val filepath = "$resourcesPath/db/migration/$filename"
    File(filepath).takeIf { it.createNewFile() }?.appendText("-- script")
  }
}
