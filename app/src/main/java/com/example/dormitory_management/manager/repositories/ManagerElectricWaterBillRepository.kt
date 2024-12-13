package com.example.dormitory_management.manager.repositories

import android.util.Log
import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.BillModel
import com.google.firebase.firestore.FirebaseFirestore

class ManagerElectricWaterBillRepository {
    private val firestore: FirebaseFirestore = FirebaseUtils.instance.firestore

    fun loadAllElectricWaterBills(roomId: String, callback: MyCallBack<List<BillModel>, String>) {
        val bills = mutableListOf<BillModel>()

        firestore.collection("rooms")
            .document(roomId)
            .collection("bills")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    try {
                        val bill = BillModel(document)
                        bills.add(bill)
                        Log.d("Firestore", "Số hóa đơn tải về: ${documents.size()}")
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
    fun addElectricWaterBill(roomId: String, bill: BillModel, callback: MyCallBack<Boolean, String>) {
        firestore.collection("rooms")
            .document(roomId)
            .collection("bills")
            .add(bill)
            .addOnSuccessListener { documentReference ->
                callback.success(true)
            }
            .addOnFailureListener { exception ->
                callback.fail("Lỗi khi thêm hóa đơn: ${exception.message}")
            }
    }
    fun updateElectricWaterBill(roomId: String, billId: String, updatedBill: BillModel, callback: MyCallBack<Boolean, String>) {
        firestore.collection("rooms")
            .document(roomId)
            .collection("bills")
            .document(billId)
            .update(
                "title", updatedBill.title,
                "electricStartNumber", updatedBill.electricStartNumber,
                "electricEndNumber", updatedBill.electricEndNumber,
                "waterStartNumber", updatedBill.waterStartNumber,
                "waterEndNumber", updatedBill.waterEndNumber,
                "electricUsed", updatedBill.electricUsed,
                "waterUsed", updatedBill.waterUsed,
                "totalElectric", updatedBill.totalElectric,
                "totalWater", updatedBill.totalWater,
                "totalBill", updatedBill.totalBill,
                "status", updatedBill.status
            )
            .addOnSuccessListener {
                callback.success(true)
            }
            .addOnFailureListener { exception ->
                callback.fail("Lỗi khi cập nhật hóa đơn: ${exception.message}")
            }
    }
    fun deleteElectricWaterBill(roomId: String, billId: String, callback: MyCallBack<Boolean, String>) {
        val billDoc = firestore.collection("rooms")
            .document(roomId)
            .collection("bills")
            .document(billId)

        billDoc.get()
            .addOnSuccessListener { document ->
                if (document.exists() && document.getString("status") == "Chưa thanh toán") {
                    billDoc.delete()
                        .addOnSuccessListener {
                            callback.success(true)
                        }
                        .addOnFailureListener { exception ->
                            callback.fail("Lỗi khi xóa hóa đơn: ${exception.message}")
                        }
                } else {
                    callback.fail("Hóa đơn không thể xóa do hóa đơn đã thanh toán")
                }
            }
            .addOnFailureListener { exception ->
                callback.fail("Lỗi khi kiểm tra trạng thái hóa đơn: ${exception.message}")
            }
    }


}
