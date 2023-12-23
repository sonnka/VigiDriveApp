package com.vigidriveapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vigidriveapp.R

class MainPage : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var bottomNavigationView: BottomNavigationView? = null
    private val activity: BottomNavigationView.OnNavigationItemSelectedListener = this@MainPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        getExtra()

        bottomNavigationView = findViewById(R.id.bottomMenu);

        bottomNavigationView?.let { b ->
            b.setOnNavigationItemSelectedListener(activity)
            b.setSelectedItemId(R.id.info)
            b.setItemIconTintList(null)
        }
    }

    private fun getExtra() {
        val arguments = intent.extras
        if (arguments != null) {
            if (arguments.containsKey("userId")) {
                userId = arguments.getString("userId")
                setUserId(userId!!)
            }
            if (arguments.containsKey("token")) {
                token = arguments.getString("token")
                setToken(token!!)
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }


    companion object {
        private var userId: String? = null
        private var token: String? = null

        fun getUserId(): String? {
            return userId
        }

        fun getToken(): String? {
            Log.d("token", token + " ")
            return token
        }
    }


    private fun setUserId(userIdValue: String) {
        userId = userIdValue
    }

    private fun setToken(tokenValue: String) {
        token = tokenValue
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.accesses -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    AccessesPage()
                ).commit()
                return true
            }

            R.id.profile -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    ProfilePage()
                ).commit()
                return true
            }

            R.id.info -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    InfoPage()
                ).commit()
                return true
            }
        }
        return false
    }
}