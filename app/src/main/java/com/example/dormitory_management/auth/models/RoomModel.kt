package com.example.dormitory_management.auth.models

data class RoomModel(
    var id: String = "",
    val roomNumber: String?,
    val buildingNumber: String?,
    val price: Long?,
    val status: String?,
    val available: Int?,
    var isSelected: Boolean = false,
) {
    constructor() : this(
        "",
        "",
        "",
        null,
        "",
        null,
    )
}