package com.vigidriveapp.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vigidriveapp.R
import com.vigidriveapp.model.request.DriverRequest
import com.vigidriveapp.network.ApiRepositoryImpl
import com.vigidriveapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URISyntaxException

class RegisterPage : AppCompatActivity() {
    private val activity: AppCompatActivity = this@RegisterPage
    private val apiRepository = ApiRepositoryImpl()
    private val validator = Validator()
    private var emailInput: TextInputEditText? = null
    private var passwordInput: TextInputEditText? = null
    private var avatar: ImageView? = null
    private var firstNameInput: TextInputEditText? = null
    private var lastNameInput: TextInputEditText? = null
    private var emailContainer: TextInputLayout? = null
    private var passwordContainer: TextInputLayout? = null
    private var firstNameContainer: TextInputLayout? = null
    private var lastNameContainer: TextInputLayout? = null

    private val PICK_IMAGE = 1
    var avatarUri: String? = null
    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        init()
    }

    private fun init() {
        emailInput = findViewById<TextInputEditText>(R.id.emailEditText)
        passwordInput = findViewById<TextInputEditText>(R.id.passwordEditText)
        firstNameInput = findViewById<TextInputEditText>(R.id.firstNameEditText)
        lastNameInput = findViewById<TextInputEditText>(R.id.lastNameEditText)
        avatar = findViewById<ImageView>(R.id.avatar)

        emailContainer = findViewById<TextInputLayout>(R.id.emailContainer)
        passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)
        firstNameContainer = findViewById<TextInputLayout>(R.id.firstNameContainer)
        lastNameContainer = findViewById<TextInputLayout>(R.id.lastNameContainer)

        avatar.let { a ->
            a!!.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

        validate()

    }

    private fun validate() {
        emailInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                emailContainer?.let { c -> c.helperText = validateEmail() }
            }
        }
        passwordInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                passwordContainer?.let { c -> c.helperText = validatePassword() }
            }
        }
        firstNameInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                firstNameContainer?.let { c -> c.helperText = validateFirstName() }
            }
        }
        lastNameInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                lastNameContainer?.let { c -> c.helperText = validateLastName() }
            }
        }

    }

    private fun validateEmail(): String {
        return validator.validateEmail(emailInput?.text.toString().trim())
    }

    private fun validatePassword(): String {
        return validator.validatePassword(passwordInput?.text.toString().trim())
    }

    private fun validateFirstName(): String {
        return validator.validateFirstName(firstNameInput?.text.toString().trim())
    }

    private fun validateLastName(): String {
        return validator.validateLastName(lastNameInput?.text.toString().trim())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val selectedImageUri = data?.data
            var uri: URI? = null
            try {
                uri = URI(selectedImageUri.toString())
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            avatar!!.setImageURI(selectedImageUri)


            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)

            image = convertBitmapToBase64(bitmap)

            avatarUri = uri.toString()
            activity.contentResolver
                .takePersistableUriPermission(
                    selectedImageUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // Adjust quality as needed
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun register(view: View) {

        val driver = DriverRequest(
            firstNameInput?.text.toString().trim(),
            lastNameInput?.text.toString().trim(),
            image!!,
            emailInput?.text.toString().trim(),
            passwordInput?.text.toString().trim()
        )

        Log.d("Driver", driver.toString())

        apiRepository.register(driver,
            object :
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (!response.isSuccessful) {
                        Log.d("Error1", response.code().toString())
                        AlertDialog.Builder(activity)
                            .setTitle("Sing up")
                            .setMessage("Something went wrong. Try again later.")
                            .setPositiveButton("Okay") { _, _ ->
                                val intent = Intent(activity, LoginPage::class.java)
                                startActivity(intent)
                                activity.finish()
                            }
                            .show()
                    } else {
                        AlertDialog.Builder(activity)
                            .setTitle("Sign up")
                            .setMessage("Registration was successful. You can try to log into your account.")
                            .setPositiveButton("Okay") { _, _ ->
                                val intent = Intent(activity, LoginPage::class.java)
                                startActivity(intent)
                                activity.finish()
                            }
                            .show()
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("Error2", t.message.toString())
                    AlertDialog.Builder(activity)
                        .setTitle("Sing up")
                        .setMessage("Something went wrong. Try again later.")
                        .setPositiveButton("Okay") { _, _ ->
                            val intent = Intent(activity, LoginPage::class.java)
                            startActivity(intent)
                            activity.finish()
                        }
                        .show()
                }
            })

    }
}