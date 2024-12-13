package com.example.dormitory_management.admin.models

data class RoomModelIn(
    val roomNumber: String?,
    val buildingNumber: String?,
    val price: Long?,
    val status: String?,
    val available: Int?,
) {
    constructor() : this(
        "",
        "",
        null,
        "",
        null,
    )
}