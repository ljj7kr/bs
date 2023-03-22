package com.jj.blog.blogsearchrdb.dao

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "POPULAR_KEYWORD_HOUR"
)
class PopularKeywordHourDao : BaseAuditDaoEntity() {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_POPULAR_KEYWORD_HOUR_ID")
    @SequenceGenerator(
        name = "SQ_POPULAR_KEYWORD_HOUR_ID",
        sequenceName = "SQ_POPULAR_KEYWORD_HOUR_ID",
        allocationSize = 1,
        initialValue = 1
    )
    var id: Long? = null

    @Column(name = "KEYWORD", nullable = false, length = 100)
    var keyword: String? = null

    @Column(name = "COUNT", nullable = false)
    var count: Long? = null

    @Column(name = "START_TIME", nullable = false)
    var startTime: LocalDateTime? = null

    @Column(name = "END_TIME", nullable = false)
    var endTime: LocalDateTime? = null
}