package com.sid.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var auth: FirebaseAuth
    private lateinit var myAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        database = Firebase.database
        val userID = auth.currentUser!!.uid
        val myRef = database.getReference("Data").child(userID)

        val cycleList: ArrayList<cycleData> = ArrayList()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        // i = 1,2,3..
                        var cycleName: String = ""
                        var cycleDesc: String = ""
                        var cycleUrl: String = ""
                        for (j in i.children) {
                            if (j.key.toString() == "CycleName") cycleName = j.value.toString()
                            if (j.key.toString() == "CycleDesc") cycleDesc = j.value.toString()
                            if (j.key.toString() == "CycleUrl") cycleUrl = j.value.toString()
                        }
                        if (!cycleList.contains(cycleData(cycleName, cycleDesc, cycleUrl))) {
                            cycleList.add(cycleData(cycleName, cycleDesc, cycleUrl))
                        }

                    }
                    val layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerView = findViewById(R.id.RecyclerView)
                    recyclerView.layoutManager = layoutManager
                    myAdapter = MyAdapter(cycleList,this@MainActivity)
                    recyclerView.adapter = myAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}