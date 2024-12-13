package com.example.dormitory_management.manager.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.viewpager.widget.ViewPager
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.adapters.ViewPagerAdapter
import com.example.dormitory_management.student.repositories.RoomRepository
import com.google.android.material.tabs.TabLayout


class RoomManagerFragment: Fragment(){
    val key = "InformationStudentFragment"
    // view
    private lateinit var pager: ViewPager
    private lateinit var tab: TabLayout

    // repository
    private val roomRepository: RoomRepository = RoomRepository()

    // data
    private lateinit var adapter : ViewPagerAdapter
    private var rooms: MutableList<RoomModel> = mutableListOf()
//    private var btn_ind: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_room, container, false)
        // view
        pager = view.findViewById(R.id.viewPager)
        tab = view.findViewById(R.id.tablayouts)
        adapter = ViewPagerAdapter(childFragmentManager )
        initView()

        // data
        readListRoom()

        return  view
    }

    private fun initView(){
        // list room
        adapter.clear()
        val roomEmpty = rooms.filter { it.available!! < 4 }
        val roomFull = rooms.filter { it.available!! .toInt() == 4 }
        adapter.addFragment(RoomTab(rooms), "Tất cả (${rooms.size})")
        adapter.addFragment(RoomTab(roomEmpty),  "Còn trống (${roomEmpty.size})")
        adapter.addFragment(RoomTab(roomFull), "Hết chỗ (${roomFull.size})")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)
    }

    private fun readListRoom(){
        roomRepository.getListRoomBy(
            object : MyCallBack<List<RoomModel>, String> {
                override fun success(param: List<RoomModel>) {
                    rooms = param.toMutableList()
                    initView()
                    Log.d(key, "success getListRoomBy ")
                }
                override fun fail(param: String) {
                    Log.d(key, "fail getListRoomBy ")
                }
            }
        )
    }

}