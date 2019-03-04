package com.joom.challenge.api

import java.lang.RuntimeException

class ApiException(
    val code: Int,
    val msg: String?
) : RuntimeException("Error: $code $msg")