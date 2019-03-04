package com.joom.challenge.ui

data class NetworkRequestState(
    val type: Type,
    val errorText: String? = null,
    var isErrorShown: Boolean = false,
    val isDataOutdated: Boolean = false,
    var isDataOutdatedHandled: Boolean = false
) {

    enum class Type {
        LOADING, COMPLETE, ERROR
    }

    companion object {
        fun loading(): NetworkRequestState {
            return NetworkRequestState(Type.LOADING)
        }

        fun complete(isDataOutdated: Boolean = false): NetworkRequestState {
            return NetworkRequestState(Type.COMPLETE, isDataOutdated = isDataOutdated)
        }

        fun error(error: String): NetworkRequestState {
            return NetworkRequestState(Type.ERROR, error)
        }
    }
}