package com.jj.blog.blogsearchcommon.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageResponseDto<T> {
    var content: MutableList<T> = mutableListOf()
    var pageable: PageRequest? = null
    var sort: Sort? = null
    var first: Boolean? = null
    var last: Boolean? = null
    var empty: Boolean? = null
    var totalPages: Int? = null
    var totalElements: Int? = null
    var size: Int? = null
    var number: Int? = null
    var numberOfElements: Int? = null
}