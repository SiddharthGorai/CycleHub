package com.sid.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class editProfile : AppCompatActivity() {
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

        findViewById<EditText>(R.id.userName1).setText(uName!!)
        findViewById<TextView>(R.id.userEmail1).setText(uEmail!!)

    }
}