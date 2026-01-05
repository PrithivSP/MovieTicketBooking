package models.dtos

sealed class NavResult<out T> {
    object Back: NavResult<Nothing>()
    object Exit: NavResult<Nothing>()
    object Retry: NavResult<Nothing>()

    data class Result<T>(val result: T): NavResult<T>()
}