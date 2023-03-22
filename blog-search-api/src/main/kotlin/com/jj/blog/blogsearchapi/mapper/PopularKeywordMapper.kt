package com.jj.blog.blogsearchapi.mapper

import com.jj.blog.blogsearchcommon.dto.PopularKeywordDto
import com.jj.blog.blogsearchrdb.dao.PopularKeywordHourDao
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PopularKeywordMapper : GenericMapper<PopularKeywordDto, PopularKeywordHourDao>
