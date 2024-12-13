package com.example.dormitory_management.student.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.student.models.RenewRequestModel
import java.text.SimpleDateFormat
import java.util.*

class RenewRequestAdapter : RecyclerView.Adapter<RenewRequestAdapter.RenewRequestViewHolder>() {

    private val renewRequests = mutableListOf<RenewRequestModel>()

    fun setRenewRequests(requests: List<RenewRequestModel>) {
        renewRequests.clear()
        renewRequests.addAll(requests)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RenewRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_renew_request, parent, false)
        return RenewRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RenewRequestViewHolder, position: Int) {
        val request = renewRequests[position]
        holder.startDateTextView.text = "Ngày bắt đầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.startDate)}"
        holder.endDateTextView.text = "Ngày kết thúc: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.endDate)}"

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

    override fun getItemCount(): Int = renewRequests.size

    class RenewRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startDateTextView: TextView = itemView.findViewById(R.id.txt_start_date)
        val endDateTextView: TextView = itemView.findViewById(R.id.txt_end_date)
        val statusBadge: TextView = itemView.findViewById(R.id.txt_status_badge)
    }
}
