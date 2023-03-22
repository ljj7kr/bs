package com.jj.blog.blogsearchcommon.exception

/**
 * Service Level 에러 발생 시 사용하는 Exception
 */
class ServiceException : RuntimeException {
    var restfulErrorCode: RestfulErrorCode

    constructor(restfulErrorCode: RestfulErrorCode) {
        this.restfulErrorCode = restfulErrorCode
    }

    constructor(restfulErrorCode: RestfulErrorCode, message: String?) : super(message) {
        this.restfulErrorCode = restfulErrorCode
    }

    constructor(restfulErrorCode: RestfulErrorCode, cause: Throwable?) : super(cause) {
        this.restfulErrorCode = restfulErrorCode
    }

    constructor(
        restfulErrorCode: RestfulErrorCode, message: String?, cause: Throwable?
    ) : super(message, cause) {
        this.restfulErrorCode = restfulErrorCode
    }
}