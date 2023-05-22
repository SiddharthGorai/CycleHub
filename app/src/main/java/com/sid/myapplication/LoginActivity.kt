package com.sid.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mBar: ProgressBar
    private lateinit var database: FirebaseDatabase


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        supportActionBar?.hide()

        auth = Firebase.auth
        mBar = findViewById(R.id.pBar)


        val signUpTxt: TextView = findViewById(R.id.signUp)
        val logInTxt: TextView = findViewById(R.id.logIN)
        val signInbtn: Button = findViewById(R.id.regButton)
        val logInbtn: Button = findViewById(R.id.lButton)
        val signInLayout: LinearLayout = findViewById(R.id.signInLayout)
        val logInLayout: LinearLayout = findViewById(R.id.logInLayout)
        val forPass: TextView = findViewById(R.id.forgetPass)

        signUpTxt.setOnClickListener {
            signUpTxt.background = resources.getDrawable(R.drawable.switch_trcks, null)
            signUpTxt.setTextColor(resources.getColor(R.color.txtColor, null))
            logInTxt.background = null
            signInLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            logInbtn.visibility = View.GONE
            signInbtn.visibility = View.VISIBLE
            logInTxt.setTextColor(resources.getColor(R.color.yellow, null))

        }

        logInTxt.setOnClickListener {
            logInTxt.background = resources.getDrawable(R.drawable.switch_trcks, null)
            logInTxt.setTextColor(resources.getColor(R.color.txtColor, null))
            signUpTxt.background = null
            logInLayout.visibility = View.VISIBLE
            signInbtn.visibility = View.GONE
            logInbtn.visibility = View.VISIBLE
            signInLayout.visibility = View.GONE
            signUpTxt.setTextColor(resources.getColor(R.color.yellow, null))

        }

        logInbtn.setOnClickListener { login() }
        signInbtn.setOnClickListener { signIn() }
        forPass.setOnClickListener { forgotpassword() }


    }

    private fun forgotpassword() {
        val intent = Intent(this,forgotPassword::class.java)
        startActivity(intent)
    }

    private fun signIn() {
        val inputEmail: EditText = findViewById(R.id.usrEmail)
        val inputPass: EditText = findViewById(R.id.usrPass)
        val inputConPass: EditText = findViewById(R.id.usrConPass)
        val inputUsername: EditText = findViewById(R.id.usrName)


        val sEmail: String = inputEmail.text.toString().trim()
        val sPass: String = inputPass.text.toString().trim()
        val sConPass: String = inputConPass.text.toString().trim()
        val sUsrName: String = inputUsername.text.toString().trim()

        if (sEmail.isEmpty()) {
            inputEmail.error = "Please enter correct email."
            inputEmail.requestFocus()
        } else if (sPass.isEmpty() || sPass.length < 6) {
            inputPass.error = "Please enter password greater than 6 characters."
            inputPass.requestFocus()
        } else if (sUsrName.isEmpty()) {
            inputUsername.error = "Please enter username."
            inputUsername.requestFocus()
        } else if (sConPass != sPass) {
            inputConPass.error = "Password not matching."
            inputConPass.requestFocus()
        } else {
            mBar.visibility = View.VISIBLE
            auth = Firebase.auth

            auth.createUserWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                Toast.makeText(this, "verification sent", Toast.LENGTH_SHORT).show()
                                saveData(sUsrName, sEmail)
                                updateUIsignin(sEmail, sPass)
                                finish()
                                mBar.visibility = View.INVISIBLE

                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("error", it.exception.toString())
                        Toast.makeText(
                            this, "Authentication Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        mBar.visibility = View.GONE
                    }

                }
        }

    }

    private fun updateUIsignin(strEmail: String, strPass: String) {
        val intent = Intent(this@LoginActivity, verification::class.java)
        intent.putExtra("strEmail",strEmail)
        intent.putExtra("strPass",strPass)
        startActivity(intent)
    }

    private fun saveData(strAdminName: String, strEmail: String) {
        database = Firebase.database
        val myRef = database.getReference("Admins")
        val adminID = auth.currentUser!!.uid
        val adminDataList = adminData(strAdminName, strEmail)

        myRef.child(adminID).setValue(adminDataList)

    }

    private fun login() {

        val logEmail: EditText = findViewById(R.id.lEmail)
        val logPass: EditText = findViewById(R.id.lPass)

        val llEmail: String = logEmail.text.toString().trim()
        val llPass: String = logPass.text.toString().trim()

        if (llEmail.isEmpty()) {
            logEmail.error = "Please enter email."
            logEmail.requestFocus()
        }
        else if (llPass.isEmpty()){
            logPass.error = "Please enter password."
            logPass.requestFocus()
        }
        else {
            mBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(llEmail, llPass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val verification = auth.currentUser?.isEmailVerified
                        if (verification == true) {
                            updateUIlogin()
                            finish()

                        } else {
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT)
                                .show()
                            mBar.visibility = View.INVISIBLE

                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        mBar.visibility = View.INVISIBLE
                    }
                }
        }


    }

    private fun updateUIlogin() {
        val intent = Intent(this@LoginActivity,MainActivity::class.java)
        startActivity(intent)
    }
}