package com.example.dormitory_management.manager.repositories

import android.content.Context
import android.util.Log
import com.example.dormitory_management.manager.models.RepairRequestModel
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.Utils.FirebaseUtils
import com.google.firebase.firestore.FirebaseFirestore

class RepairRequestRepository(private val context: Context) {

    private val firestore: FirebaseFirestore = FirebaseUtils.instance.firestore

    fun loadAllRepairRequests(callback: MyCallBack<List<RepairRequestModel>, String>) {
        val repairRequests = mutableListOf<RepairRequestModel>()

        firestore.collection("rooms")
            .get()
            .addOnSuccessListener { rooms ->
                if (rooms.isEmpty) {
                    callback.success(repairRequests)
                    return@addOnSuccessListener
                }

                var pendingRequests = rooms.size()

                for (room in rooms) {
                    val roomId = room.id
                    Log.d("RoomCheck", "Room ID: $roomId, Data: ${room.data}")
                    val buildingNumber = room.getString("buildingNumber") ?: "N/A"
                    val roomNumber = room.getString("roomNumber") ?: "N/A"

                    firestore.collection("rooms")
                        .document(roomId)
                        .collection("repairRequests")
                        .whereEqualTo("status", "pending")
                        .get()
                        .addOnSuccessListener { requests ->
                            for (request in requests) {
                                val id = request.id
                                val name = request.getString("name") ?: ""
                                val description = request.getString("description") ?: ""
                                val status = request.getString("status") ?: ""

                                val repairRequest = RepairRequestModel(
                                    id = id,
                                    name = name,
                                    description = description,
                                    status = status,
                                    roomId = roomId,
                                    buildingNumber = buildingNumber,
                                    roomNumber = roomNumber
                                )
                                repairRequests.add(repairRequest)
                            }

                            pendingRequests--
                            if (pendingRequests == 0) {
                                callback.success(repairRequests)
                            }
                        }
                        .addOnFailureListener {
                            callback.fail("Lỗi khi tải yêu cầu sửa chữa: ${it.message}")
                            pendingRequests--
                            if (pendingRequests == 0) {
                                callback.success(repairRequests)
                            }
                        }
                }
            }
            .addOnFailureListener {
                callback.fail("Lỗi khi tải dữ liệu từ Firebase: ${it.message}")
            }
    }

    fun approveRepairRequest(requestId: String, roomId: String, callback: MyCallBack<Unit, String>) {
        val repairRequestRef = firestore.collection("rooms")
            .document(roomId)
            .collection("repairRequests")
            .document(requestId)

        repairRequestRef.update("status", "Đã duyệt")
            .addOnSuccessListener { callback.success(Unit) }
            .addOnFailureListener { callback.fail("Lỗi khi duyệt yêu cầu: ${it.message}") }
    }
}
