package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.widget.Toast
import com.example.smarthealthsystem.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityRegister : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    //private lateinit var databaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("User Information")

        //databaseHelper = UserDatabaseHelper(this)

        binding.btnSignUp.setOnClickListener {
            val registerUsername = binding.editTextRegisterUsername.text.toString()
            val registerEmail = binding.editTextRegisterEmail.text.toString()
            val registerPassword = binding.editTextRegisterPassword.text.toString()

            if(registerUsername.isNotEmpty() && registerEmail.isNotEmpty() && registerPassword.isNotEmpty()){
                registerDatabase("E002", registerUsername, registerEmail, registerPassword, "User")
            }
            else{
                Toast.makeText(this@ActivityRegister, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
//            registerDatabase(registerUsername, registerEmail, registerPassword, "User")
        }

        binding.textViewHaveAccount.setOnClickListener {
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerDatabase(userID: String, username: String, email: String, password: String, role: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists()){
                    //val id = databaseReference.push().key
                    val userID = "U002"
                    val userData = UserData(userID, username, email, password, role)
                    databaseReference.child(userID!!).setValue(userData)
                    Toast.makeText(this@ActivityRegister, "Register Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ActivityRegister, ActivityLogin::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@ActivityRegister, "User Already Exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ActivityRegister, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun registerDatabase(username: String, email: String, password: String, role: String){
//        val user = User(0, username, email, password, role)
//        val insertedRowId = databaseHelper.addUser(user)
//
//        if(insertedRowId != -1L){
//            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ActivityLogin::class.java)
//            startActivity(intent)
//            finish()
//        }
//        else{
//            Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
//        }
//    }
}