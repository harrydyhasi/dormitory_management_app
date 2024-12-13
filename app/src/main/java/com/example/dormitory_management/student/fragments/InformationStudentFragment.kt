package com.example.dormitory_management.student.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.dormitory_management.R
import com.example.dormitory_management.student.activities.ChangePasswordStudentActivity
import com.example.dormitory_management.student.adapters.ViewPagerAdapter
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.example.dormitory_management.student.repositories.UserRepository
import com.google.android.material.tabs.TabLayout


class InformationStudentFragment : Fragment() {

    private var userId: String? = null
    private val key = "InformationStudentFragment"

    // Views
    private lateinit var pager: ViewPager
    private lateinit var tab: TabLayout
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView

    // Repository
    private val userRepository: UserRepository = UserRepository()

    // Data
    private var user: UserModel = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the userId passed via arguments
        userId = arguments?.getString("USER_ID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_student_information, container, false)

        // Setup views
        pager = view.findViewById(R.id.viewPager)
        tab = view.findViewById(R.id.tablayouts)
        adapter = ViewPagerAdapter(childFragmentManager)
        txtName = view.findViewById(R.id.name_student)
        txtEmail = view.findViewById(R.id.email_student)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId?.let { id ->
            userRepository.getUserById(
                id,
                object : MyCallBack<UserModel, String> {
                    override fun success(param: UserModel) {
                        user = param
                        loadView(user)
                        Log.d(key, "Successfully retrieved user by ID")
                    }

                    override fun fail(param: String) {
                        Log.d(key, "Failed to retrieve user by ID")
                    }
                }
            )
        } ?: Log.d(key, "User ID is null")

        // Setup action for update password button
        view.findViewById<Button>(R.id.btn_update_pass).setOnClickListener {
            onChangePassword()
        }
    }

    private fun loadView(user: UserModel) {
        txtName.text = user.fullName
        txtEmail.text = user.email
        adapter.clear()
        adapter.addFragment(
            StudentTabCommonFragment(user),
            resources.getString(R.string.infor_student_tab_common)
        )
        adapter.addFragment(
            StudentTabRoomFragment(user),
            resources.getString(R.string.infor_student_tab_stay)
        )
        pager.adapter = adapter
        tab.setupWithViewPager(pager)
    }

    private fun onChangePassword() {
        try {
            val intent = Intent(activity, ChangePasswordStudentActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        } catch (e: Exception) {
            Log.i(key, e.toString())
        }
    }

    companion object {
        fun newInstance(userId: String): InformationStudentFragment {
            val fragment = InformationStudentFragment()
            val args = Bundle()
            args.putString("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }
}
