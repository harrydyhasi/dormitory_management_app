package com.example.dormitory_management.manager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.activities.ListElectricWaterBillActivity
import com.example.dormitory_management.manager.adapters.UpdateElectricWaterBillAdapter
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.student.repositories.RoomRepository

class UpdateElectricWaterBillFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UpdateElectricWaterBillAdapter
    private val roomRepository = RoomRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_electric_water_bill, container, false)

        recyclerView = view.findViewById(R.id.rcv_list_rooms)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = UpdateElectricWaterBillAdapter(emptyList()) { room ->
            navigateToUpdateDetail(room)
        }
        recyclerView.adapter = adapter

        loadRooms()
        return view
    }

    private fun loadRooms() {
        roomRepository.getListRoomBy(object : MyCallBack<List<RoomModel>, String> {
            override fun success(data: List<RoomModel>) {
                adapter.updateData(data)
            }

            override fun fail(message: String) {
                Toast.makeText(context, "Failed to load rooms: $message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToUpdateDetail(room: RoomModel) {
        val intent = Intent(context, ListElectricWaterBillActivity::class.java).apply {
            putExtra("roomId", room.idRoom)
            putExtra("buildingNumber", room.buildingNumber)
            putExtra("roomNumber", room.roomNumber)
        }
        startActivity(intent)
    }

}
