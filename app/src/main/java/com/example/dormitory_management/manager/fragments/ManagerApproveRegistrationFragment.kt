package com.example.dormitory_management.manager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.adapters.StudentRoomRequestAdapter
import com.example.dormitory_management.manager.models.RoomRequestModel
import com.example.dormitory_management.manager.repositories.RoomRequestRepository

class ManagerApproveRegistrationFragment : Fragment(R.layout.fragment_manager_approve_registration) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentRoomRequestAdapter
    private val roomRequestList = mutableListOf<RoomRequestModel>()

    private val repository = RoomRequestRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView_room_requests)
        adapter = StudentRoomRequestAdapter(roomRequestList) { roomRequest, action ->
            handleRequestAction(roomRequest, action)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fetchRoomRequests()
    }

    private fun fetchRoomRequests() {
        repository.fetchUserRoomRequestsWithRoomNumbers(object : MyCallBack<List<RoomRequestModel>, String> {
            @SuppressLint("NotifyDataSetChanged")
            override fun success(param: List<RoomRequestModel>) {

                roomRequestList.clear()
                roomRequestList.addAll(param)
                adapter.notifyDataSetChanged()
            }

            override fun fail(param: String) {
                Toast.makeText(context, "Failed to fetch room requests: $param", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun handleRequestAction(roomRequest: RoomRequestModel, action: String) {
        val userId = roomRequest.student?.phoneNumber // Đảm bảo `id` là thuộc tính trong `UserModel` đại diện cho document ID trong Firestore

        if (userId.isNullOrBlank()) {
            Toast.makeText(context, "User phone number is missing!", Toast.LENGTH_SHORT).show()
            return
        }

        if (action == "approve") {
            // Cập nhật trạng thái thành "Active"
            repository.updateUserStatus(userId, "Active", object : MyCallBack<Unit, String> {
                override fun success(data: Unit) {

                    Toast.makeText(context, "Approved request for ${roomRequest.student?.fullName}", Toast.LENGTH_SHORT).show()
                    val roomId = roomRequest.student
                        .idRoom
                    val email = roomRequest.student.email
                    if (!roomId.isNullOrBlank() && !email.isNullOrBlank()) {
                        repository.fetchRoomDetailsAndSendEmail(roomId, email, object : MyCallBack<Unit, String> {
                            override fun success(data: Unit) {
                                // Đã gửi email thành công
                            }

                            override fun fail(message: String) {
                                Toast.makeText(context, "Failed to send email: $message", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    roomRequestList.remove(roomRequest)
                    adapter.notifyDataSetChanged()
                }

                override fun fail(message: String) {
                    Toast.makeText(context, "Failed to approve: $message", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Xóa người dùng khỏi Firestore
            repository.deleteUser(roomRequest.student, object : MyCallBack<Unit, String> {
                override fun success(data: Unit) {
                    val email = roomRequest.student.email
                    if (!email.isNullOrBlank()) {
                        val emailSubject = "Room Request Rejected"
                        val emailBody = "Dear ${roomRequest.student.fullName},\n\nWe regret to inform you that your room request has been rejected."
                        repository.sendEmail(email, emailSubject, emailBody)
                    }
                    Toast.makeText(context, "Rejected request for ${roomRequest.student.fullName}", Toast.LENGTH_SHORT).show()
                    roomRequestList.remove(roomRequest)
                    adapter.notifyDataSetChanged()
                }

                override fun fail(message: String) {
                    Toast.makeText(context, "Failed to reject: $message", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}