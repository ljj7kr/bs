description = "blog-search-api"
dependencies {
    // spring
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // docs
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4") {
        exclude("org.yaml", "snakeyaml")
    }
    // gson
    implementation("com.google.code.gson:gson:2.10.1")
    // querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    // mock server
    testImplementation("com.github.tomakehurst:wiremock-standalone:2.27.2")
    implementation("org.slf4j:slf4j-api")
}
