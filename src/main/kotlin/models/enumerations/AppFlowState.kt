package models.enumerations

enum class AppFlowState {
    AUTH_MENU,

    LOGIN,
    SIGNUP,

    USER_MENU,

    BROWSE_MOVIES,
    SEARCH_MOVIE,
//    BROWSE_THEATER,

    SELECT_MOVIE,
    SELECT_THEATER,
    SELECT_SCREEN,
    SELECT_DATE,
    SELECT_TIME,
    SELECT_SEATS,
    BOOKING,
//    CHANGE_LOCATION,
//    USER_HISTORY,
//    CANCEL_BOOKING,
//    UPDATE_PROFILE,

    EXIT
}