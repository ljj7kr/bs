import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	idea // intellij file open
	java
	application // tar/zip, java, distribution
	id("org.springframework.boot") version "3.0.4"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
	kotlin("kapt") version "1.7.22"
}

java.sourceCompatibility = JavaVersion.VERSION_17
allprojects {
	group = "com.jj.blog"
	version = "1.0.0"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "idea")
	apply(plugin = "java")
	apply(plugin = "application")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "kotlin-spring") // allopen
	apply(plugin = "kotlin-jpa") // noarg
	apply(plugin = "kotlin-kapt")

	dependencies {
		// kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		// spring
		implementation("org.springframework.boot:spring-boot-configuration-processor")
		kapt("org.springframework.boot:spring-boot-configuration-processor")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.projectreactor:reactor-test")
		// mapper
		implementation("org.mapstruct:mapstruct:1.5.3.Final")
		kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
		// h2
		runtimeOnly("com.h2database:h2")
	}

	tasks.jar {
		enabled = true
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

// api 가 common, rdb, client 사용
project(":blog-search-api") {
	dependencies {
		implementation(project(":blog-search-common"))
		implementation(project(":blog-search-rdb"))
		implementation(project(":blog-search-client"))
	}
}

// client 가 common 사용
project(":blog-search-client") {
	dependencies {
		implementation(project(":blog-search-common"))
	}
}

tasks.jar {
	enabled = false
}
tasks.bootJar {
	enabled = false
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}