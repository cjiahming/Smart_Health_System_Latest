package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.smarthealthsystem.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = UserDatabaseHelper(this)

        binding.btnLogin.setOnClickListener {
            val loginUsername = binding.editTextUsername.text.toString()
            val loginPassword = binding.editTextPassword.text.toString()
            loginDatabase(loginUsername, loginPassword)
        }

        binding.textViewForgotPassword.setOnClickListener {

        }

        binding.textViewNoAccount.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginDatabase(username: String, password: String){
        val userExists = databaseHelper.readUser(username, password)
        val userRole = databaseHelper.getUserByUsername(username)

        if (userExists && (userRole.role.toString() == "Admin")){
            Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityCrudMain::class.java)
            startActivity(intent)
            finish()
        }
        else if(userExists && (userRole.role.toString() == "User")){
            Toast.makeText(this, "User", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}