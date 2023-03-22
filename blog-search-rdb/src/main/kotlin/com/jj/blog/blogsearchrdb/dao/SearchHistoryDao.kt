package com.jj.blog.blogsearchrdb.dao

import jakarta.persistence.*

@Entity
@Table(
    name = "SEARCH_HISTORY"
)
class SearchHistoryDao(keyword: String, server: String) : BaseAuditDaoEntity() {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SEARCH_HISTORY_ID")
    @SequenceGenerator(
        name = "SQ_SEARCH_HISTORY_ID",
        sequenceName = "SQ_SEARCH_HISTORY_ID",
        allocationSize = 1,
        initialValue = 1
    )
    var id: Long? = null

    @Column(name = "KEYWORD", nullable = false, length = 100)
    var keyword: String? = keyword

    @Column(name = "SERVER", nullable = false, length = 100)
    var server: String? = server

    override fun toString(): String {
        return "SearchHistoryDao(id=$id, keyword=$keyword, server=$server)"
    }
}