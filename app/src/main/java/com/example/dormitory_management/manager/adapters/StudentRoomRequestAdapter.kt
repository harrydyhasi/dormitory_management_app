package com.example.dormitory_management.manager.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RoomRequestModel
import com.example.dormitory_management.manager.models.UserModel

class StudentRoomRequestAdapter(
    private val roomRequests: List<RoomRequestModel>,
    private val onAction: (RoomRequestModel, String) -> Unit
) : RecyclerView.Adapter<StudentRoomRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room_request, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roomRequest = roomRequests[position]


        // Bind data to the UI elements
        holder.tvStudentName.text = roomRequest.student?.fullName ?: "N/A"
        holder.tvCccd.text = roomRequest.student?.cccd ?: "N/A"
        holder.tvPhoneNumber.text = roomRequest.student?.phoneNumber ?: "N/A"
        holder.tvEmail.text = roomRequest.student?.email ?: "N/A"
        holder.tvStatus.text = "Chưa duyệt"
        holder.tvRoomNumber.text = roomRequest.roomNumber
        holder.tvBuildingNumber.text = roomRequest.buildingNumber

        // Set up button listeners
        holder.btnApprove.setOnClickListener {
            onAction(roomRequest, "approve")
        }

        holder.btnReject.setOnClickListener {
            onAction(roomRequest, "reject")
        }
    }

    override fun getItemCount(): Int = roomRequests.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStudentName: TextView = view.findViewById(R.id.tv_student_name)
        val tvCccd: TextView = view.findViewById(R.id.tv_cccd)
        val tvPhoneNumber: TextView = view.findViewById(R.id.tv_phone_number)
        val tvEmail: TextView = view.findViewById(R.id.tv_email)
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val tvRoomNumber: TextView = view.findViewById(R.id.tv_room_number_request)
        val tvBuildingNumber: TextView = view.findViewById(R.id.tv_building_number_request)
        val btnApprove: Button = view.findViewById(R.id.btn_approve)
        val btnReject: Button = view.findViewById(R.id.btn_reject)
    }
}
