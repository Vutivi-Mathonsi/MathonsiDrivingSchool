package com.example.mathonsidrivingschooldemo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mathonsidrivingschooldemo.model.DateData
import com.example.mathonsidrivingschooldemo.views.DateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatesActivity : AppCompatActivity() {
    lateinit var txtView: TextView
    lateinit var btnDateView: Button
    lateinit var dateList: ArrayList<DateData>
    lateinit var dateAdapter: DateAdapter
    private lateinit var recv: RecyclerView
    private val c = Calendar.getInstance()

    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dates)

        dateList = ArrayList()
        firebaseRef = FirebaseDatabase.getInstance().getReference("test")
        fetchData()

        dateAdapter = DateAdapter(this, dateList)
        recv = findViewById(R.id.recycler)
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = dateAdapter
//        txtView = findViewById(R.id.dateView)
        btnDateView = findViewById(R.id.selectDateButton)


        btnDateView.setOnClickListener {
            showDatePicker()
        }

        /**Navigation to a different page*/
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.bookings
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.bookings -> return@setOnItemSelectedListener true
                R.id.upload -> {
                    Intent(this, UploadDocumentsActivity::class.java).also {
                        ContextCompat.startActivity(this, it, null)
                    }
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.home -> {
                    Intent(this, MainActivity::class.java).also {
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
                }
            }
            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context,"error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this, {DatePicker, year: Int, month: Int, day: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year,month,day)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateFormatString = SimpleDateFormat("EEEE LLL yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            val formattedDateString = dateFormatString.format(selectedDate.time)

            val dateId = firebaseRef.push().key!!
            val date = DateData(dateId,formattedDate,formattedDateString)
            dateList.add(date)

            //Database and list addition

            firebaseRef.child(dateId).setValue(date)
                .addOnCompleteListener{
                    Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show()
                }
            dateAdapter.notifyDataSetChanged()

        },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH),
        )

        datePickerDialog.show()
    }
}