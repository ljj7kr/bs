package com.jj.blog.blogsearchapi.repository

import com.jj.blog.blogsearchrdb.dao.SearchHistoryDao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SearchHistoryRepository : JpaRepository<SearchHistoryDao, Long>, SearchHistoryRepositoryCustom