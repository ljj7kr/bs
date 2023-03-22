package com.jj.blog.blogsearchapi.repository

import com.jj.blog.blogsearchrdb.dao.PopularKeywordHourDao
import java.time.LocalDateTime

interface PopularKeywordHourRepositoryCustom {
    fun findLatestPopularKeyList(targetTime: LocalDateTime): List<PopularKeywordHourDao>
}