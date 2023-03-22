package com.jj.blog.blogsearchapi.repository

import com.jj.blog.blogsearchrdb.dao.PopularKeywordHourDao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PopularKeywordHourRepository : JpaRepository<PopularKeywordHourDao, Long>,
    PopularKeywordHourRepositoryCustom