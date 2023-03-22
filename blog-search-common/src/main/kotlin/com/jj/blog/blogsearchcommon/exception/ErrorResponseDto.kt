package com.jj.blog.blogsearchcommon.exception

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

/**
 * REST API 에러 결과에 사용하는 DTO
 */
@Schema(description = "에러 결과")
data class ErrorResponseDto(
    @Schema(description = "에러 코드") var code: Int? = null,
    @Schema(description = "에러 내용") var message: String? = null,
    @Schema(description = "에러 리스트") var errors: MutableList<CustomFieldError> = mutableListOf()
) {

    companion object {
        fun of(restfulError: RestfulErrorCode, bindingResult: BindingResult): ErrorResponseDto {
            return ErrorResponseDto(restfulError, CustomFieldError.of(bindingResult))
        }

        fun of(restfulError: RestfulErrorCode): ErrorResponseDto {
            return ErrorResponseDto(restfulError)
        }

//        fun of(restfulError: RestfulErrorCode, message: String): ErrorResponseDto {
//            return ErrorResponseDto(restfulError, message)
//        }

        fun of(
            restfulError: RestfulErrorCode, errors: MutableList<CustomFieldError>
        ): ErrorResponseDto {
            return ErrorResponseDto(restfulError, errors)
        }
    }

    constructor(restfulError: RestfulErrorCode, errors: MutableList<CustomFieldError>) : this() {
        this.code = restfulError.code
        this.message = restfulError.message
        this.errors = errors
    }

    constructor(restfulError: RestfulErrorCode) : this() {
        this.code = restfulError.code
        this.message = restfulError.message
        this.errors = mutableListOf()
    }

    constructor(restfulError: RestfulErrorCode, message: String) : this() {
        this.code = restfulError.code
        this.message = message
        this.errors = mutableListOf()
    }

    class CustomFieldError(val field: String?, val value: String, val reason: String?) {
        companion object {
            fun of(field: String, value: String, reason: String?): MutableList<CustomFieldError> {
                val fieldErrors: MutableList<CustomFieldError> = mutableListOf()
                fieldErrors.add(CustomFieldError(field, value, reason))
                return fieldErrors
            }

            fun of(bindingResult: BindingResult): MutableList<CustomFieldError> {
                val fieldErrors = bindingResult.fieldErrors
                return fieldErrors.map { error: FieldError ->
                    CustomFieldError(
                        error.field, error.rejectedValue as String, error.defaultMessage
                    )
                }.toMutableList()
            }
        }
    }
}