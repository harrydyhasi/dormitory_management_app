package com.example.dormitory_management.manager.models

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable
import java.util.Date

data class RenewRequestModel(
    val idUser: String?,
    val nameUser: String?,
    val roomNumber: String?,
    val buildingNumber: String?,
    val id: String?,
    val endDate: Date?,
    val startDate: Date?,
    val status: String?,
    ): Serializable {

//    constructor(idUser: String, id:String, data: MutableMap<String, Any>): this(
//        idUser,
//        "",
//        "",
//        "",
//        id,
//        data["endDate"] as Date?,
//        data["startDate"] as Date?,
//        data["status"].toString(),
//    )

//    constructor(idUser: String,id:String, dataSnapshot: DocumentSnapshot): this(
//        idUser ?: "",
//        id ?: "",
//        "",
//        "",
//        dataSnapshot.getDate("endDate"),
//        dataSnapshot.getDate("startDate"),
//        dataSnapshot.getString("status").toString(),
//    )

    constructor(idUser: String,id:String, nameUser: String, roomNumber:String, buildingNumber: String,  dataSnapshot: DocumentSnapshot): this(
        idUser ?: "",
        nameUser ?: "",
        roomNumber ?:"",
        buildingNumber ?:"",
        id ?:"",
        dataSnapshot.getDate("endDate"),
        dataSnapshot.getDate("startDate"),
        dataSnapshot.getString("status").toString(),
    )

    constructor() : this(
        idUser = "null",
        nameUser = "null",
        roomNumber = "null",
        buildingNumber = "null",
        id = "null",
        endDate = null,
        startDate = null,
        status = null,
    )
}