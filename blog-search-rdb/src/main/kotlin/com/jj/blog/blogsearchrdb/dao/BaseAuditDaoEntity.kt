package com.jj.blog.blogsearchrdb.dao

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditDaoEntity {
    @Column(name = "CREATED_AT", nullable = false)
    @CreatedDate
    var createdAt: LocalDateTime? = null

    @Column(name = "MODIFIED_AT", nullable = false)
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null
}
