package com.jj.blog.blogsearchapi.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.jj.blog.blogsearchapi.repository"])
@EntityScan("com.jj.blog.blogsearchrdb.dao")
@Configuration
class JpaConfig {
    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Bean
    fun jpaQuryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}