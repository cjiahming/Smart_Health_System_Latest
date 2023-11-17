package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.smarthealthsystem.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        setContentView(R.layout.activity_main)

        val userData = intent.getSerializableExtra("userData") as? UserData
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemReselectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_detect -> {
                    replaceFragment(FragmentDetection())
                    true
                }
                R.id.bottom_map -> {
                    replaceFragment(FragmentMaps())
                    true
                }
                R.id.bottom_profile -> {
                    val fragmentProfile = FragmentProfile().apply {
                        arguments = Bundle().apply {
                            putSerializable("userData", userData)
                        }
                    }
                    replaceFragment(fragmentProfile)
                    true
                }
                else -> false
            }
        }
        replaceFragment(FragmentDetection())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.logOutToolBar){
            signOut()
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}