import org.gradle.kotlin.dsl.implementation

plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
}
val springAiVersion by extra("2.0.0-M3")

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "demo1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }}

dependencies {
    //basic
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    //lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    //devTools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    //h2
    runtimeOnly("com.h2database:h2")

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

    //gemini
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai")

    //claude
    implementation("org.springframework.ai:spring-ai-starter-model-anthropic")

    //groq (openai-compatible)
    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    //jasypt (yml 암호화)
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    //spring security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    //jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    //redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// .env 파일을 bootRun 환경변수로 자동 로드
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    val envFile = file(".env")
    if (envFile.exists()) {
        envFile.readLines()
            .filter { it.isNotBlank() && !it.startsWith("#") && it.contains("=") }
            .forEach { line ->
                val (key, value) = line.split("=", limit = 2)
                environment(key.trim(), value.trim())
            }
    }
}
