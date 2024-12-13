package com.example.dormitory_management.admin.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.admin.models.RoomModelIn
import com.example.dormitory_management.admin.models.RoomModelOut
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AdminRepository {
    val collection = "users"

    fun getUserList(callback: MyCallBack<List<UserModel>, String>)
    {
        val db = FirebaseUtils.instance.get_collection(collection)
        db.get()
            .addOnSuccessListener { documents ->
                val userList = mutableListOf<UserModel>()
                for (document in documents) {
                    val user = document.toObject(UserModel::class.java)
                    userList.add(user)
                }
                callback.success(userList)
            }
            .addOnFailureListener { exception ->

                callback.fail("Failed to fetch users: ${exception.message}")
            }
    }

    fun addUser(user: UserModel, callback: MyCallBack<String, String>) {
        val userCCCD = user.cccd ?: return callback.fail("User CCCD is null")
        FirebaseUtils.instance.get_collection(collection).document(userCCCD)
            .set(user)
            .addOnSuccessListener {
                callback.success("User added successfully $userCCCD")
            }
            .addOnFailureListener { e ->
                callback.fail("Failed to add user: ${e.message}")
            }
    }



    fun getRoomList(callback: MyCallBack<List<RoomModelOut>, String>) {
        val db = FirebaseUtils.instance.get_collection("rooms")
        db.get()
            .addOnSuccessListener { documents ->
                val roomList = mutableListOf<RoomModelOut>()
                for (document in documents) {
                    val room = document.toObject(RoomModelOut::class.java)
                    room.id = document.id
                    roomList.add(room)
                }
                callback.success(roomList)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to fetch rooms: ${exception.message}")
            }
    }

    fun addRoom(room: RoomModelIn, callback: MyCallBack<String, String>) {
        val db = FirebaseUtils.instance.get_collection("rooms")

        // Kiểm tra xem phòng đã tồn tại chưa
        db.whereEqualTo("roomNumber", room.roomNumber)
            .whereEqualTo("buildingNumber", room.buildingNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Nếu không có phòng trùng lặp, thêm phòng mới
                    db.add(room)
                        .addOnSuccessListener {
                            callback.success("Room added successfully!")
                        }
                        .addOnFailureListener { e ->
                            callback.fail("Failed to add room: ${e.message}")
                        }
                } else {
                    callback.fail("Room already exists in this building!")
                }
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to check room existence: ${exception.message}")
            }
    }


    fun updateRoom(room: RoomModelIn, idRoom: String?, callback: MyCallBack<String, String>) {
        val db = FirebaseUtils.instance.get_collection("rooms")

        db.whereEqualTo("roomNumber", room.roomNumber)
            .whereEqualTo("buildingNumber", room.buildingNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty || (documents.size() == 1 && documents.first().id == idRoom)) {
                    db.document(idRoom ?: "").set(room)
                        .addOnSuccessListener {
                            callback.success("Room updated successfully!")
                        }
                        .addOnFailureListener { exception ->
                            callback.fail("Failed to update room: ${exception.message}")
                        }
                } else {
                    callback.fail("Room with the same number and building already exists!")
                }
            }
            .addOnFailureListener { exception ->
                callback.fail("Error checking room: ${exception.message}")
            }
    }


    fun deleteRoom(roomId: String, callback: MyCallBack<String, String>) {
        val dbRooms = FirebaseUtils.instance.get_collection("rooms")
        val dbUsers = FirebaseUtils.instance.get_collection("users")

        // Lấy thông tin phòng
        dbRooms.document(roomId)
            .get()
            .addOnSuccessListener { roomDocument ->
                if (roomDocument.exists()) {
                    val available = roomDocument.getLong("available")?.toInt() ?: 0

                    // Lấy danh sách sinh viên thuộc phòng
                    dbUsers.whereEqualTo("idRoom", roomId)
                        .get()
                        .addOnSuccessListener { userDocuments ->
                            val inactiveStudents = userDocuments.filter {
                                it.getString("status") == "Inactive"
                            }
                            val inactiveCount = inactiveStudents.size

                            // Kiểm tra điều kiện số lượng sinh viên "inactive" == "available"
                            if (inactiveCount == available) {
                                // Gửi email cho từng sinh viên trước khi xóa
                                inactiveStudents.forEach { userDoc ->
                                    val email = userDoc.getString("email")
                                    val fullName = userDoc.getString("fullName") ?: "Student"
                                    if (!email.isNullOrBlank()) {
                                        val emailSubject = "Room Deletion Notification"
                                        val emailBody = "Dear $fullName,\n\nWe would like to inform you that your room assignment has been deleted. Please contact administration for further assistance."
                                        sendEmail(email, emailSubject, emailBody)
                                    }
                                }

                                // Xóa các sinh viên có idRoom khớp với roomId
                                val batch = FirebaseUtils.instance.firestore.batch()
                                userDocuments.forEach { userDoc ->
                                    batch.delete(dbUsers.document(userDoc.id))
                                }

                                // Xóa phòng
                                batch.delete(dbRooms.document(roomId))

                                // Thực hiện batch commit
                                batch.commit()
                                    .addOnSuccessListener {
                                        callback.success("Phòng và sinh viên liên quan đã được xóa thành công!")
                                    }
                                    .addOnFailureListener { e ->
                                        callback.fail("Xóa thất bại: ${e.message}")
                                    }
                            } else {
                                callback.fail("Không thể xóa phòng: Số lượng sinh viên 'inactive' không khớp với 'available'.")
                            }
                        }
                        .addOnFailureListener { e ->
                            callback.fail("Lỗi khi lấy danh sách sinh viên: ${e.message}")
                        }
                } else {
                    callback.fail("Phòng không tồn tại!")
                }
            }
            .addOnFailureListener { e ->
                callback.fail("Lỗi khi kiểm tra thông tin phòng: ${e.message}")
            }
    }



    fun getStudentsForRoom(roomId: String, callback: MyCallBack<List<UserModel>, String>) {
        val db = FirebaseUtils.instance.get_collection("users")
        db.whereEqualTo("idRoom", roomId) // Tìm tất cả sinh viên có ID phòng khớp
            .get()
            .addOnSuccessListener { documents ->
                val studentList = mutableListOf<UserModel>()
                for (document in documents) {
                    val student = document.toObject(UserModel::class.java)
                    studentList.add(student)
                }
                callback.success(studentList)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to fetch students: ${exception.message}")
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
}