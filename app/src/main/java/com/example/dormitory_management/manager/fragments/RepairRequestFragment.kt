package com.example.dormitory_management.manager.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.adapters.RepairRequestAdapter
import com.example.dormitory_management.manager.models.RepairRequestModel
import com.example.dormitory_management.manager.repositories.RepairRequestRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack

class RepairRequestFragment : Fragment() {

    private lateinit var repairRequestRepository: RepairRequestRepository
    private lateinit var repairRequestAdapter: RepairRequestAdapter
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_repair_request, container, false)

        repairRequestRepository = RepairRequestRepository(requireContext())
        progressDialog = ProgressDialog(context).apply {
            setMessage("Đang tải ...")
            setCancelable(false)
        }

        repairRequestAdapter = RepairRequestAdapter()

        repairRequestAdapter.setOnApproveClickListener { request ->
            approveRepairRequest(request.id, request.roomId)
        }

        val repairRequestList: RecyclerView = view.findViewById(R.id.manager_repair_request_list)
        repairRequestList.layoutManager = LinearLayoutManager(context)
        repairRequestList.adapter = repairRequestAdapter

        loadAllRepairRequests()

        return view
    }

    private fun loadAllRepairRequests() {
        progressDialog.show()

        repairRequestRepository.loadAllRepairRequests(object : MyCallBack<List<RepairRequestModel>, String> {
            override fun success(param: List<RepairRequestModel>) {
                repairRequestAdapter.setRepairRequests(param)
                Log.d("du lieu", param.toString())
                progressDialog.dismiss()
            }

            override fun fail(param: String) {
                Toast.makeText(context, "Lỗi khi tải yêu cầu sửa chữa: $param", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun approveRepairRequest(requestId: String, roomId: String) {
        repairRequestRepository.approveRepairRequest(requestId, roomId, object : MyCallBack<Unit, String> {
            override fun success(param: Unit) {
                Toast.makeText(context, "Duyệt thành công", Toast.LENGTH_SHORT).show()
                loadAllRepairRequests()
            }

            override fun fail(param: String) {
                Toast.makeText(context, "Lỗi khi duyệt yêu cầu: $param", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
