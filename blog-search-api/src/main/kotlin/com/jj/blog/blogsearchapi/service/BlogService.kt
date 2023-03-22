package com.jj.blog.blogsearchapi.service

import com.jj.blog.blogsearchapi.client.RemoteClients
import com.jj.blog.blogsearchapi.config.AppConfig
import com.jj.blog.blogsearchapi.mapper.PopularKeywordMapper
import com.jj.blog.blogsearchapi.repository.PopularKeywordHourRepository
import com.jj.blog.blogsearchapi.repository.SearchHistoryRepository
import com.jj.blog.blogsearchcommon.dto.BaseBlogDto
import com.jj.blog.blogsearchcommon.dto.PageQueryDto
import com.jj.blog.blogsearchcommon.dto.PopularKeywordDto
import com.jj.blog.blogsearchcommon.exception.RestfulErrorCode
import com.jj.blog.blogsearchcommon.exception.ServiceException
import com.jj.blog.blogsearchrdb.dao.SearchHistoryDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
@ComponentScan(basePackages = ["com.jj.blog.blogsearchclient.client"])
class BlogService(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val popularKeywordHourRepository: PopularKeywordHourRepository,
    private val popularKeywordMapper: PopularKeywordMapper,
    private val appConfig: AppConfig,
    private val remoteClients: RemoteClients,
) {
    @Value("\${bs.popular.keyword.count:10}")
    val popularKeywordCount: Long = 10

    fun readBlog(keyword: String, pageQueryDto: PageQueryDto): Page<out BaseBlogDto> {
        for (client in appConfig.clients) {
            val serverName = client.key
            val clientConfigMap = client.value
            val baseUrl = clientConfigMap.baseUrl
            val apiPathTemplate = clientConfigMap.apiPath
            val headers = clientConfigMap.headers

            try {
                // 원격지 서버 조회
                val baseBlogDtoList = remoteClients.client(
                    serverName, baseUrl, apiPathTemplate, headers, keyword, pageQueryDto
                )
                // search history 입력
                val searchHistoryDao = SearchHistoryDao(keyword, serverName)
                searchHistoryRepository.save(searchHistoryDao)
                return PageImpl(baseBlogDtoList, pageQueryDto.of(), baseBlogDtoList.size.toLong())
            } catch (th: Throwable) {
                log.error("$serverName connection failure")
                continue
            }
        }
        throw ServiceException(RestfulErrorCode.REMOTE_SERVER_RESULT_ERROR)
    }

    // 검색 이력으로 실시간 집계하여 조회
    fun readPopularKeyword(): List<PopularKeywordDto> {
        val now = LocalDateTime.now()
        val startTime = now.minus(Duration.ofHours(24))
        val newPopularKeywordDtoList = searchHistoryRepository
            .aggregatePopularKeyword(startTime, now, popularKeywordCount)
        if (newPopularKeywordDtoList.isEmpty())
            throw ServiceException(RestfulErrorCode.NOT_EXIST_HISTORY, "최근 검색 이력이 존재하지 않습니다.")

        return newPopularKeywordDtoList
    }

    // 1시간 단위로 업데이트되는 집계 데이터 조회
    fun readPopularKeywordOrAggregate(): List<PopularKeywordDto> {
        // 매 시간 마다 1시간 단위 집계가 생성된다 (예: 오늘 12시에 생성되는 집계는 전날 12시 이상 오늘 12시 미만 데이터 기준 집계이다)
        // 조회 시간이 12시 라면 집계종료시간이 11시 보다 큰 집계를 조회한다
        val now = LocalDateTime.now()
        val before1Hour = now.minusHours(1)
        // 최근 집계 조회
        val popularKeywordDtoList = popularKeywordHourRepository.findLatestPopularKeyList(before1Hour)
            .map { dao -> popularKeywordMapper.toDto(dao) }

        // 최근 집계 없으면 최근 23시간+ 동안의 조회수를 집계
        if (popularKeywordDtoList.isEmpty()) {
            val startTime = now.withMinute(0).withSecond(0).withNano(0)
                .minus(Duration.ofHours(23))
            val newPopularKeywordDtoList = searchHistoryRepository
                .aggregatePopularKeyword(startTime, now, popularKeywordCount)
            if (newPopularKeywordDtoList.isEmpty()) {
                throw ServiceException(RestfulErrorCode.NOT_EXIST_HISTORY, "최근 검색 이력이 존재하지 않습니다.")
            } else {
                val popularKeywordHourDaoList = popularKeywordDtoList.map { dto -> popularKeywordMapper.toEntity(dto) }
                popularKeywordHourRepository.saveAll(popularKeywordHourDaoList)
                return newPopularKeywordDtoList
            }
        }

        // 리턴
        return popularKeywordDtoList
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}