package com.example.dormitory_management.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.student.models.UserModel

class UserAdapter(private val userList: List<UserModel>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val fullName: TextView = view.findViewById(R.id.fullName)
        val cccd: TextView = view.findViewById(R.id.cccd)
        val email: TextView = view.findViewById(R.id.email)
        val phoneNumber: TextView = view.findViewById(R.id.phoneNumber)
        val status: TextView = view.findViewById(R.id.status)
        val role: TextView = view.findViewById(R.id.roleID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.fullName.text = user.fullName ?: "Unknown"
        holder.cccd.text = user.cccd ?: "N/A"
        holder.email.text = user.email ?: "N/A"
        holder.phoneNumber.text = user.phoneNumber ?: "N/A"
        holder.status.text = user.status ?: "Inactive"
        holder.role.text = user.role ?: ""
        when (user.role) {
            "admin" -> {
                holder.role.text = "Quản trị viên"
            }
            "manager" -> {
                holder.role.text = "Quản lý"
            }
            "student" -> {
                holder.role.text = "Sinh viên"
            }
            else -> {
                holder.role.text = "Sinh viên"
            }
        }

        when (user.status) {
            "Active" -> {
                holder.status.text = "Đang hoạt động"
            }
            "Inactive" -> {
                holder.status.text = "Ngừng hoạt động"
            }
            else -> {
                holder.status.text = "Đang hoạt động"
            }
        }

        // Set an item click listener to open the EditUserActivity
        holder.itemView.setOnClickListener {
//
        }
    }

    override fun getItemCount(): Int = userList.size
}
