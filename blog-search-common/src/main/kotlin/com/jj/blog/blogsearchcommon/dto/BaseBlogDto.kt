package com.jj.blog.blogsearchcommon.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
open class BaseBlogDto {
    var title: String? = null
    var contents: String? = null
    var url: String? = null
    var blogname: String? = null
    var thumbnail: String? = null
    var datetime: String? = null
}