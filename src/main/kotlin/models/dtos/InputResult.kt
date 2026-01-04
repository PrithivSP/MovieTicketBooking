package models.dtos

sealed class NavResult<out T> {
    object Back: NavResult<Unit>()
    object Exit: NavResult<Nothing>()
    object Retry: NavResult<Unit>()

    data class Result<T>(val result: T): NavResult<T>()
}