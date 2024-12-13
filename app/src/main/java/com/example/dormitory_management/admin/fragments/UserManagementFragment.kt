package com.example.dormitory_management.admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.admin.UserAdapter
import com.example.dormitory_management.admin.repositories.AdminRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserManagementFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private val userList = mutableListOf<UserModel>()
    private lateinit var adminRepository: AdminRepository

    lateinit var adminFAB: FloatingActionButton
    lateinit var managerFAB: FloatingActionButton
    lateinit var fabAddUser: FloatingActionButton
    var fabVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_management, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_users)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter

        fabAddUser = view.findViewById(R.id.fab_add_user)
        adminFAB = view.findViewById(R.id.fab_add_admin)
        managerFAB = view.findViewById(R.id.fab_add_manager)


        fabAddUser.setOnClickListener {
//            _handlefabClick()
            showAddUserDialog()
        }


        adminRepository = AdminRepository()

        loadUsersFromFirestore()
        // Inflate the layout for this fragment
        return view
    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_user, null)

        val fullNameInput = dialogView.findViewById<EditText>(R.id.et_user_full_name)
        val cccdInput = dialogView.findViewById<EditText>(R.id.et_user_cccd)
        val emailInput = dialogView.findViewById<EditText>(R.id.et_user_email)
        val phoneInput = dialogView.findViewById<EditText>(R.id.et_user_phone)
        val roleSpinner = dialogView.findViewById<Spinner>(R.id.spinner_role)
//        val statusSpinner = dialogView.findViewById<Spinner>(R.id.spinner_status)
        val addButton = dialogView.findViewById<Button>(R.id.btn_add_user)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        addButton.setOnClickListener {
            val fullName = fullNameInput.text.toString().trim()
            val cccd = cccdInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phoneNumber = phoneInput.text.toString().trim()
            var role = roleSpinner.selectedItem.toString()
            when (roleSpinner.selectedItem.toString()) {
                "Quản trị viên" -> {
                    role = "admin"
                }
                "Quản lý" -> {
                    role = "manager"
                }
                "Sinh viên" -> {
                    role = "student"
                }
                else -> {
                    role = "student"
                }
            }
            val status = "Active"

            // Validate required fields
            if (fullName.isEmpty() || cccd.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new user model
            val newUser = UserModel(
                fullName = fullName,         // Required field
                cccd = cccd,                 // Required field
                email = email,               // Required field
                date = null,                 // Not required, set as null
                gender = null,               // Not required, set as null
                password = "12345",             // Not required, set as null
                phoneNumber = phoneNumber,   // Required field
                idRoom = null,               // Not required, set as null
                dateStart = null,            // Not required, set as null
                dateEnd = null,              // Not required, set as null
                dateReturn = null,           // Not required, set as null
                price = null,                // Not required, set as null
                status = status,             // Required field
                statusReturn = null,         // Not required, set as null
                role = role                  // Required field
            )


            // Add user to Firestore
            adminRepository.addUser(newUser, object : MyCallBack<String, String> {
                override fun success(param: String) {
                    Toast.makeText(requireContext(), "User added successfully", Toast.LENGTH_SHORT).show()
                    loadUsersFromFirestore()
                    dialog.dismiss()
                }

                override fun fail(error: String) {
                    Toast.makeText(requireContext(), "Failed to add user: $error", Toast.LENGTH_SHORT).show()
                }

            } )
        }

        dialog.show()
    }






    private fun _handlefabClick() {
        if (!fabVisible) {
            adminFAB.show()
            managerFAB.show()
            adminFAB.visibility = View.VISIBLE
            managerFAB.visibility = View.VISIBLE
            fabAddUser.setImageDrawable(resources.getDrawable(R.drawable.ic_close))
            fabVisible = true
        } else {
            adminFAB.hide()
            managerFAB.hide()

            // on below line we are changing the
            // visibility of home and settings fab
            adminFAB.visibility = View.GONE
            managerFAB.visibility = View.GONE

            // on below line we are changing image source for add fab
            fabAddUser.setImageDrawable(resources.getDrawable(R.drawable.ic_add))

            // on below line we are changing
            // fab visible to false.
            fabVisible = false
        }
    }

    private fun loadUsersFromFirestore() {
        adminRepository.getUserList(object : MyCallBack<List<UserModel>, String> {
            override fun success(result: List<UserModel>) {

                userList.clear()
                userList.addAll(result)

                // Notify the adapter that the data has been updated
                adapter.notifyDataSetChanged()
            }

            override fun fail(error: String) {

                Toast.makeText(requireContext(), "Failed to load users: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}