
package com.example.dormitory_management.student.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.adapters.ElectricWaterBillAdapter
import com.example.dormitory_management.student.models.ElectricWaterBillModel
import com.example.dormitory_management.student.models.StudentRoomModel
import com.example.dormitory_management.student.repositories.ElectricWaterBillRepository
import com.example.dormitory_management.student.repositories.StudentRoomRepository


class ElectricWaterBillFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val billRepository = ElectricWaterBillRepository()
    private val roomRepository = StudentRoomRepository()
    private lateinit var progressDialog: ProgressDialog
    private var roomId: String? = null // Declare a variable to hold roomId

    // Create a newInstance method to pass arguments to the fragment
    companion object {
        private const val ROOM_ID_KEY = "ROOM_ID"
        private const val REQUEST_STATUS = 1000


        fun newInstance(roomId: String): ElectricWaterBillFragment {
            val fragment = ElectricWaterBillFragment()
            val args = Bundle()
            args.putString(ROOM_ID_KEY, roomId) // Pass roomId to the fragment
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_bill, container, false)
        progressDialog = ProgressDialog(context).apply {
            setCancelable(false)
        }
        progressDialog.show()

        // Retrieve roomId from arguments
        roomId = arguments?.getString(ROOM_ID_KEY)

        if (roomId != null) {
            recyclerView = view.findViewById(R.id.rcv_list_bill)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            loadRoomAndBills()
        } else {
            Toast.makeText(context, "Room ID is missing", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadRoomAndBills() {
        roomRepository.getRoomById(roomId ?: "", object : MyCallBack<StudentRoomModel, String> {
            override fun success(room: StudentRoomModel) {
                loadBills(room.buildingNumber.toString(), room.roomNumber.toString())
                progressDialog.dismiss()
            }

            override fun fail(param: String) {
                Log.d("Bill", "Không thể lấy thông tin: $param")
                progressDialog.dismiss()
            }
        })
    }

    private fun loadBills(building: String, room: String) {
        billRepository.loadAllElectricWaterBills(roomId ?: "", object : MyCallBack<List<ElectricWaterBillModel>, String> {
            override fun success(bills: List<ElectricWaterBillModel>) {
                val adapter = ElectricWaterBillAdapter(bills, building, room)
                recyclerView.adapter = adapter
            }

            override fun fail(param: String) {
                Log.d("Bill", "Không thể lấy thông tin: $param")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ElectricWaterBillFragment.REQUEST_STATUS && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d("TAG", "onActivityResult called, reloading bills")
            loadRoomAndBills()  // This will reload the list of bills in the fragment
        }
    }

}
