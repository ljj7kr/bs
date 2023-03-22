//package com.jj.blog.blogsearchcommon.exception
//
//import jakarta.validation.ConstraintViolationException
//import org.slf4j.LoggerFactory
//import org.springframework.dao.DataIntegrityViolationException
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.validation.BindException
//import org.springframework.web.HttpRequestMethodNotSupportedException
//import org.springframework.web.bind.MethodArgumentNotValidException
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
//
///** Controller Exception Handling  */
//@ControllerAdvice
//class ControllerExceptionHandler : ResponseEntityExceptionHandler() {
//
//    companion object {
//        private val log = LoggerFactory.getLogger(this::class.java)
//    }
//
//    /** Service 에서 발생시키는 Exception  */
//    @ExceptionHandler(ServiceException::class)
//    protected fun handleServiceException(e: ServiceException): ResponseEntity<ErrorResponseDto> {
//        val errorCode = e.restfulErrorCode
//        log.error("handleServiceException, errorCode : {}", errorCode, e)
//        val errorResponseDto = ErrorResponseDto.of(errorCode)
//        return ResponseEntity(errorResponseDto, HttpStatus.valueOf(errorCode.status))
//    }
//
//    /**
//     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다. HttpMessageConverter 에서 등록한
//     * HttpMessageConverter binding 못할경우 발생 주로 @RequestBody, @RequestPart 어노테이션에서 발생
//     */
//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    protected fun handleMethodArgumentNotValidException(
//        e: MethodArgumentNotValidException
//    ): ResponseEntity<ErrorResponseDto> {
//        log.error("handleMethodArgumentNotValidException", e)
//        val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
//        return ResponseEntity(errorResponseDto, HttpStatus.valueOf(RestfulErrorCode.INVALID_INPUT_VALUE.status))
//    }
//
//    /** 바인딩 오류, 주로 원시타입 변수에 null 할당 시도시 발생 */
//    @ExceptionHandler(BindException::class)
//    protected fun handleBindException(e: BindException): ResponseEntity<ErrorResponseDto> {
//        log.error("handleBindException", e)
//        val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
//        return ResponseEntity(errorResponseDto, HttpStatus.valueOf(RestfulErrorCode.INVALID_INPUT_VALUE.status))
//    }
//
//    /** 메서드 파라미터 타입 불일치  */
//    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
//    protected fun handleMethodArgumentTypeMismatchException(
//        e: MethodArgumentTypeMismatchException
//    ): ResponseEntity<ErrorResponseDto> {
//        log.error("handleMethodArgumentTypeMismatchException", e)
//        val value = e.value
//        val result = value?.toString() ?: ""
//        val errors = ErrorResponseDto.CustomFieldError.of(e.name, result, e.errorCode)
//        val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.INVALID_ARGUMENT, errors)
//        return ResponseEntity(errorResponseDto, HttpStatus.BAD_REQUEST)
//    }
//
//    /** 지원하지 않은 HTTP method 호출 할 경우 발생  */
//    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
//    protected fun handleHttpRequestMethodNotSupportedException(
//        e: HttpRequestMethodNotSupportedException
//    ): ResponseEntity<ErrorResponseDto> {
//        log.error("handleHttpRequestMethodNotSupportedException", e)
//        val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.METHOD_NOT_ALLOWED)
//        return ResponseEntity.status(HttpStatus.valueOf(RestfulErrorCode.METHOD_NOT_ALLOWED.status))
//            .body(errorResponseDto)
////        return ResponseEntity(errorResponseDto, HttpStatus.valueOf(RestfulErrorCode.METHOD_NOT_ALLOWED.status))
//    }
//
//    /** DAO Constraint Exception, 대표적으로 JPA 사용 시 Unique Constraint */
//    @ExceptionHandler(DataIntegrityViolationException::class)
//    protected fun handleDataIntegrityViolationException(
//        e: DataIntegrityViolationException
//    ): ResponseEntity<ErrorResponseDto> {
//        log.error("handleDataIntegrityViolationException", e)
//        val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.CONFLICT)
//        return ResponseEntity(errorResponseDto, HttpStatus.valueOf(RestfulErrorCode.CONFLICT.status))
//    }
//
//    /** 기타 Exception  */
//    @ExceptionHandler(Exception::class)
//    protected fun handleException(e: Exception): ResponseEntity<ErrorResponseDto> {
//        // ConstraintViolationException 은 다른 유형으로 래핑되고 다시 던져지기 때문에 직접 캐치할 수 없다 따라서 이곳에서 처리함
//        return if (e is ConstraintViolationException) {
//            log.error("handleConstraintViolationException", e)
//            val constraintViolations = e.constraintViolations
//            val errors: MutableList<ErrorResponseDto.CustomFieldError> = mutableListOf()
//            for (c in constraintViolations) {
//                errors.addAll(
//                    ErrorResponseDto.CustomFieldError.of(
//                        c.propertyPath.toString(), c.invalidValue.toString(), c.message
//                    )
//                )
//            }
//            val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.INVALID_INPUT_VALUE, errors)
//            ResponseEntity(errorResponseDto, HttpStatus.valueOf(RestfulErrorCode.INVALID_INPUT_VALUE.status))
//        // 기타 Exception
//        } else {
//            log.error("handleException", e)
//            val errorResponseDto = ErrorResponseDto.of(RestfulErrorCode.INTERNAL_SERVER_ERROR)
//            ResponseEntity(errorResponseDto, HttpStatus.valueOf(RestfulErrorCode.INTERNAL_SERVER_ERROR.status))
//        }
//    }
//}
