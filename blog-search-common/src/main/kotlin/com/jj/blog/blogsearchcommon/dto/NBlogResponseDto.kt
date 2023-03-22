package com.jj.blog.blogsearchcommon.dto

import java.time.LocalDateTime

class NBlogResponseDto {
    var lastBuildDate: String? = null
    var total: Long? = null
    var start: Int? = null
    var display: Int? = null
    var items: List<NBlogItems> = mutableListOf()

    class NBlogItems {
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var bloggername: String? = null
        var bloggerlink: String? = null
        var postdate: String? = null
    }
}
