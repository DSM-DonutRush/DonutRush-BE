package donut.rush.global.error.exception

class DonutRushException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
