package com.example.dormitory_management.student.repositories

import android.util.Log
import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.ElectricWaterBillModel
import com.google.firebase.firestore.FirebaseFirestore

class ElectricWaterBillRepository {
    private val firestore: FirebaseFirestore = FirebaseUtils.instance.firestore

    fun loadAllElectricWaterBills(roomId: String, callback: MyCallBack<List<ElectricWaterBillModel>, String>) {
        val bills = mutableListOf<ElectricWaterBillModel>()

        firestore.collection("rooms")
            .document(roomId)
            .collection("bills")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    try {
                        val bill = ElectricWaterBillModel(document)
                        bills.add(bill)
                    } catch (e: Exception) {
                        Log.e("Lỗi", "Lỗi khi tải hóa đơn: ${e.message}")
                    }
                }
                callback.success(bills)
            }

            .addOnFailureListener { exception ->
                callback.fail("Lỗi khi tải hóa đơn: ${exception.message}")
            }
    }

    fun updateStatusElectricWaterBill(
        roomId: String,
        billId: String,
        status: String,
        callback: MyCallBack<ElectricWaterBillModel, String>
    ) {
        // Find the room by roomNumber
        firestore.collection("rooms")
            .whereEqualTo("roomNumber", roomId) // Assuming roomNumber is a field in the "rooms" collection
            .get()
            .addOnSuccessListener { roomDocuments ->
                if (roomDocuments.isEmpty) {
                    callback.fail("Room not found.")
                    return@addOnSuccessListener
                }

                val roomDocument = roomDocuments.documents.first()

                roomDocument.reference.collection("bills")
                    .document(billId)
                    .get()
                    .addOnSuccessListener { billDocument ->
                        if (!billDocument.exists()) {
                            callback.fail("Bill not found.")
                            return@addOnSuccessListener
                        }

                        try {

                            billDocument.reference.update("status", status)
                                .addOnSuccessListener {
                                    // Retrieve the updated bill document
                                    billDocument.reference.get()
                                        .addOnSuccessListener { updatedDocument ->
                                            if (updatedDocument.exists()) {
                                                val updatedBill = ElectricWaterBillModel(updatedDocument)
                                                callback.success(updatedBill)  // Return the updated bill
                                            } else {
                                                callback.fail("Bill not found after update.")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            callback.fail("Failed to retrieve updated bill: ${exception.message}")
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    callback.fail("Failed to update status: ${exception.message}")
                                }
                        } catch (e: Exception) {
                            Log.e("Error", "Error while processing the bill: ${e.message}")
                            callback.fail("Error while processing the bill: ${e.message}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        callback.fail("Failed to retrieve bill: ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to retrieve room: ${exception.message}")
            }
    }


}