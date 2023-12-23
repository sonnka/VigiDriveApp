package com.vigidriveapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vigidriveapp.R
import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.response.LoginResponse
import com.vigidriveapp.network.ApiRepositoryImpl
import com.vigidriveapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {
    private val activity: AppCompatActivity = this@LoginPage
    private val apiRepository = ApiRepositoryImpl()
    private val validator = Validator()
    private var username: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var usernameContainer: TextInputLayout? = null
    private var passwordContainer: TextInputLayout? = null
    private var userId: Long? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        init()
    }

    private fun init() {
        username = findViewById<TextInputEditText>(R.id.loginEditText)
        password = findViewById<TextInputEditText>(R.id.passwordEditText)

        usernameContainer = findViewById<TextInputLayout>(R.id.loginContainer)
        passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)

        username?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                usernameContainer?.let { c -> c.helperText = validateUsername() }
            }
        }
        password?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                passwordContainer?.let { c -> c.helperText = validatePassword() }
            }
        }
    }

    fun registration(view: View) {
        val intent = Intent(activity, RegisterPage::class.java)
        startActivity(intent)
        activity.finish()
    }

    private fun validateUsername(): String {
        return validator.validateEmail(username?.text.toString().trim())
    }

    private fun validatePassword(): String {
        return validator.validatePassword(password?.text.toString().trim())
    }


    fun login(view: View) {

        val user = LoginRequest(username?.text.toString().trim(), password?.text.toString().trim())

        apiRepository.login(
            user,
            object :
                Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            userId = body.id
                            val intent = Intent(activity, MainPage::class.java)
                            intent.putExtra("userId", userId)
                            intent.putExtra("token", body.token)
                            startActivity(intent)
                            activity.finish()
                        } else {
                            AlertDialog.Builder(activity)
                                .setTitle("Sing in")
                                .setMessage("Something went wrong. Try again later.")
                                .setPositiveButton("Okay") { _, _ ->
                                    username!!.setText("")
                                    password!!.setText("")
                                }
                                .show()
                        }
                    } else {
                        Log.d("Error status code", response.code().toString())
                        AlertDialog.Builder(activity)
                            .setTitle("Sing in")
                            .setMessage("Something went wrong. Try again later.")
                            .setPositiveButton("Okay") { _, _ ->
                                username!!.setText("")
                                password!!.setText("")
                            }
                            .show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("Error", t.message.toString())
                    AlertDialog.Builder(activity)
                        .setTitle("Sing in")
                        .setMessage("Something went wrong. Try again later.")
                        .setPositiveButton("Okay") { _, _ ->
                            username!!.setText("")
                            password!!.setText("")
                        }
                        .show()
                }
            })
    }

}