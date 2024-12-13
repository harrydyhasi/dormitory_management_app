package com.example.dormitory_management.manager.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class UserModel(
    val fullName: String?,
    val cccd: String?,
    val email: String?,
    val date: Date?,
    val gender: String?,
    val password:String?,
    val phoneNumber: String?,
    val idRoom: String?,
    val dateStart: Date?,
    val dateEnd: Date?,
    val dateReturn:Date?,
    val price: Long?,
    val status: String?,
    val statusReturn: String?,
)  {
    constructor() : this(
        "",
        "",
        "",
        null,
        "",
        "",
        "",
        "",
        null,
        null,
        null,
        0,
        "",
        "",
    )

    constructor(dataSnapshot: DocumentSnapshot): this(
        dataSnapshot.getString("fullName"),
        dataSnapshot.getString("cccd"),
        dataSnapshot.getString("email"),
        dataSnapshot.getDate("date"),
        dataSnapshot.getString("gender"),
        "",
        dataSnapshot.getString("phoneNumber"),
        dataSnapshot.getString("idRoom"),
        dataSnapshot.getDate("dateStart"),
        dataSnapshot.getDate("dateEnd"),
        dataSnapshot.getDate("dateReturn"),
        dataSnapshot.getLong("price"),
        dataSnapshot.getString("status"),
        dataSnapshot.getString("statusReturn"),
    )

    constructor(
        fullName: String,
        cccd: String,
        email: String,
        date: String,
        gender: String,
        password: String,
        phoneNumber: String,
        idRoom: String,
        dateStart: Nothing?,
        dateEnd: Nothing?,
        dateReturn: Nothing?,
        price: Long,
        status: String,
        statusReturn: String
    ) : this()

    override fun toString(): String {
        return "UserModel(fullName='$fullName', cccd='$cccd', email='$email', date=$date, gender='$gender', password='$password', phoneNumber='$phoneNumber', idRoom='$idRoom', dateStart=$dateStart, dateEnd=$dateEnd, dateReturn=$dateReturn, price=$price, status='$status', statusReturn='$statusReturn')"
    }

}