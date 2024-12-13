package com.example.dormitory_management.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.admin.adapters.RoomAdapter
import com.example.dormitory_management.admin.models.RoomModelIn
import com.example.dormitory_management.admin.models.RoomModelOut
import com.example.dormitory_management.admin.repositories.AdminRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RoomManagementFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomAdapter: RoomAdapter
    private val roomList = mutableListOf<RoomModelOut>()
    private val studentList = mutableListOf<UserModel>()
    private val adminRepository = AdminRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_management, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_rooms)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        roomAdapter = RoomAdapter(roomList, ::onEditRoom, ::onDeleteRoom, studentList)
        recyclerView.adapter = roomAdapter

        val fabAddRoom = view.findViewById<FloatingActionButton>(R.id.fab_add_room)
        fabAddRoom.setOnClickListener { showAddRoomDialog() }

        fetchRoomList()

        return view
    }

    private fun fetchRoomList() {
        adminRepository.getRoomList(object : MyCallBack<List<RoomModelOut>, String> {
            override fun success(data: List<RoomModelOut>) {
                roomList.clear()
                studentList.clear()
                roomList.addAll(data)
                fetchStudentsForRooms()
            }
            override fun fail(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchStudentsForRooms() {
        val roomFetchCount = roomList.size
        var fetchedCount = 0

        for (room in roomList) {
            adminRepository.getStudentsForRoom(room.id ?: "", object : MyCallBack<List<UserModel>, String> {
                override fun success(students: List<UserModel>) {
                    studentList.addAll(students)
                    fetchedCount++
                    if (fetchedCount == roomFetchCount) {
                        roomAdapter.notifyDataSetChanged()
                    }
                }
                override fun fail(message: String) {
                    Toast.makeText(context, "Failed to fetch students: $message", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showAddRoomDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_room, null)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val etRoomNumber = dialogView.findViewById<EditText>(R.id.et_room_number)
        val etBuildingNumber = dialogView.findViewById<EditText>(R.id.et_building_number)
        val etPrice = dialogView.findViewById<EditText>(R.id.et_price)
        val btnAdd = dialogView.findViewById<Button>(R.id.btn_add)

        btnAdd.setOnClickListener {
            val roomNumber = etRoomNumber.text.toString()
            val buildingNumber = etBuildingNumber.text.toString()
            val price = etPrice.text.toString().toLongOrNull() ?: 0L
            val selectedStatus = "Còn chỗ"
            val available = 0

            if (roomNumber.isBlank() || buildingNumber.isBlank() || price <= 0) {
                Toast.makeText(requireContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newRoom = RoomModelIn(roomNumber, buildingNumber, price, selectedStatus, available)
            adminRepository.addRoom(newRoom, object : MyCallBack<String, String> {
                override fun success(data: String) {
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    fetchRoomList()
                    dialog.dismiss()
                }

                override fun fail(message: String) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    fetchRoomList()
                }
            })
        }

        dialog.show()
    }


    private fun onEditRoom(room: RoomModelOut) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_room, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Chỉnh sửa phòng")

        val etRoomNumber = dialogView.findViewById<EditText>(R.id.et_room_number)
        val etBuildingNumber = dialogView.findViewById<EditText>(R.id.et_building_number)
        val etPrice = dialogView.findViewById<EditText>(R.id.et_price)
        val radioGroupStatus: RadioGroup = dialogView.findViewById(R.id.radio_group_status_edit)
        val etAvailable = dialogView.findViewById<EditText>(R.id.et_available)

        // Populate existing room details
        etRoomNumber.setText(room.roomNumber)
        etBuildingNumber.setText(room.buildingNumber)
        etPrice.setText(room.price.toString())
        val selectedStatus = room.status ?: "Còn chỗ"
        val available = room.available ?: 0

        when (selectedStatus) {
            "Còn chỗ" -> radioGroupStatus.check(R.id.rb_available_edit)
            "Hết chỗ" -> radioGroupStatus.check(R.id.rb_full_edit)
        }
        etAvailable.setText(available.toString())

        radioGroupStatus.isEnabled = false
        dialogView.findViewById<RadioButton>(R.id.rb_available_edit).isClickable = false
        dialogView.findViewById<RadioButton>(R.id.rb_full_edit).isClickable = false
        etAvailable.isEnabled = false

        dialogBuilder.setPositiveButton("Lưu") { _, _ ->
            // Get updated values from user input
            val updatedRoom = RoomModelIn(
                roomNumber = etRoomNumber.text.toString(),
                buildingNumber = etBuildingNumber.text.toString(),
                price = etPrice.text.toString().toLongOrNull() ?: room.price,
                status = selectedStatus,
                available = available
            )

            adminRepository.updateRoom(updatedRoom, room.id, object : MyCallBack<String, String> {
                override fun success(data: String) {
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    fetchRoomList()
                }

                override fun fail(message: String) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    fetchRoomList()
                }
            })
        }

        dialogBuilder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.create().show()
    }



    private fun onDeleteRoom(room: RoomModelOut) {
        adminRepository.deleteRoom(room.id!!, object : MyCallBack<String, String> {
            override fun success(data: String) {
                Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                fetchRoomList()
            }

            override fun fail(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
