package com.example.dormitory_management.auth.repositories

import android.util.Log
import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.admin.models.RoomModelOut
import com.example.dormitory_management.auth.models.RoomModel
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class AuthRepository {
    val collection = "users"

    fun login(userName: String, passWord: String, callBack: MyCallBack<Triple<String, String, String>, String>) {
        FirebaseUtils.instance.get_collection(collection)
            .whereEqualTo("email", userName)
            .whereEqualTo("password", passWord)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    callBack.fail("Tài khoản hoặc mật khẩu không đúng")
                } else {
                    val userDocument = documents.documents[0]
                    val role = userDocument.getString("role").toString()
                    val userId = userDocument.id
                    val roomId = userDocument.getString("idRoom").toString()
                    callBack.success(Triple(role, userId, roomId))
                }
            }
            .addOnFailureListener { exception ->
                callBack.fail("Đăng nhập thất bại: ${exception.message}")
            }
    }

    fun getAvailableRooms(callback: MyCallBack<List<RoomModel>, String>) {
        FirebaseUtils.instance.get_collection("rooms")
            .whereLessThan("available", 4) // Điều kiện lấy phòng có số lượng người < 4
            .get()
            .addOnSuccessListener { documents ->
                val rooms = documents.map { document ->
                    val room = document.toObject(RoomModel::class.java)
                    room.id = document.id
                    room
                }
                callback.success(rooms)
            }
            .addOnFailureListener { e ->
                callback.fail(e.message ?: "Unknown error")
            }
    }

    fun getRoom(roomId: String, callback: MyCallBack<RoomModel?, String>) {
        FirebaseUtils.instance.get_collection("rooms")
            .document(roomId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val room = document.toObject(RoomModel::class.java)
                    callback.success(room)
                } else {
                    callback.fail("Phòng không tồn tại")
                }
            }
            .addOnFailureListener { e ->
                callback.fail(e.message ?: "Lỗi không xác định")
            }
    }

    fun updateRoomAvailability(roomId: String, callback: MyCallBack<Unit, String>) {
        val roomRef = FirebaseUtils.instance.get_collection("rooms").document(roomId)

        roomRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentAvailable = document.getLong("available")?.toInt() ?: 0

                    // Kiểm tra nếu phòng đã đầy trước khi cập nhật
                    if (currentAvailable >= 4) {
                        callback.fail("Phòng đã đầy")
                        return@addOnSuccessListener
                    }
                    val updatedAvailable = currentAvailable + 1

                    if (updatedAvailable > 4) {
                        callback.fail("Phòng đã đầy")
                        return@addOnSuccessListener
                    }

                    val updates = mutableMapOf<String, Any>("available" to updatedAvailable)

                    // Nếu available == 4 sau khi cập nhật, thay đổi trạng thái thành "Hết chỗ"
                    if (updatedAvailable == 4) {
                        updates["status"] = "Hết chỗ"
                    }

                    roomRef.update(updates)
                        .addOnSuccessListener {
                            callback.success(Unit)
                        }
                        .addOnFailureListener { e ->
                            callback.fail(e.message ?: "Lỗi khi cập nhật thông tin phòng")
                        }
                } else {
                    callback.fail("Phòng không tồn tại")
                }
            }
            .addOnFailureListener { e ->
                callback.fail(e.message ?: "Lỗi khi truy vấn phòng")
            }
    }

    fun addUser(user: UserModel, roomId: String, callback: MyCallBack<String, String>) {
        val collectionRef = FirebaseUtils.instance.get_collection(collection)
        val roomRef = FirebaseUtils.instance.get_collection("rooms").document(roomId)
        val batch = FirebaseUtils.instance.firestore.batch() // Khởi tạo một batch

        // Kiểm tra trùng lặp
        collectionRef.whereEqualTo("phoneNumber", user.phoneNumber)
            .get()
            .addOnSuccessListener { phoneDocs ->
                if (!phoneDocs.isEmpty) {
                    callback.fail("Số điện thoại đã tồn tại")
                    return@addOnSuccessListener
                }

                collectionRef.whereEqualTo("email", user.email)
                    .get()
                    .addOnSuccessListener { emailDocs ->
                        if (!emailDocs.isEmpty) {
                            callback.fail("Email đã tồn tại")
                            return@addOnSuccessListener
                        }

                        collectionRef.whereEqualTo("cccd", user.cccd)
                            .get()
                            .addOnSuccessListener { idDocs ->
                                if (!idDocs.isEmpty) {
                                    callback.fail("Căn cước công dân đã tồn tại")
                                    return@addOnSuccessListener
                                }

                                // Không trùng lặp, thêm user vào batch
                                val userDocRef = collectionRef.document(user.phoneNumber!!)
                                batch.set(userDocRef, user)

                                // Cập nhật phòng trong batch
                                roomRef.get()
                                    .addOnSuccessListener { roomDoc ->
                                        if (roomDoc.exists()) {
                                            val currentAvailable = roomDoc.getLong("available")?.toInt() ?: 0
                                            val updatedAvailable = currentAvailable + 1

                                            if (updatedAvailable <= 4) {
                                                val updates = mutableMapOf<String, Any>("available" to updatedAvailable)

                                                // Nếu available == 4 sau khi cập nhật, thay đổi trạng thái thành "Hết chỗ"
                                                if (updatedAvailable == 4) {
                                                    updates["status"] = "Hết chỗ"
                                                }

                                                batch.update(roomRef, updates) // Thêm cập nhật phòng vào batch

                                                // Thực thi batch
                                                batch.commit()
                                                    .addOnSuccessListener {
                                                        callback.success("Người dùng đã được thêm thành công và phòng đã được cập nhật!")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        callback.fail("Thêm người dùng và cập nhật phòng thất bại: ${e.message}")
                                                    }
                                            } else {
                                                callback.fail("Phòng đã đầy")
                                            }
                                        } else {
                                            callback.fail("Phòng không tồn tại")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        callback.fail("Lỗi khi lấy thông tin phòng: ${e.message}")
                                    }
                            }
                            .addOnFailureListener { e ->
                                callback.fail("Lỗi khi kiểm tra căn cước công dân: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        callback.fail("Lỗi khi kiểm tra email: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                callback.fail("Lỗi khi kiểm tra số điện thoại: ${e.message}")
            }
    }






}