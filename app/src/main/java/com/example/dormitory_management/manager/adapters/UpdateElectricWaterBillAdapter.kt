package com.example.dormitory_management.manager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RoomModel

class UpdateElectricWaterBillAdapter(
    private var roomList: List<RoomModel>,
    private val onItemClick: (RoomModel) -> Unit
) : RecyclerView.Adapter<UpdateElectricWaterBillAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room_in_update_bill, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.txtBuilding.text = room.buildingNumber
        holder.txtRoom.text = room.roomNumber

        holder.itemView.setOnClickListener { onItemClick(room) }
    }

    override fun getItemCount(): Int = roomList.size

    fun updateData(newRoomList: List<RoomModel>) {
        roomList = newRoomList
        notifyDataSetChanged()
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBuilding: TextView = itemView.findViewById(R.id.txt_building)
        val txtRoom: TextView = itemView.findViewById(R.id.txt_room)
    }
}
