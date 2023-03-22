package com.jj.blog.blogsearchapi.config

import org.springframework.boot.context.properties.ConfigurationProperties

/** app config */
@ConfigurationProperties("bs")
data class AppConfig(
    val clients: Map<String, ClientConfig>
) {

    data class ClientConfig(
        val baseUrl: String,
        val apiPath: String,
        val headers: MutableMap<String, String>,
    )
}