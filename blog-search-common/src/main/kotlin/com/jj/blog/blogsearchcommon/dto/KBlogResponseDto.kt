package com.jj.blog.blogsearchcommon.dto

import java.time.LocalDateTime

class KBlogResponseDto {
    var meta: KBlogMeta? = null
    var documents: List<KBlogDocuments> = mutableListOf()

    class KBlogMeta {
        var total_count: Int? = null
        var pageable_count: Int? = null
        var is_end: Boolean? = null
    }

    class KBlogDocuments {
        var title: String? = null
        var contents: String? = null
        var url: String? = null
        var blogname: String? = null
        var thumbnail: String? = null
        var datetime: String? = null
    }
}
