package com.example.dormitory_management.student.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.student.models.ReturnRoomRequestModel
import java.text.SimpleDateFormat
import java.util.*

class ReturnRoomRequestAdapter : RecyclerView.Adapter<ReturnRoomRequestAdapter.ReturnRoomRequestViewHolder>() {

    private val returnRoomRequests = mutableListOf<ReturnRoomRequestModel>()

    fun setReturnRoomRequests(requests: List<ReturnRoomRequestModel>) {
        returnRoomRequests.clear()
        returnRoomRequests.addAll(requests)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnRoomRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_return_room_request, parent, false)
        return ReturnRoomRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReturnRoomRequestViewHolder, position: Int) {
        val request = returnRoomRequests[position]
        holder.dateReturnTextView.text = "Ngày trả phòng: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.returnDate)}"

        when (request.status) {
            "pending" -> {
                holder.statusBadge.setBackgroundResource(R.drawable.status_badge_bg)
                holder.statusBadge.text = "Chờ duyệt"
                holder.statusBadge.isSelected = false
            }
            "approved" -> {
                holder.statusBadge.setBackgroundResource(R.drawable.status_badge_bg)
                holder.statusBadge.text = "Đã duyệt"
                holder.statusBadge.isSelected = true
            }
            "rejected" -> {
                holder.statusBadge.setBackgroundResource(R.drawable.status_badge_bg_rd)
                holder.statusBadge.text = "Từ chối"
                holder.statusBadge.isSelected = false
            }
            else -> {
                holder.statusBadge.setBackgroundResource(R.drawable.status_badge_bg_rd)
                holder.statusBadge.text = request.status
                holder.statusBadge.isSelected = true
            }
        }
    }

    override fun getItemCount(): Int = returnRoomRequests.size

    class ReturnRoomRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateReturnTextView: TextView = itemView.findViewById(R.id.txt_return_date)
        val statusBadge: TextView = itemView.findViewById(R.id.txt_status_badge)
    }
}
