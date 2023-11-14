package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.widget.Toast
import com.example.smarthealthsystem.databinding.ActivityRegisterBinding

class ActivityRegister : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = UserDatabaseHelper(this)

        binding.btnSignUp.setOnClickListener {
            val registerUsername = binding.editTextRegisterUsername.text.toString()
            val registerEmail = binding.editTextRegisterEmail.text.toString()
            val registerPassword = binding.editTextRegisterPassword.text.toString()
            registerDatabase(registerUsername, registerEmail, registerPassword, "User")
        }

        binding.textViewHaveAccount.setOnClickListener {
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerDatabase(username: String, email: String, password: String, role: String){
        val user = User(0, username, email, password, role)
        val insertedRowId = databaseHelper.addUser(user)

        if(insertedRowId != -1L){
            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
        }
    }
}