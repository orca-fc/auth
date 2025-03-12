plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.orca"
version = "0.0.1-SNAPSHOT"
extra["springCloudVersion"] = "2024.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.jsonwebtoken:jjwt:0.12.3")

	runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64")

	testImplementation("io.projectreactor:reactor-test:3.7.3")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:6.0.0.M1")
	testImplementation("io.mockk:mockk:1.13.17")
	runtimeOnly("io.kotest:kotest-assertions-core:6.0.0.M1")
	testImplementation("org.junit.platform:junit-platform-launcher:1.12.0-M1")
	testImplementation("org.springframework:spring-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
