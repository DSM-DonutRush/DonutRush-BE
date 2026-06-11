package donut.rush.global.error

data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val data: Any? = null,
)
