package fly.spring.common.kotlin.extension

import fly.spring.common.exception.GeneralException

fun String.genGeneralException(): GeneralException = GeneralException(this)