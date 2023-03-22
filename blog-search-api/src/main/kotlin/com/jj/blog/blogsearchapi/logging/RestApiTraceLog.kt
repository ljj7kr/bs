package com.jj.blog.blogsearchapi.logging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.jj.blog.blogsearchcommon.constant.Constants
import com.jj.blog.blogsearchcommon.exception.ErrorResponseDto
import org.aspectj.lang.JoinPoint
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/** RESTFul API 공통 포맷 TraceLog 기록  */
object RestApiTraceLog {
    private const val REQ_ATTRIBUTE_NAME_START_TIME = "START_TIME"
    private val traceLog = LoggerFactory.getLogger(Constants.LOGGER_NAME_TRACE)

    fun requestLog(p: JoinPoint) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        var clientIp = request.getHeader("X-FORWARDED-FOR")
        if (clientIp == null) clientIp = request.remoteAddr
        val startTime = System.currentTimeMillis()
        request.setAttribute(REQ_ATTRIBUTE_NAME_START_TIME, startTime)
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

        traceLog.info(
            "REQ {}?{} {} {}; {}",
            request.requestURI,
            if (StringUtils.hasText(request.queryString)) request.queryString else "",
            p.target.javaClass.simpleName,
            p.signature.name,
            clientIp
        )
    }

    fun responseLog(p: JoinPoint, returnValue: Any) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        var elapsed = -1L
        val obj = request.getAttribute(REQ_ATTRIBUTE_NAME_START_TIME)
        if (obj != null) {
            val startTime = obj as Long
            val endTime = System.currentTimeMillis()
            elapsed = endTime - startTime
        }
        var resultStatusCode: HttpStatusCode = HttpStatus.OK
        if (ResponseEntity::class.java.isAssignableFrom(returnValue.javaClass)) {
            val responseEntity = returnValue as ResponseEntity<*>
            resultStatusCode = responseEntity.statusCode
            val body = responseEntity.body
            if (body != null && ErrorResponseDto::class.java.isAssignableFrom(body.javaClass)) {
                val (code, message) = body as ErrorResponseDto

                // 에러
                traceLog.info(
                    "RES {}?{} {} {}; {}, {}, {}, {}ms",
                    request.requestURI,
                    if (StringUtils.hasText(request.queryString)) request.queryString else "",
                    p.target.javaClass.simpleName,
                    p.signature.name,
                    resultStatusCode.value(),
                    code,
                    message,
                    elapsed
                )
            } else {
                // 정상
                traceLog.info(
                    "RES {}?{} {} {}; {}, 0, SUCCESS, {}ms",
                    request.requestURI,
                    if (StringUtils.hasText(request.queryString)) request.queryString else "",
                    p.target.javaClass.simpleName,
                    p.signature.name,
                    resultStatusCode.value(),
                    elapsed
                )
            }
        } else {
            // ResponseEntity 사용하지 않은 경우
            traceLog.info(
                "RES {}?{} {} {}; {}, 0, SUCCESS, {}ms",
                request.requestURI,
                if (StringUtils.hasText(request.queryString)) request.queryString else "",
                p.target.javaClass.simpleName,
                p.signature.name,
                resultStatusCode.value(),
                elapsed
            )
        }
    }
}