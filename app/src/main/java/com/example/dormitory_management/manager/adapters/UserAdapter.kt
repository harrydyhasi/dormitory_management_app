package com.example.dormitory_management.manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.UserModel

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.txtName.text = user.fullName ?: "Unknown"
        holder.txtPhone.text = user.phoneNumber ?: "Unknown"
        holder.txtEmail.text = user.email ?: "Unknown"
        holder.txtCCCD.text = user.cccd ?: "Unknown"
    }
    fun setData(list: List<UserModel>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txt_name)
        val txtPhone: TextView = view.findViewById(R.id.txt_phone)
        val txtEmail: TextView = view.findViewById(R.id.txt_email)
        val txtCCCD: TextView = view.findViewById(R.id.txt_cccd)
    }
}