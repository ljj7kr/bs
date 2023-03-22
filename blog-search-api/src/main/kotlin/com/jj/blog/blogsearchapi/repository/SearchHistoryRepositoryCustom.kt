package com.jj.blog.blogsearchapi.repository

import com.jj.blog.blogsearchcommon.dto.PopularKeywordDto
import java.time.LocalDateTime

interface SearchHistoryRepositoryCustom {
    fun aggregatePopularKeyword(startTime: LocalDateTime, endTime: LocalDateTime, maxCount: Long)
            : List<PopularKeywordDto>
}