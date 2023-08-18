import com.graphql_java_generator.plugin.conf.CustomScalarDefinition
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("com.graphql-java-generator.graphql-gradle-plugin3") version "2.2"
}

group = "sall.good"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.graphql:spring-graphql-test")
	implementation("com.graphql-java-generator:graphql-java-server-runtime:2.2")
	implementation("com.graphql-java-generator:graphql-java-client-runtime:2.2")
	implementation("com.graphql-java-generator:graphql-java-common-runtime:2.2")
	implementation("com.graphql-java:graphql-java-extended-scalars:20.2")
	runtimeOnly("org.postgresql:r2dbc-postgresql:1.0.2.RELEASE")
	runtimeOnly("org.postgresql:postgresql:42.6.0")
	runtimeOnly("org.liquibase:liquibase-core:4.23.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

generatePojoConf {
	setSchemaFileFolder("src/main/resources/graphql")
	setCustomScalars(arrayOf(
		CustomScalarDefinition(
			"JSON",
			"java.util.Map",
			null,
			"graphql.scalars.ExtendedScalars.Json",
			null,
		),
		CustomScalarDefinition(
			"Date",
			"java.time.LocalDate",
			null,
			"graphql.scalars.ExtendedScalars.Date",
			null,
		)
	))
}


sourceSets {
	main {
		java {
			srcDirs("$buildDir/generated/sources/graphqlGradlePlugin")
			srcDirs("$buildDir/generated/resources/graphqlGradlePlugin")
		}
	}
}

tasks.compileKotlin {
	dependsOn("generatePojo")
}

tasks.compileJava {
	dependsOn("generatePojo")
}
