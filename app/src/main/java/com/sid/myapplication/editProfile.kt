package com.sid.myapplication

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class editProfile : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun selectImg() {
        val imgIntent = Intent(Intent.ACTION_GET_CONTENT)
        imgIntent.type = "image/*"
        imgIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(imgIntent, 13)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uName: String? = intent.getStringExtra("UserName")
        val uEmail: String? = intent.getStringExtra("UserEmail")
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val picBtn = findViewById<ImageView>(R.id.userImgA)

        findViewById<EditText>(R.id.userName1).setText(uName!!)
        findViewById<TextView>(R.id.userEmail1).setText(uEmail!!)

        picBtn.setOnClickListener{
            selectImg()
        }
        saveBtn.setOnClickListener {
            auth = FirebaseAuth.getInstance()
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
            finish()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

//            13 -> if (resultCode == RESULT_OK) {
//
//                val uri: Uri = data?.data!!
//                val uriString: String = uri.toString()
//                var imgName: String? = null
//
//                if (uriString.startsWith("content://")) {
//                    var myCursor: Cursor? = null
//
//                    try {
//                        myCursor = applicationContext!!.contentResolver.query(
//                            uri, null, null, null, null
//                        )
//                        if (myCursor != null && myCursor.moveToFirst()) {
//                            imgName =
//                                myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                            uploadAlert = AlertDialog.Builder(this)
//                            uploadAlert.setTitle("My Vault")
//                                .setMessage("Do you want to upload $imgName to myvault ?")
//                                .setCancelable(true)
//                                .setPositiveButton("Yes") { dialogInterface, it ->
//                                    mProgressDialog.show()
//                                    uploadImg(uri, imgName,mProgressDialog)
//
//                                }
//                                .setNegativeButton("No") { dialogInterface, it ->
//                                    dialogInterface.cancel()
//                                }
//                                .show()
//                        }
//
//                    } finally {
//                        myCursor?.close()
//                    }
//                }
//
//            }
        }

    }
}