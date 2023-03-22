package com.jj.blog.blogsearchapi.repository

import com.jj.blog.blogsearchrdb.dao.PopularKeywordHourDao
import com.jj.blog.blogsearchrdb.dao.QPopularKeywordHourDao
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.time.LocalDateTime

class PopularKeywordHourRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) :
    QuerydslRepositorySupport(PopularKeywordHourDao::class.java), PopularKeywordHourRepositoryCustom {
    @Value("\${bs.popular.keyword.count:10}")
    val popularKeywordCount: Long = 10

    private val qPopularKeywordHourDao: QPopularKeywordHourDao = QPopularKeywordHourDao.popularKeywordHourDao

    // startTime: 통계 데이터 기준시작시간(이상), endTime: 통계 데이터 기준종료시간(미만), targetTime: 조회시점-1시간
    // endTime 이 targetTime 보다 큰 경우 조회
    override fun findLatestPopularKeyList(targetTime: LocalDateTime): List<PopularKeywordHourDao> {
        return jpaQueryFactory
            .selectFrom(qPopularKeywordHourDao)
            .where(
                gtEndTime(targetTime),
            )
            .orderBy(qPopularKeywordHourDao.count.desc())
            .limit(popularKeywordCount)
            .fetch()
    }

    fun gtEndTime(targetTime: LocalDateTime): Predicate? {
        return qPopularKeywordHourDao.endTime.gt(targetTime)
    }
}