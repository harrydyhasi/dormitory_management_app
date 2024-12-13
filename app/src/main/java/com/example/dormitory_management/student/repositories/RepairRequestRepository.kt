package com.example.dormitory_management.student.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.RepairRequestModel
import com.google.firebase.firestore.FirebaseFirestore

class RepairRequestRepository {

    fun addRepairRequest(roomId: String, name: String, description: String, callback: MyCallBack<Unit, String>) {
        val repairRequest = hashMapOf(
            "name" to name,
            "description" to description,
            "status" to "pending"
        )

        FirebaseUtils.instance.get_document("rooms", roomId)
            .collection("repairRequests")
            .add(repairRequest)
            .addOnSuccessListener {
                callback.success(Unit)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to add repair request: ${exception.message}")
            }
    }

    fun getRepairRequests(roomId: String, callback: MyCallBack<List<RepairRequestModel>, String>) {
        FirebaseUtils.instance.get_document("rooms", roomId)
            .collection("repairRequests")
            .get()
            .addOnSuccessListener { result ->
                val repairRequests = result.map { document -> RepairRequestModel(document) }
                callback.success(repairRequests)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to load repair requests: ${exception.message}")
            }
    }
}
