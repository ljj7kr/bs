package com.jj.blog.blogsearchapi.logging

import com.jj.blog.blogsearchapi.logging.RestApiTraceLog.requestLog
import com.jj.blog.blogsearchapi.logging.RestApiTraceLog.responseLog
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

/** 공통 Logging 처리  */
@Aspect
@Component
class ControllerTraceLoggingAspect {
    @Before(value = "within(com.jj..controller..*Controller)")
    fun endpointBefore(p: JoinPoint?) {
//    MethodSignature signature = (MethodSignature) p.getSignature();
//    TraceRequest traceRequest = signature.getMethod().getAnnotation(TraceRequest.class);
        requestLog(p!!)
    }

    @AfterReturning(value = "within(com.jj..controller..*)", returning = "returnValue")
    fun endpointAfter(p: JoinPoint?, returnValue: Any?) {
//    MethodSignature signature = (MethodSignature) p.getSignature();
//    TraceResponse traceResponse = signature.getMethod().getAnnotation(TraceResponse.class);
        responseLog(p!!, returnValue!!)
    }
}