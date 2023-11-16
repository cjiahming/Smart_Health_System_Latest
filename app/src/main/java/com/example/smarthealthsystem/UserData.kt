package com.example.smarthealthsystem

import java.io.Serializable

data class UserData(
    val userID: String? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null): Serializable{
}
