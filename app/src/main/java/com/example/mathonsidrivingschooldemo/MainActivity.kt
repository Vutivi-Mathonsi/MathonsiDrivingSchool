package com.example.mathonsidrivingschooldemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mathonsidrivingschooldemo.model.DateData
import com.example.mathonsidrivingschooldemo.views.DateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var dateList: ArrayList<DateData>
    lateinit var dateAdapter: DateAdapter
    lateinit var text: TextView
    private lateinit var firebaseRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateList = ArrayList()
        firebaseRef = FirebaseDatabase.getInstance().getReference("test")
        text = findViewById(R.id.editTextTextPersonName)

        fetchData()
        dateAdapter = DateAdapter(this, dateList)



//

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> return@setOnItemSelectedListener true
                R.id.bookings -> {
                    Intent(this, DatesActivity::class.java).also {
                        ContextCompat.startActivity(this, it, null)
                    }
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.upload -> {
                    Intent(this, UploadDocumentsActivity::class.java).also {
                        ContextCompat.startActivity(this, it, null)
                    }
                }
            }
            false
        }
    }

    /**Fetching data from database*/
    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dateList.clear()
                if (snapshot.exists()){
                    for(dateSnap in snapshot.children){
                        val date = dateSnap.getValue(DateData::class.java)
                        dateList.add(date!!)
                    }
                    dateAdapter.notifyDataSetChanged()
                    text.text = dateList[0].dateForm.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context,"error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}