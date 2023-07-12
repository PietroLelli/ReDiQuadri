plugins {
    id("java")
    id("application")
}

group = "org.example"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("io.javalin:javalin:5.1.3")
    implementation("com.google.code.gson:gson:2.10")
    implementation("io.javalin.community.openapi:javalin-openapi-plugin:5.1.3") // for /openapi route with JSON scheme
    implementation("io.javalin.community.openapi:javalin-swagger-plugin:5.1.3") // for Swagger UI

    annotationProcessor("io.javalin.community.openapi:openapi-annotation-processor:5.1.3")

    implementation("org.slf4j:slf4j-simple:2.0.3")

    implementation(project(":rq-common"))
    implementation(project(":rq-presentation"))
}

application{
    mainClass.set("rq.ReDiQuadriService")
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}