package com.example.dormitory_management.manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.manager.models.UserModel

class LookupStudentAdapter(private val userList: List<UserModel>, private val roomMap: Map<String, RoomModel>) : RecyclerView.Adapter<LookupStudentAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lookup_student, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.txtName.text = user.fullName ?: "Unknown"
        holder.txtPhone.text = user.phoneNumber ?: "Unknown"
        holder.txtEmail.text = user.email ?: "Unknown"
        holder.txtCCCD.text = user.cccd ?: "Unknown"
        val room = roomMap[user.idRoom.toString()]
        if (room != null) {
            holder.txtBuilding.text = room.buildingNumber ?: "N/A"
            holder.txtRoom.text = room.roomNumber ?: "N/A"
        } else {
            holder.txtBuilding.text = "N/A"
            holder.txtRoom.text = "N/A"
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    // ViewHolder cho RecyclerView
    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBuilding: TextView = view.findViewById(R.id.txt_building)
        val txtRoom: TextView = view.findViewById(R.id.txt_room)
        val txtName: TextView = view.findViewById(R.id.txt_name)
        val txtPhone: TextView = view.findViewById(R.id.txt_phone)
        val txtEmail: TextView = view.findViewById(R.id.txt_email)
        val txtCCCD: TextView = view.findViewById(R.id.txt_cccd)
    }
}

