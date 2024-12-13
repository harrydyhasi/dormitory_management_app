package com.example.dormitory_management.auth.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.auth.models.RoomModel

class RoomAdapter(
    private var rooms: List<RoomModel>,
    private val onRoomSelected: (RoomModel) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomNumber: TextView = itemView.findViewById(R.id.txtRoomNumber)
        val buildingNumber: TextView = itemView.findViewById(R.id.txtBuildingNumber)
        val price: TextView = itemView.findViewById(R.id.txtPrice)
        val available: TextView = itemView.findViewById(R.id.txtAvailable)
        val status: TextView = itemView.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room_register, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.roomNumber.text = room.roomNumber
        holder.buildingNumber.text = room.buildingNumber
        holder.price.text = "${room.price} VNĐ"
        holder.available.text = "${room.available}/4"
        holder.status.text =room.status
        holder.itemView.setOnClickListener {
            onRoomSelected(room)
        }

        holder.itemView.setBackgroundColor(
            if (room.isSelected) Color.LTGRAY else Color.WHITE
        )

        holder.itemView.setOnClickListener {
            rooms.forEach { it.isSelected = false }
            room.isSelected = !room.isSelected
            notifyDataSetChanged()

            onRoomSelected(room)
        }
    }

    override fun getItemCount(): Int = rooms.size

    fun updateData(newRooms: List<RoomModel>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}
