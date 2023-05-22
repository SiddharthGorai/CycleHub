package com.sid.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class verification : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val strEmail = intent.getStringExtra("strEmail")!!.toString()
        val strPass = intent.getStringExtra("strPass")!!.toString()
        val contBtn: Button = findViewById(R.id.cont)

        val cBar: ProgressBar = findViewById(R.id.cBar)
        auth = Firebase.auth
        contBtn.setOnClickListener {
            cBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (auth.currentUser?.isEmailVerified == true) {
                            val intent = Intent(this@verification, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT)
                                .show()
                            cBar.visibility = View.INVISIBLE

                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@verification, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        cBar.visibility = View.INVISIBLE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this@verification, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }


    }
}