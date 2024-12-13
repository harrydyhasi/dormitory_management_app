package com.example.dormitory_management.manager.models

data class RoomRequestModel(
    val student: UserModel?,
    val roomNumber: String? = "",
    val buildingNumber: String? = ""
) {
    constructor(
    ):this(
        null,
        "",
        "",
    )
}
