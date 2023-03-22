package com.jj.blog.blogsearchapi

import com.jj.blog.blogsearchapi.config.AppConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@EnableConfigurationProperties(AppConfig::class)
@SpringBootApplication
class BlogSearchApiApplication

fun main(args: Array<String>) {
    runApplication<BlogSearchApiApplication>(*args)
}
