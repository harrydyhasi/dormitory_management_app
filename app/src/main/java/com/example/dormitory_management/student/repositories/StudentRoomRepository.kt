package com.example.dormitory_management.student.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.StudentRoomModel

class StudentRoomRepository {
    val collection = "rooms"

    fun getRoomById(id:String, callback: MyCallBack<StudentRoomModel, String>){
        val doc = FirebaseUtils.instance.get_document(collection, id)

        doc.get().addOnSuccessListener { documentSnapshot ->
            callback.success(StudentRoomModel(id, documentSnapshot))
        }.addOnFailureListener { exception ->
            callback.fail("fail getRoom")
        }
    }

}