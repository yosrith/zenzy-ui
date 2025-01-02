package com.zenzy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameField = findViewById<EditText>(R.id.name)
        val emailField = findViewById<EditText>(R.id.email)
        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(name: String, email: String, username: String, password: String) {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:8080/api/users/signup" // Backend URL

        val json = JSONObject()
        json.put("name", name)
        json.put("email", email)
        json.put("username", username)
        json.put("password", password)

        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignupActivity, "Signup failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SignupActivity, "Signup successful!", Toast.LENGTH_SHORT).show()
                        finish() // Navigate back to the login screen
                    } else {
                        Toast.makeText(this@SignupActivity, "Signup failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
