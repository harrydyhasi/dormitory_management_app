package com.example.dormitory_management.admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.dormitory_management.R
import com.example.dormitory_management.R.id.nav_admin_logout
import com.example.dormitory_management.admin.fragments.RoomManagementFragment
import com.example.dormitory_management.admin.fragments.RoomStatisticsFragment
import com.example.dormitory_management.admin.fragments.UserManagementFragment
import com.example.dormitory_management.auth.LoginActivity
import com.example.dormitory_management.manager.fragments.ManagerApproveRegistrationFragment
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view_admin)
        navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RoomStatisticsFragment())
                .commit()
            supportActionBar?.title = "Thống kê"
            navigationView.setCheckedItem(R.id.nav_admin_statictis)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_admin_statictis -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RoomStatisticsFragment())
                    .commit()
                supportActionBar?.title = "Thống kê"
            }
            R.id.nav_admin_user_management -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UserManagementFragment())
                    .commit()
                supportActionBar?.title = "Quản lý tài khoản"
            }
            R.id.nav_admin_dormitory_management -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RoomManagementFragment())
                    .commit()
                supportActionBar?.title = "Quản lý phòng ở"
            }
            nav_admin_logout -> {
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