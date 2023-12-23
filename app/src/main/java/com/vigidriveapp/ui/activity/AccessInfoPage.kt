package com.vigidriveapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vigidriveapp.R
import com.vigidriveapp.model.response.AccessResponse
import com.vigidriveapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccessInfoPage : AppCompatActivity() {

    private val apiRepository = ApiRepositoryImpl()
    private var token: String? = null
    private var userId: Long? = null
    private var accessId: Long? = null
    private var driverEmail: TextView? = null
    private var managerEmail: TextView? = null
    private var duration: TextView? = null
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_info_page)
        init()
    }

    private fun init() {
        getExtra()
        token = MainPage.getToken()
        userId = MainPage.getUserId()

        driverEmail = findViewById(R.id.dremail)
        managerEmail = findViewById(R.id.manemail)
        duration = findViewById(R.id.duration)
        button = findViewById(R.id.accessButton)

        getAccess()
    }


    private fun getAccess() {
        apiRepository.getAccess("Bearer " + token, userId!!, accessId!!, object :
            Callback<AccessResponse> {
            override fun onResponse(
                call: Call<AccessResponse>,
                response: Response<AccessResponse>
            ) {
                if (response.isSuccessful) {
                    val access = response.body()
                    if (access != null) {
                        fillData(access)
                        if (access.isActive) {
                            button!!.setText("Stop")
                            button.let { b ->
                                b!!.setOnClickListener {
                                    stopAccess()
                                }
                            }
                        } else {
                            button!!.setText("Give")
                            button.let { b ->
                                b!!.setOnClickListener {
                                    giveAccess()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@AccessInfoPage,
                            "Something went wrong!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(this@AccessInfoPage, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<AccessResponse>, t: Throwable) {
                Toast.makeText(this@AccessInfoPage, "Something went wrong!", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun fillData(access: AccessResponse) {
        driverEmail!!.setText("Driver email: " + access.driverEmail)
        managerEmail!!.setText("Manager email: " + access.managerEmail)
        duration!!.setText("Duration: " + duration)
    }

    private fun stopAccess() {
        apiRepository.stopAccess("Bearer " + token, userId!!, accessId!!, object :
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@AccessInfoPage, MainPage::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("token", token)
                    startActivity(intent)
                    this@AccessInfoPage.finish()

                } else {
                    Toast.makeText(this@AccessInfoPage, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AccessInfoPage, "Something went wrong!", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun giveAccess() {
        apiRepository.giveAccess("Bearer " + token, userId!!, accessId!!, object :
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@AccessInfoPage, MainPage::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("token", token)
                    startActivity(intent)
                    this@AccessInfoPage.finish()

                } else {
                    Toast.makeText(this@AccessInfoPage, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AccessInfoPage, "Something went wrong!", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getExtra() {
        val arguments = intent.extras
        if (arguments != null) {
            if (arguments.containsKey("accessId")) {
                accessId = arguments.getLong("accessId")
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            val intent = Intent(this, AccessesPage::class.java)
            startActivity(intent)
        }
    }
}