package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.smarthealthsystem.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val myButton = findViewById<Button>(R.id.buttonTest)
        val textTest = findViewById<TextView>(R.id.helloTest)

        myButton.setOnClickListener {
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            //replaceFragment(FragmentLogin())
        }

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
                    replaceFragment(FragmentProfile())
                    true
                }
                else -> false
            }
        }
        replaceFragment(FragmentDetection())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}