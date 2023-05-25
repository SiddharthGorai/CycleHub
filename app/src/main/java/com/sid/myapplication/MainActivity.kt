package com.sid.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var signoutAlert: AlertDialog.Builder
    private lateinit var auth: FirebaseAuth
    private lateinit var myAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var uName: String
    private lateinit var uEmail: String

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun changeMenuBar(menu: Menu?) {
        val auth = Firebase.auth
        val userID = auth.currentUser!!.uid
        database = Firebase.database
        val myRef = database.reference
        myRef.child("Admins").child(userID).get().addOnSuccessListener {

            uName = it.child("adminName").value.toString()
            uEmail = it.child("adminEmail").value.toString()

            val dName = menu?.findItem(R.id.dname)
            val dEmail = menu?.findItem(R.id.demail)
            dEmail?.title = uEmail
            dName?.title = uName


        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun doThis() {
        signoutAlert = AlertDialog.Builder(this)
        signoutAlert.setTitle("Alert!")
            .setMessage("Do you want to Sign out ?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialogInterface, it ->
                Firebase.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialogInterface, it ->
                dialogInterface.cancel()
            }.show()

    }

    private fun changePassword(menu: Menu) {

        val uBar = findViewById<ProgressBar>(R.id.uBar)
        uBar.visibility = View.VISIBLE

        val auth = Firebase.auth
        val dEmail = menu.findItem(R.id.demail)
        val uEmail = dEmail.title.toString()

        auth.sendPasswordResetEmail(uEmail.trim())
            .addOnSuccessListener {
                uBar.visibility = View.INVISIBLE
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("CycleHub")
                    .setCancelable(true)
                    .setMessage("A password change link has been sent to your eamil.")
                    .setNegativeButton("Ok") { dialogInterface, it ->
                        dialogInterface.cancel()
                    }.show()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }


    }

    private fun showCycle(cycleNameA: String, cycleDescA: String, cycleUrlA: String) {
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.cycle_popup, null)
        view.findViewById<TextView>(R.id.heading).text = cycleNameA
        view.findViewById<TextView>(R.id.description).text = cycleDescA
        Glide.with(this@MainActivity).load(cycleUrlA)
            .into(
                view.findViewById(R.id.cycleImageA)
            )

        alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(view)
            .setCancelable(true)
            .show()

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout, R.string.nav_open, R.string.nav_close
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView: NavigationView = findViewById(R.id.navView)
        val menu: Menu = navView.menu
        changeMenuBar(menu)
        navView.setNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.signOut -> doThis()
                R.id.changePass -> changePassword(menu)
            }
            true
        }

        val headerView: View = navView.getHeaderView(0)
        val editBtn: ImageView = headerView.findViewById(R.id.penEdit)

        editBtn.setOnClickListener {
            val intent = Intent(applicationContext, editProfile::class.java)
            intent.putExtra("UserName",uName)
            intent.putExtra("UserEmail",uEmail)
            startActivity(intent)
        }
        auth = Firebase.auth
        database = Firebase.database
        val myRef = database.getReference("Data")

        val cycleList: ArrayList<cycleData> = ArrayList()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        // i = 1,2,3..
                        val cycleName = i.child("CycleName").value.toString()
                        val cycleDesc = i.child("CycleDesc").value.toString()
                        val cycleUrl = i.child("CycleUrl").value.toString()
                        if (!cycleList.contains(cycleData(cycleName, cycleDesc, cycleUrl))) {
                            cycleList.add(cycleData(cycleName, cycleDesc, cycleUrl))
                        }

                    }
                    val layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerView = findViewById(R.id.RecyclerView)
                    recyclerView.layoutManager = layoutManager
                    myAdapter = MyAdapter(cycleList, this@MainActivity)
                    recyclerView.adapter = myAdapter

                    myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val cycleNameA =
                                recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.findViewById<TextView>(
                                    R.id.cycleName
                                )?.text.toString()

                            val cycleDescA =
                                recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.findViewById<TextView>(
                                    R.id.cycleDesc
                                )?.text.toString()

                            val cycleUrlA =
                                recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.findViewById<TextView>(
                                    R.id.url
                                )?.text.toString()

                            showCycle(cycleNameA, cycleDescA, cycleUrlA)
                        }
                    })


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }
}