package com.example.dormitory_management.student.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.student.models.RepairRequestModel

class RepairRequestAdapter : RecyclerView.Adapter<RepairRequestAdapter.RepairRequestViewHolder>() {

    private val repairRequests = mutableListOf<RepairRequestModel>()

    fun setRepairRequests(requests: List<RepairRequestModel>) {
        repairRequests.clear()
        repairRequests.addAll(requests)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_repair_request, parent, false)
        return RepairRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepairRequestViewHolder, position: Int) {
        val request = repairRequests[position]
        holder.nameTextView.text = request.name
        holder.descriptionTextView.text = "Mô tả: ${request.description}"

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

    override fun getItemCount(): Int = repairRequests.size

    class RepairRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txt_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txt_description)
        val statusBadge: TextView = itemView.findViewById(R.id.txt_status_badge)
    }
}
