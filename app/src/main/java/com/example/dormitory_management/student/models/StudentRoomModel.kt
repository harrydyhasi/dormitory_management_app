package com.example.dormitory_management.student.models

import com.google.firebase.firestore.DocumentSnapshot

data class StudentRoomModel (
    val idRoom: String?,
    val available: Long?,
    val buildingNumber: String?,
    val roomNumber: String?,
    val price: Long?,
    val status: String?
){
    constructor(id:String, data: MutableMap<String, Any>): this(
        id,
        data["available"] as Long?,
        data["buildingNumber"].toString(),
        data["roomNumber"].toString(),
        data["price"] as Long?,
        data["status"].toString(),
    )

    constructor(id:String, dataSnapshot: DocumentSnapshot): this(
        id,
        dataSnapshot.getLong("available"),
        dataSnapshot.getString("buildingNumber"),
        dataSnapshot.getString("roomNumber"),
        dataSnapshot.getLong("price"),
        dataSnapshot.getString("status"),
    )

    override fun toString(): String {
        return "StudentRoomModel(idRoom=$idRoom, available=$available, numberBuilding=$buildingNumber, numberRoom=$roomNumber, price=$price, status=$status)"
    }


}