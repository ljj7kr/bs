package com.jj.blog.blogsearchapi.mapper

import com.jj.blog.blogsearchcommon.dto.BaseBlogDto
import com.jj.blog.blogsearchcommon.dto.KBlogResponseDto
import com.jj.blog.blogsearchcommon.dto.NBlogDto
import com.jj.blog.blogsearchcommon.dto.NBlogResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BaseBlogMapper {

    fun toBaseBlog(org: KBlogResponseDto.KBlogDocuments): BaseBlogDto

    @Mapping(source = "description", target = "contents")
    @Mapping(source = "link", target = "url")
    @Mapping(source = "bloggername", target = "blogname")
    @Mapping(source = "postdate", target = "datetime")
    fun toNBlog(org: NBlogResponseDto.NBlogItems): NBlogDto
}
