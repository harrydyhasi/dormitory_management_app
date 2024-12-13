package com.example.dormitory_management.manager.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.adapters.RoomAdapter
import com.example.dormitory_management.manager.activities.DetailRoomActivity
import com.example.dormitory_management.manager.models.RoomModel

class RoomTab(val list: List<RoomModel>):Fragment() {
    val key = "RoomTab"

    //view
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var rcv_rooms: RecyclerView
    private lateinit var txt_no_data: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_room_tab, container, false)

        //view
        rcv_rooms = view.findViewById(R.id.rcv_rooms)
        txt_no_data = view.findViewById(R.id.txt_no_data)

        // action
        setNoData()

        return  view
    }
    private fun setNoData(){
        if (list.isEmpty()) {
            txt_no_data.isVisible = true
            rcv_rooms.isVisible = false
        } else{
            txt_no_data.isVisible = false
            rcv_rooms.isVisible = true
            initView()
        }
    }


    private fun initView(){
        roomAdapter = RoomAdapter()

        roomAdapter.setOnApproveClickListener { room ->
            // intent
            clickShowDetail(room)
        }

        rcv_rooms.layoutManager = LinearLayoutManager(context)
        rcv_rooms.adapter = roomAdapter

        roomAdapter.setRooms(list)
    }
    private fun clickShowDetail(room:RoomModel){
        try{
            val intent = Intent(activity, DetailRoomActivity::class.java)
            intent.putExtra("room", room)
            startActivity(intent)
        } catch (e: Exception){
            Log.i(key, e.toString())
        }
    }
}