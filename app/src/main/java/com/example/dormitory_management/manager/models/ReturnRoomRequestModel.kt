package com.example.dormitory_management.manager.models

import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable
import java.util.*

data class ReturnRoomRequestModel(
    val idUser: String?,     // User ID associated with the request
    val id: String?,         // Unique request ID
    val returnDate: Date?,   // Date of return
    val status: String?,
    val fullName: String = "",
    val buildingNumber: String = "",
    val roomId: String = "",
    val roomNumber: String = ""
) : Serializable {

    // Constructor for creating an instance from Firestore data (Map)
    constructor(idUser: String, id: String, data: MutableMap<String, Any>) : this(
        idUser = idUser,
        id = id,
        returnDate = data["returnDate"] as? Date,
        status = data["status"] as? String,

    )

    // Constructor for creating an instance from Firestore DocumentSnapshot
    constructor(idUser: String, id: String, documentSnapshot: DocumentSnapshot) : this(
        idUser = idUser,
        id = id,
        returnDate = documentSnapshot.getDate("dateReturn"),
        status = documentSnapshot.getString("status"),
        fullName = documentSnapshot.getString("fullName") ?: "",
        roomId = documentSnapshot.getString("roomId") ?: "",
        buildingNumber = documentSnapshot.getString("buildingNumber") ?: "",
        roomNumber = documentSnapshot.getString("roomNumber") ?: ""
    )
    constructor(idUser: String,id:String, fullName: String, roomNumber:String, buildingNumber: String,  dataSnapshot: DocumentSnapshot): this(
        idUser ?: "",
        id ?: "",
        dataSnapshot.getDate("dateReturn"),
        dataSnapshot.getString("status").toString(),
        fullName ?: "",
        buildingNumber ?: "",
        roomNumber ?:"",
        roomNumber ?:"",
    )

}
