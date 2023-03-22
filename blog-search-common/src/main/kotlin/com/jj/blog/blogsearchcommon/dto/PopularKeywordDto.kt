package com.jj.blog.blogsearchcommon.dto

data class PopularKeywordDto(
    var keyword: String?,
    var count: Long?,
) {
    constructor() : this(null, null)
}