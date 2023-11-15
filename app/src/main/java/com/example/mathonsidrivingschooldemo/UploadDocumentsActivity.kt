package com.example.mathonsidrivingschooldemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class UploadDocumentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_documents)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.upload
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.upload -> return@setOnItemSelectedListener true
                R.id.bookings -> {
                    Intent(this, DatesActivity::class.java).also {
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
}