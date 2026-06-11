package donut.rush.global.error

import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DonutRushException::class)
    fun handleDonutRushException(e: DonutRushException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(e.errorCode.status)
            .body(ErrorResponse(message = e.message ?: e.errorCode.message))

    @ExceptionHandler(
        IllegalArgumentException::class,
        MethodArgumentTypeMismatchException::class,
    )
    fun handleBadRequest(e: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(ErrorCode.INVALID_INPUT.status)
            .body(ErrorResponse(message = ErrorCode.INVALID_INPUT.message))

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .internalServerError()
            .body(ErrorResponse(message = "서버 오류가 발생했습니다."))
}
