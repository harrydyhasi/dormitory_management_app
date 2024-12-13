package com.example.dormitory_management.manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RenewRequestModel
import com.example.dormitory_management.manager.models.UserModel
import java.text.SimpleDateFormat
import java.util.*

class RenewRequestAdapter: RecyclerView.Adapter<RenewRequestAdapter.RenewRequestViewHolder>() {

    private val renewRequests = mutableListOf<RenewRequestModel>()
    private var onApproveClickListener: ((RenewRequestModel, Boolean) -> Unit)? = null

    fun setRenewRequests(requests: List<RenewRequestModel>) {
        renewRequests.clear()
        renewRequests.addAll(requests)
        notifyDataSetChanged()
    }

    fun setOnApproveClickListener(listener: (RenewRequestModel, Boolean) -> Unit) {
        onApproveClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RenewRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_renew_request, parent, false)
        return RenewRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RenewRequestViewHolder, position: Int) {
        val request = renewRequests[position]
        holder.startDateTextView.text = "${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.startDate)}"
        holder.endDateTextView.text = "${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.endDate)}"
        holder.txt_name_user.text = request.nameUser
        holder.txt_room_name.text = request.roomNumber
        holder.txt_building_name.text = request.buildingNumber
        holder.btn_rejected.setOnClickListener {
            onApproveClickListener?.invoke(request, false)
        }
        holder.btn_approved.setOnClickListener {
            onApproveClickListener?.invoke(request, true)
        }
    }

    override fun getItemCount(): Int = renewRequests.size

    class RenewRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startDateTextView: TextView = itemView.findViewById(R.id.txt_start_date)
        val endDateTextView: TextView = itemView.findViewById(R.id.txt_end_date)
        val txt_name_user: TextView = itemView.findViewById(R.id.txt_name_user)
        val txt_room_name: TextView = itemView.findViewById(R.id.txt_room_name)
        val txt_building_name: TextView = itemView.findViewById(R.id.txt_building_name)
        val btn_rejected: Button = itemView.findViewById(R.id.btn_rejected)
        val btn_approved: Button = itemView.findViewById(R.id.btn_approved)
    }
}
