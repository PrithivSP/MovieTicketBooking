package utils

private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
private const val PHONE_PATTERN = "^[0-9]{10}$"
private const val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=!]{8,}$"

private const val MIN_AGE = 3
private const val MAX_AGE = 120

fun String.isValidEmailId(): Boolean = this.matches(EMAIL_PATTERN.toRegex())

fun String.isValidPhoneNumber(): Boolean = this.matches(PHONE_PATTERN.toRegex())

fun String.isValidPassword(): Boolean = this.matches(PASSWORD_PATTERN.toRegex())

fun Int.isValidAge(): Boolean = this in MIN_AGE..MAX_AGE