package com.jj.blog.blogsearchcommon.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageQueryDto {
    @Schema(description = "페이지 번호", type = "int", example = "0")
    var pageNo = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }

    @Schema(description = "페이지 사이즈", type = "int", example = "20")
    var pageSize = DEFAULT_PAGE_SIZE
        set(value) {
            field = if (value > MAX_PAGE_SIZE) DEFAULT_PAGE_SIZE else value
        }

    @Schema(description = "정렬 필드", type = "string", example = DEFAULT_SORT_FIELD)
    var sortField = DEFAULT_SORT_FIELD

    @Schema(description = "정렬 방향", type = "string", example = DEFAULT_SORT_DIRECTION_STR)
    var sortDirection: Sort.Direction = DEFAULT_SORT_DIRECTION

    fun of(): PageRequest {
        return PageRequest.of(pageNo, pageSize, sortDirection, sortField)
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_PAGE_SIZE = 100
        private const val DEFAULT_SORT_FIELD = "accuracy"
        private const val DEFAULT_SORT_DIRECTION_STR = "DESC"
        private val DEFAULT_SORT_DIRECTION: Sort.Direction = Sort.Direction.fromString(DEFAULT_SORT_DIRECTION_STR)
    }
}
