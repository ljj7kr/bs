package com.jj.blog.blogsearchapi.repository

import com.jj.blog.blogsearchcommon.dto.PopularKeywordDto
import com.jj.blog.blogsearchrdb.dao.QSearchHistoryDao
import com.jj.blog.blogsearchrdb.dao.SearchHistoryDao
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.time.LocalDateTime

class SearchHistoryRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) :
    QuerydslRepositorySupport(SearchHistoryDao::class.java), SearchHistoryRepositoryCustom {
    @Value("\${bs.popular.keyword.count:10}")
    val popularKeywordCount: Long = 10

    private val qSearchHistoryDao: QSearchHistoryDao = QSearchHistoryDao.searchHistoryDao

    // 조회된 keyword 수가 많은 순서대로 n개 조회
    // 최근 24시간 동안의 조회수를 집계하여 1시간 단위로 업데이트
    override fun aggregatePopularKeyword(startTime: LocalDateTime, endTime: LocalDateTime, maxCount: Long)
            : List<PopularKeywordDto> {
        return jpaQueryFactory
            .select(
                Projections.fields(
                    PopularKeywordDto::class.java,
                    qSearchHistoryDao.keyword.`as`("keyword"),
                    qSearchHistoryDao.keyword.count().`as`("count")
                )
            )
            .from(qSearchHistoryDao)
            .where(
                goeCreatedAt(startTime),
                ltCreatedAt(endTime),
            )
            .groupBy(qSearchHistoryDao.keyword)
            .orderBy(qSearchHistoryDao.keyword.count().desc())
            .limit(maxCount)
            .fetch()
    }

    fun goeCreatedAt(startTime: LocalDateTime): Predicate? {
        return qSearchHistoryDao.createdAt.goe(startTime)
    }

    fun ltCreatedAt(endTime: LocalDateTime): Predicate? {
        return qSearchHistoryDao.createdAt.lt(endTime)
    }
}