description = "blog-search-rdb"
// 특정 어노테이션에 대해 allOpen 수행
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

// 특정 어노테이션에 대해 noArg 생성자 추가
noArg {
    annotation("jakarta.persistence.Entity")
}
dependencies {
     // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
}
