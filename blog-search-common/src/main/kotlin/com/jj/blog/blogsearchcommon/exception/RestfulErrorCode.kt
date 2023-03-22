package com.jj.blog.blogsearchcommon.exception

enum class RestfulErrorCode(val code: Int, val message: String, val status: Int) {
    /** Service 에러 */
    NOT_EXIST_HISTORY(-100, "검색 기록이 존재하지 않습니다.", 400),

    /** 공통 에러 */
    INTERNAL_SERVER_ERROR(-1, "Internal Server Error", 500),
    INVALID_INPUT_VALUE(-2, "Invalid Input Value", 400),
    INVALID_ARGUMENT(-3, "Invalid Argument", 400),
    METHOD_NOT_ALLOWED(-4, "Method Not Allowed", 405),
    CONFLICT(-5, "Conflict", 409),

    /** 원격 호출 에러 */
    REMOTE_SERVER_UNKNOWN_HOST_ERROR(-900, "Remote Server Unknown Host Error", 500),
    REMOTE_SERVER_CONNECTION_ERROR(-901, "Remote Server Connection Error", 500),
    REMOTE_SERVER_READ_TIMEOUT_ERROR(-902, "Remote Server Read Timeout Error", 500),
    REMOTE_SERVER_RESULT_ERROR(-903, "Remote Server Result Error", 500),
}