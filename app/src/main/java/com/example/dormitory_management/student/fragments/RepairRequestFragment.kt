package com.example.dormitory_management.student.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.student.adapters.RepairRequestAdapter
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.RepairRequestModel
import com.example.dormitory_management.student.repositories.RepairRequestRepository

class RepairRequestFragment : Fragment() {

    private lateinit var repairRequestAdapter: RepairRequestAdapter
    private lateinit var repairRequestRepository: RepairRequestRepository
    private var roomId: String? = null // Declare a variable to hold roomId

    // Create a newInstance method to pass arguments to the fragment
    companion object {
        private const val ROOM_ID_KEY = "ROOM_ID"

        fun newInstance(roomId: String): RepairRequestFragment {
            val fragment = RepairRequestFragment()
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
        val view = inflater.inflate(R.layout.fragment_student_repair_request, container, false)

        repairRequestRepository = RepairRequestRepository()
        repairRequestAdapter = RepairRequestAdapter()

        val repairRequestList: RecyclerView = view.findViewById(R.id.repair_request_list)
        repairRequestList.layoutManager = LinearLayoutManager(context)
        repairRequestList.adapter = repairRequestAdapter

        val createNewRequestButton: Button = view.findViewById(R.id.btn_create_new_request)
        createNewRequestButton.setOnClickListener {
            showRepairRequestDialog()
        }

        // Retrieve roomId from arguments
        roomId = arguments?.getString(ROOM_ID_KEY)

        if (roomId != null) {
            loadRepairRequests()  // Only load repair requests if roomId is available
        } else {
            Toast.makeText(context, "Room ID is missing", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showRepairRequestDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_student_repair_request_form, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.edt_name)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.edt_description)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                addRepairRequest(name, description)
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    private fun addRepairRequest(name: String, description: String) {
        if (roomId != null) {
            repairRequestRepository.addRepairRequest(roomId!!, name, description, object :
                MyCallBack<Unit, String> {
                override fun success(data: Unit) {
                    Toast.makeText(context, "Yêu cầu sửa chữa đã được gửi", Toast.LENGTH_SHORT).show()
                    loadRepairRequests()
                }

                override fun fail(error: String) {
                    Toast.makeText(context, "Lỗi khi gửi yêu cầu: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadRepairRequests() {
        if (roomId != null) {
            repairRequestRepository.getRepairRequests(roomId!!, object :
                MyCallBack<List<RepairRequestModel>, String> {
                override fun success(data: List<RepairRequestModel>) {
                    repairRequestAdapter.setRepairRequests(data)
                }

                override fun fail(error: String) {
                    Toast.makeText(context, "Lỗi khi tải yêu cầu: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
