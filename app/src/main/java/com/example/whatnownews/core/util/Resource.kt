package com.example.whatnownews.core.util

/**
 * A generic class that holds a value with its loading status.
 * @param <T> The type of the data.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful state with data.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents an error state with a message.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents a loading state.
     */
    class Loading<T> : Resource<T>()
}

