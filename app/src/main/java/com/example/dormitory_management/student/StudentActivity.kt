package com.example.dormitory_management.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.dormitory_management.R
import com.example.dormitory_management.auth.LoginActivity
import com.example.dormitory_management.student.fragments.ElectricWaterBillFragment
import com.example.dormitory_management.student.fragments.InformationStudentFragment
import com.example.dormitory_management.student.fragments.RenewRequestFragment
import com.example.dormitory_management.student.fragments.RepairRequestFragment
import com.example.dormitory_management.student.fragments.ReturnRoomRequestFragment
import com.google.android.material.navigation.NavigationView

class StudentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            val userId = intent.getStringExtra("USER_ID") ?: ""
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InformationStudentFragment.newInstance(userId))
                .commit()
            supportActionBar?.title = "Thông tin sinh viên"
            navigationView.setCheckedItem(R.id.nav_student_information)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val userId = intent.getStringExtra("USER_ID")
        val roomId = intent.getStringExtra("ROOM_ID")
        when (item.itemId) {
            R.id.nav_student_information -> {
                val fragment = InformationStudentFragment.newInstance(userId ?: "")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
                supportActionBar?.title = "Thông tin sinh viên"
            }
            R.id.nav_student_bill -> {
                val fragment = ElectricWaterBillFragment.newInstance(roomId ?: "")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
                    supportActionBar?.title = "Hóa đơn, biên lai"
            }
            R.id.nav_student_repair_request -> {
                val fragment = RepairRequestFragment.newInstance(roomId ?: "")
                supportFragmentManager.beginTransaction().
                        replace(R.id.fragment_container, fragment)
                    .commit()
                supportActionBar?.title = "Yêu cầu sửa chữa"
            }
            R.id.nav_student_return_room_request -> {
                val fragment = ReturnRoomRequestFragment.newInstance(userId ?: "")
                supportFragmentManager.beginTransaction().
                replace(R.id.fragment_container, fragment)
                    .commit()
                supportActionBar?.title = "Yêu cầu trả phòng"
        }
            R.id.nav_student_renew_request -> {
                val fragment = RenewRequestFragment.newInstance(userId ?: "")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit()
                supportActionBar?.title = "Gia hạn phòng ở"
            }
            R.id.nav_student_logout -> {
                Toast.makeText(this, "Log out Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
