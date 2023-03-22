package com.jj.blog.blogsearchapi.controller

import com.jj.blog.blogsearchapi.service.BlogService
import com.jj.blog.blogsearchcommon.dto.BaseBlogDto
import com.jj.blog.blogsearchcommon.dto.PageQueryDto
import com.jj.blog.blogsearchcommon.dto.PopularKeywordDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/blog")
@Tag(name = "", description = "블로그 조회")
class BlogController(
    private val blogService: BlogService
) {

    @Operation(summary = "블로그 검색")
    @GetMapping("")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(schema = Schema(implementation = Page::class))]
        )]
    )
    @Parameters(
        Parameter(name = "keyword", description = "키워드 이름"),
    )
    fun retrieveBlog(
        @RequestParam(value = "keyword", required = true) keyword: String,
        @Parameter(
            schema = Schema(implementation = PageQueryDto::class),
            `in` = ParameterIn.QUERY
        ) pageQuery: PageQueryDto
    ): ResponseEntity<Page<out BaseBlogDto>> {
        val blogDtoPage = blogService.readBlog(keyword, pageQuery)
        return ResponseEntity.ok(blogDtoPage)
    }

    @Operation(summary = "인기 검색어 목록 조회")
    @GetMapping("/popular/keyword")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "OK")]
    )
    fun retrievePopularKeyword(): ResponseEntity<List<PopularKeywordDto>> {
        val popularDtoPage = blogService.readPopularKeyword()
        return ResponseEntity.ok(popularDtoPage)
    }
}