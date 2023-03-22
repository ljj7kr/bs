package com.jj.blog.blogsearchapi.client

import com.jj.blog.blogsearchapi.mapper.BaseBlogMapper
import com.jj.blog.blogsearchclient.client.BlogWebClient
import com.jj.blog.blogsearchcommon.dto.BaseBlogDto
import com.jj.blog.blogsearchcommon.dto.KBlogResponseDto
import com.jj.blog.blogsearchcommon.dto.NBlogResponseDto
import com.jj.blog.blogsearchcommon.dto.PageQueryDto
import com.jj.blog.blogsearchcommon.exception.RestfulErrorCode
import com.jj.blog.blogsearchcommon.exception.ServiceException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.net.URI

@Component
class RemoteClients(
    private val blogWebClient: BlogWebClient,
    private val baseBlogMapper: BaseBlogMapper,
) {
    fun client(
        serverName: String,
        baseUrl: String,
        apiPathTemplate: String,
        headers: MutableMap<String, String>,
        keyword: String,
        pageQueryDto: PageQueryDto
    ): List<BaseBlogDto> {

        return when (serverName) {
            "kakao" -> kakaoClient(baseUrl, apiPathTemplate, headers, keyword, pageQueryDto)
            "naver" -> naverClient(baseUrl, apiPathTemplate, headers, keyword, pageQueryDto)
            else -> throw ServiceException(RestfulErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun kakaoClient(
        baseUrl: String,
        apiPathTemplate: String,
        headers: MutableMap<String, String>,
        keyword: String,
        pageQueryDto: PageQueryDto
    ): List<BaseBlogDto> {

        val apiPath =
            String.format(
                apiPathTemplate, pageQueryDto.sortField, pageQueryDto.pageNo + 1, pageQueryDto.pageSize, keyword
            )
        headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
        headers[HttpHeaders.ACCEPT] = MediaType.ALL_VALUE

        val blogDto = blogWebClient.getBlock(
            KBlogResponseDto::class.java,
            URI.create(baseUrl).toString(),
            URI.create(apiPath).toString(),
            headers,
        ) ?: KBlogResponseDto()

        return blogDto.documents.map { dto ->
            baseBlogMapper.toBaseBlog(dto)
        }
    }

    fun naverClient(
        baseUrl: String,
        apiPathTemplate: String,
        headers: MutableMap<String, String>,
        keyword: String,
        pageQueryDto: PageQueryDto
    ): List<BaseBlogDto> {

        val sortField = when (pageQueryDto.sortField) {
            "accuracy" -> "sim" // 정확도순
            "recency" -> "date" // 최신순
            else -> "sim"
        }

        val apiPath =
            String.format(
                apiPathTemplate, keyword, pageQueryDto.pageSize, pageQueryDto.pageNo + 1, sortField
            )
        headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
        headers[HttpHeaders.ACCEPT] = MediaType.ALL_VALUE

        val blogDto: NBlogResponseDto = blogWebClient.getBlock(
            NBlogResponseDto::class.java,
            URI.create(baseUrl).toString(),
            URI.create(apiPath).toString(),
            headers
        ) ?: NBlogResponseDto()

        return blogDto.items.map { dto ->
            baseBlogMapper.toNBlog(dto)
        }
    }
}