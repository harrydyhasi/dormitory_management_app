package com.example.dormitory_management.manager.models
import com.google.firebase.firestore.DocumentSnapshot

data class RepairRequestModel(
    val id: String,
    val name: String = "",
    val description: String = "",
    val status: String = "",
    val roomId: String = "",
    val buildingNumber: String = "",
    val roomNumber: String = ""
) {
    constructor(documentSnapshot: DocumentSnapshot) : this(
        id = documentSnapshot.id,
        name = documentSnapshot.getString("name") ?: "",
        description = documentSnapshot.getString("description") ?: "",
        status = documentSnapshot.getString("status") ?: "",
        roomId = documentSnapshot.getString("roomId") ?: "",
        buildingNumber = documentSnapshot.getString("buildingNumber") ?: "",
        roomNumber = documentSnapshot.getString("roomNumber") ?: ""
    )
}
