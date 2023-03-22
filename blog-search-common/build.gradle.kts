description = "blog-search-common"
dependencies {
    // spring
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-tx")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0") // jakarta.servlet.ServletException
    // docs
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4") {
        exclude("org.yaml", "snakeyaml")
    }
}
