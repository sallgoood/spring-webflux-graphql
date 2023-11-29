import io.github.kobylynskyi.graphql.codegen.gradle.GraphQLCodegenGradleTask
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("org.jetbrains.kotlin.kapt") version "1.9.0"
	id("io.github.kobylynskyi.graphql.codegen") version "5.8.0"
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
	implementation("org.springframework.boot:spring-boot-starter-validation")
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
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.liquibase:liquibase-core:4.23.1")
	implementation("com.infobip:infobip-spring-data-r2dbc-querydsl-boot-starter:9.0.2")
	kapt("com.infobip:infobip-spring-data-jdbc-annotation-processor:9.0.2")
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

tasks.named<GraphQLCodegenGradleTask>("graphqlCodegen") {
	graphqlSchemas {
		rootDir = "$projectDir/src/main/resources/graphql"
	}
	outputDir = File("$buildDir/generated")
	packageName = "com.generated.graphql"
	generateParameterizedFieldsResolvers = false
	modelValidationAnnotation = "@jakarta.validation.constraints.NotNull"
	generatedAnnotation = "jakarta.annotation.Generated"
	customAnnotationsMapping = mutableMapOf(Pair("EpochMillis", listOf("@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.example.json.EpochMillisScalarDeserializer.class)")))
	customTypesMapping = mutableMapOf(
		Pair("JSON", "java.util.Map"),
		Pair("Date", "java.time.LocalDate"),
		Pair("DateTime", "java.time.OffsetDateTime"),
		Pair("Url", "java.net.URL"),
		Pair("Long", "java.lang.Long"),
	)
	generateEqualsAndHashCode = true
}


sourceSets {
	main {
		java {
			srcDirs("$buildDir/generated")
		}
	}
}

tasks {
	compileJava {
		dependsOn("graphqlCodegen")
	}

	compileKotlin {
		dependsOn("graphqlCodegen")
	}

	withType(KaptGenerateStubsTask::class.java) {
		dependsOn("graphqlCodegen")
	}

	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "17"
		}
	}

	withType<Test> {
		useJUnitPlatform()
	}
}
