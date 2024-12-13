package com.example.dormitory_management.manager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.ReturnRoomRequestModel
import java.text.SimpleDateFormat
import java.util.*

class ReturnRoomRequestAdapter :
    RecyclerView.Adapter<ReturnRoomRequestAdapter.ReturnRoomRequestViewHolder>() {

    private val returnRoomRequests = mutableListOf<ReturnRoomRequestModel>()
    private var onApproveClickListener: ((ReturnRoomRequestModel, Boolean) -> Unit)? = null

    fun setReturnRoomRequests(requests: List<ReturnRoomRequestModel>) {
        returnRoomRequests.clear()
        returnRoomRequests.addAll(requests)
        notifyDataSetChanged()
    }

    fun setOnApproveClickListener(listener: (ReturnRoomRequestModel, Boolean) -> Unit) {
        onApproveClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnRoomRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_return_room_request, parent, false)
        return ReturnRoomRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReturnRoomRequestViewHolder, position: Int) {
        val request = returnRoomRequests[position]
        Log.d("REQUQEST",request.toString() )
        holder.returnDateTextView.text = "Ngày trả phòng: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.returnDate)}"
        holder.fullNameTextView.text = request.fullName
        holder.buildingNumberTextView.text = request.buildingNumber
        holder.roomNumberTextView.text = request.roomNumber
        when (request.status) {
            "pending" -> {
                holder.statusTextView.setBackgroundResource(R.drawable.status_badge_bg)
                holder.statusTextView.text = "Chờ duyệt"
                holder.statusTextView.isSelected = false
            }
            "approved" -> {
                holder.statusTextView.setBackgroundResource(R.drawable.status_badge_bg)
                holder.statusTextView.text = "Đã duyệt"
                holder.statusTextView.isSelected = true
            }
            "rejected" -> {
                holder.statusTextView.setBackgroundResource(R.drawable.status_badge_bg_rd)
                holder.statusTextView.text = "Từ chối"
                holder.statusTextView.isSelected = false
            }
            else -> {
                holder.statusTextView.setBackgroundResource(R.drawable.status_badge_bg_rd)
                holder.statusTextView.text = request.status
                holder.statusTextView.isSelected = true
            }
        }

        holder.btnReject.setOnClickListener {
            onApproveClickListener?.invoke(request, false)
        }
        holder.btnApprove.setOnClickListener {
            onApproveClickListener?.invoke(request, true)
        }
    }

    override fun getItemCount(): Int = returnRoomRequests.size

    class ReturnRoomRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val returnDateTextView: TextView = itemView.findViewById(R.id.txt_return_date)
        val statusTextView: TextView = itemView.findViewById(R.id.txt_status_badge)
        val fullNameTextView: TextView = itemView.findViewById(R.id.txt_fullName)
        val buildingNumberTextView: TextView = itemView.findViewById(R.id.txt_buildingNumber)
        val roomNumberTextView: TextView = itemView.findViewById(R.id.txt_roomNumber)
        val btnReject: Button = itemView.findViewById(R.id.btn_rejected)
        val btnApprove: Button = itemView.findViewById(R.id.btn_approved)
    }
}
