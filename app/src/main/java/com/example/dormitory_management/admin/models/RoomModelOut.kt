package com.example.dormitory_management.admin.models

data class RoomModelOut(
    val roomNumber: String?,
    val buildingNumber: String?,
    val price: Long?,
    val status: String?,
    val available: Int?,
    var id: String? = null
) {
    constructor() : this(
        "",
        "",
        null,
        "",
         null,
    )
}