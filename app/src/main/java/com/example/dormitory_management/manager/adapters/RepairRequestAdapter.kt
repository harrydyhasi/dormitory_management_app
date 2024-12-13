package com.example.dormitory_management.manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RepairRequestModel
import com.google.android.material.button.MaterialButton

class RepairRequestAdapter : RecyclerView.Adapter<RepairRequestAdapter.RepairRequestViewHolder>() {

    private val repairRequests = mutableListOf<RepairRequestModel>()
    private var onApproveClickListener: ((RepairRequestModel) -> Unit)? = null

    fun setRepairRequests(requests: List<RepairRequestModel>) {
        repairRequests.clear()
        repairRequests.addAll(requests)
        notifyDataSetChanged()
    }

    fun setOnApproveClickListener(listener: (RepairRequestModel) -> Unit) {
        onApproveClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_repair_request, parent, false)
        return RepairRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepairRequestViewHolder, position: Int) {
        val request = repairRequests[position]
        holder.roomTextView.text = request.roomNumber
        holder.nameTextView.text = request.name
        holder.descriptionTextView.text = request.description
        holder.statusTextView.text = "Chờ duyệt"
        holder.buildingTextView.text = request.buildingNumber

        holder.approveButton.setOnClickListener {
            onApproveClickListener?.invoke(request)
        }
    }

    override fun getItemCount(): Int = repairRequests.size

    class RepairRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomTextView: TextView = itemView.findViewById(R.id.txt_room)
        val nameTextView: TextView = itemView.findViewById(R.id.txt_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txt_description)
        val statusTextView: TextView = itemView.findViewById(R.id.txt_status)
        val approveButton: MaterialButton = itemView.findViewById(R.id.btn_approve_request)
        val buildingTextView: TextView = itemView.findViewById(R.id.txt_building)
    }
}
