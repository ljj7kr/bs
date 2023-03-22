package com.jj.blog.blogsearchapi

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jj.blog.blogsearchapi.repository.SearchHistoryRepository
import com.jj.blog.blogsearchcommon.dto.BaseBlogDto
import com.jj.blog.blogsearchcommon.dto.PageResponseDto
import com.jj.blog.blogsearchcommon.dto.PopularKeywordDto
import com.jj.blog.blogsearchrdb.dao.SearchHistoryDao
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@DisplayName("블로그 조회 시스템 테스트")
@TestMethodOrder(MethodOrderer.DisplayName::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
class BlogSearchTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var searchHistoryRepository: SearchHistoryRepository

    // 11 개의 키워드
    var numberOfBts = 100
    var numberOfA = 90
    var numberOfB = 80
    var numberOfC = 70
    var numberOfD = 60
    var numberOfE = 50
    var numberOfF = 40
    var numberOfG = 30
    var numberOfH = 20
    var numberOfIU = 100
    var numberOfJ = 5

    @BeforeAll
    @DisplayName("검색 기록 생성")
    fun setup() {
        // 검색 기록 생성
        for (i in 1..numberOfBts) {
            val searchHistoryDao = SearchHistoryDao("BTS", "kakao")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfA) {
            val searchHistoryDao = SearchHistoryDao("A", "kakao")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfB) {
            val searchHistoryDao = SearchHistoryDao("B", "kakao")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfC) {
            val searchHistoryDao = SearchHistoryDao("C", "kakao")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfD) {
            val searchHistoryDao = SearchHistoryDao("D", "kakao")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfE) {
            val searchHistoryDao = SearchHistoryDao("E", "kakao")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfF) {
            val searchHistoryDao = SearchHistoryDao("F", "naver")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfG) {
            val searchHistoryDao = SearchHistoryDao("G", "naver")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfH) {
            val searchHistoryDao = SearchHistoryDao("H", "naver")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfIU) {
            val searchHistoryDao = SearchHistoryDao("IU", "naver")
            searchHistoryRepository.save(searchHistoryDao)
        }
        for (i in 1..numberOfJ) {
            val searchHistoryDao = SearchHistoryDao("J", "naver")
            searchHistoryRepository.save(searchHistoryDao)
        }
    }

    @Test
    @DisplayName("01. 블로그 검색")
    fun searchBlog() {
        val url = "/blog?keyword=BTS"
        val mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        val resultStr = mvcResult.response.contentAsString
        val type = object : TypeToken<PageResponseDto<BaseBlogDto>>() {}.type
        val result: PageResponseDto<BaseBlogDto> = Gson().fromJson(resultStr, type)

        // 검색 결과 확인
        Assertions.assertTrue(result.content.size > 0)
    }

    @Test
    @DisplayName("01. 인기 키워드 검색")
    fun searchPopularKeyword() {
        val url = "/blog/popular/keyword"
        val mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        val resultStr = mvcResult.response.contentAsString
        val type = object : TypeToken<List<PopularKeywordDto>>() {}.type
        val result: List<PopularKeywordDto> = Gson().fromJson(resultStr, type)
        val btsCount = result.single { it.keyword == "BTS" }.count ?: 0

        // 검색 결과 확인 (초기 세팅 값 + 검색 테스트 1건)
        Assertions.assertEquals(numberOfBts + 1, btsCount.toInt())
    }
}