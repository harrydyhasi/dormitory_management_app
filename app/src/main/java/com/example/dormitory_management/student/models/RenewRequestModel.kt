package com.example.dormitory_management.student.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class RenewRequestModel(
    val startDate: Date?,
    val endDate: Date?,
    val status: String?
) {
    constructor(document: DocumentSnapshot) : this(
        startDate = document.getDate("startDate") ?: Date(),
        endDate = document.getDate("endDate") ?: Date(),
        status = document.getString("status") ?: "pending"
    )
}