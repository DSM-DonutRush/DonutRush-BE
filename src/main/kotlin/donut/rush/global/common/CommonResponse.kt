package donut.rush.global.common

data class CommonResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
) {
    companion object {
        fun <T> ok(
            data: T,
            message: String = "",
        ) = CommonResponse(success = true, message = message, data = data)

        fun ok(message: String = "") = CommonResponse<Nothing>(success = true, message = message)

        fun fail(message: String) = CommonResponse<Nothing>(success = false, message = message)
    }
}
