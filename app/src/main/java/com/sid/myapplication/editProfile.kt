package com.sid.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class editProfile : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uName: String? = intent.getStringExtra("UserName")
        val uEmail: String? = intent.getStringExtra("UserEmail")
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        findViewById<EditText>(R.id.userName1).setText(uName!!)
        findViewById<TextView>(R.id.userEmail1).setText(uEmail!!)
        saveBtn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val userID = auth.currentUser!!.uid
            database = Firebase.database
            val myRef = database.reference
            myRef.child("Admins").child(userID).child("adminName")
                .setValue(findViewById<EditText>(R.id.userName1).text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this@editProfile, "Saved!!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@editProfile, it.toString(), Toast.LENGTH_SHORT).show()
                }


        }
    }
}