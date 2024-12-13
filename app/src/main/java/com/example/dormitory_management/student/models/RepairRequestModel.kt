package com.example.dormitory_management.student.models

import com.google.firebase.firestore.DocumentSnapshot

data class RepairRequestModel(
    val name: String,
    val description: String,
    val status: String
) {
    constructor(document: DocumentSnapshot) : this(
        name = document.getString("name") ?: "",
        description = document.getString("description") ?: "",
        status = document.getString("status") ?: "pending"
    )
}
