package com.example.smarthealthsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.smarthealthsystem.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User Information")

        binding.btnLogin.setOnClickListener {
            val loginUsername = binding.editTextUsername.text.toString()
            val loginPassword = binding.editTextPassword.text.toString()
            loginFunction(loginUsername, loginPassword)
        }

        binding.textViewForgotPassword.setOnClickListener {
            val intent = Intent(this, ActivityForgotPassword::class.java)
            startActivity(intent)
            finish()
        }

        binding.textViewNoAccount.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginFunction(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)

                            if (userData != null && userData.password == password) {
                                // Check user role and redirect accordingly
                                if (userData.role == "User") {
                                    Toast.makeText(this@ActivityLogin, "Login Successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@ActivityLogin, MainActivity::class.java)
                                    intent.putExtra("userData", userData)
                                    startActivity(intent)
                                    finish()
                                    return
                                } else if (userData.role == "Admin") {
                                    Toast.makeText(this@ActivityLogin, "Login Successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@ActivityLogin, ActivityCrudMain::class.java)
                                    startActivity(intent)
                                    finish()
                                    return
                                }
                            }
                        }
                    }
                    Toast.makeText(this@ActivityLogin, "Login Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@ActivityLogin, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
