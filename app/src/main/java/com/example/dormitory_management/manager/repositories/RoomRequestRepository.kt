package com.example.dormitory_management.manager.repositories

import android.util.Log
import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.manager.models.RoomRequestModel
import com.example.dormitory_management.manager.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RoomRequestRepository {

    fun fetchUserRoomRequestsWithRoomNumbers(callback: MyCallBack<List<RoomRequestModel>, String>) {
        FirebaseUtils.instance.get_collection("users").whereEqualTo("status", "Inactive")
            .get()
            .addOnSuccessListener { userDocuments ->
                val roomRequest = mutableListOf<RoomRequestModel>()
                val tasks = mutableListOf<Task<Void>>()

                for (document in userDocuments) {
                    val user = document.toObject(UserModel::class.java)
                    val roomId = user.idRoom
                    Log.d("USER: ", user.toString())

                    if (!roomId.isNullOrBlank()) {

                        val task = FirebaseUtils.instance.get_collection("rooms")
                            .document(roomId)
                            .get()
                            .continueWithTask { roomTask ->
                                val roomDocument = roomTask.result
                                if (roomDocument != null && roomDocument.exists()) {
                                    val roomNumber = roomDocument.getString("roomNumber")
                                    val buildingNumber = roomDocument.getString("buildingNumber")
                                    roomRequest.add(RoomRequestModel(user, roomNumber, buildingNumber))
                                } else {
                                    Log.e("TAG ROOM", "RoomID: $roomId does not exist.")
                                    roomRequest.add(RoomRequestModel(user, "", ""))
                                }
                                Tasks.forResult<Void>(null)
                            }
                            .addOnFailureListener { exception ->
                                Log.e("TAG FAIL", "Error fetching RoomID: $roomId - ${exception.message}")
                                roomRequest.add(RoomRequestModel(user, "", ""))
                            }

                        tasks.add(task) // Thêm Task<Void> vào danh sách
                    } else {
                        roomRequest.add(RoomRequestModel(user, "", ""))
                    }
                }

                // Chờ tất cả các Task hoàn thành
                Tasks.whenAllComplete(tasks).addOnCompleteListener {
                    callback.success(roomRequest)
                }
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to fetch user room requests: ${exception.message}")
            }
    }



    fun updateUserStatus(userId: String, status: String, callback: MyCallBack<Unit, String>) {
        FirebaseUtils.instance.get_collection("users").document(userId)
            .update("status", status)
            .addOnSuccessListener {
                callback.success(Unit)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to update user status: ${exception.message}")
            }
    }

    fun deleteUser(user: UserModel, callback: MyCallBack<Unit, String>) {
        FirebaseUtils.instance.get_collection("users").document(user.phoneNumber!!)
            .delete()
            .addOnSuccessListener {
                updateRoomAvailabilityAfterDelete(user.idRoom!!, object : MyCallBack<Unit, String> {
                    override fun success(data: Unit) {
                        callback.success(Unit) // Trả về thành công khi cả xóa người dùng và cập nhật phòng hoàn tất
                    }

                    override fun fail(message: String) {
                        callback.fail("Failed to update room availability: $message")
                    }
                })
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to delete user: ${exception.message}")
            }
    }

    fun updateRoomAvailabilityAfterDelete(roomId: String, callback: MyCallBack<Unit, String>) {
        val roomRef = FirebaseUtils.instance.get_collection("rooms").document(roomId)

        roomRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentAvailable = document.getLong("available")?.toInt() ?: 0
                    if (currentAvailable > 0) {
                        val updatedAvailable = currentAvailable - 1
                        val updatedStatus = "Còn chỗ"
                        val updates = mutableMapOf<String, Any>(
                            "available" to updatedAvailable,
                            "status" to updatedStatus
                        )

                        // Cập nhật phòng sau khi xóa người dùng
                        roomRef.update(updates)
                            .addOnSuccessListener {
                                callback.success(Unit)
                            }
                            .addOnFailureListener { e ->
                                callback.fail("Error when updating room availability: ${e.message}")
                            }

                    } else {
                        callback.fail("Room is already empty")
                    }
                } else {
                    callback.fail("Room does not exist")
                }
            }
            .addOnFailureListener { e ->
                callback.fail("Error when fetching room data: ${e.message}")
            }
    }

    fun sendEmail(to: String, subject: String, body: String) {
        val from = "tuannguyen23823@gmail.com" // Địa chỉ email của bạn
        val password = "vhfe qamz kahs xjhb" // Mật khẩu email của bạn

        val properties = System.getProperties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "465")
            put("mail.smtp.auth", "true")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.socketFactory.fallback", "false")
        }

        val session = Session.getInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(from, password)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(from))
                addRecipient(Message.RecipientType.TO, InternetAddress(to))
                setSubject(subject)
                setText(body)
            }

            Thread {
                try {
                    Transport.send(message)
                    // Email đã gửi thành công
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Lỗi khi gửi email
                }
            }.start()

        } catch (e: Exception) {
            e.printStackTrace()
            // Lỗi khi tạo email
        }
    }

    // Hàm lấy chi tiết phòng và gửi email
    fun fetchRoomDetailsAndSendEmail(roomId: String?, userEmail: String?, callback: MyCallBack<Unit, String>) {
        if (roomId == null || userEmail == null) {
            callback.fail("Room ID or user email is null")
            return
        }

        FirebaseUtils.instance.get_collection("rooms").document(roomId)
            .get()
            .addOnSuccessListener { document ->
                val room = document.toObject(RoomModel::class.java)

                if (room != null) {
                    // Gửi email với thông tin phòng
                    val roomDetails = """
                        Room Number: ${room.roomNumber}
                        Building Number: ${room.buildingNumber}
                        Price: ${room.price}
                        Status: ${room.status}
                    """.trimIndent()

                    val emailSubject = "Room Assignment Approved"
                    val emailBody = "Dear $userEmail,\n\nYour room request has been approved. Below are the details:\n\n$roomDetails"

                    sendEmail(userEmail, emailSubject, emailBody)

                    callback.success(Unit)
                } else {
                    callback.fail("Room not found")
                }
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to fetch room details: ${exception.message}")
            }
    }
}