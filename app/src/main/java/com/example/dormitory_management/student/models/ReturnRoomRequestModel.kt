package com.example.dormitory_management.student.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date


data class ReturnRoomRequestModel(
    val returnDate: Date,
    val status: String
) {
    constructor(document: DocumentSnapshot) : this(
        returnDate = document.getDate("dateReturn") ?: Date(),  // Get Date directly
        status = document.getString("status") ?: "pending"
    )
}
