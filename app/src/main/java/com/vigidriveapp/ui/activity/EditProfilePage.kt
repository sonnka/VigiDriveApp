package com.vigidriveapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vigidriveapp.R
import com.vigidriveapp.model.request.UpdateDriverRequest
import com.vigidriveapp.model.response.DriverResponse
import com.vigidriveapp.network.ApiRepositoryImpl
import com.vigidriveapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URISyntaxException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class EditProfilePage : AppCompatActivity() {

    private val activity: AppCompatActivity = this@EditProfilePage
    private var token: String? = null
    private var userId: Long? = null
    private var driver: DriverResponse? = null

    private val apiRepository = ApiRepositoryImpl()
    private var datePickerDialog: DatePickerDialog? = null
    private val validator = Validator()

    private var avatar: ImageView? = null
    private var phoneInput: TextInputEditText? = null
    private var birthDateInput: TextInputEditText? = null
    private var numberInput: TextInputEditText? = null


    private var phoneContainer: TextInputLayout? = null
    private var birthDateContainer: TextInputLayout? = null
    private var numberContainer: TextInputLayout? = null

    private val PICK_IMAGE = 1
    private var avatarUri: String? = null
    private var avatarTemp: ByteArray? = null
    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_page)
        init()
    }

    private fun init() {
        userId = MainPage.getUserId()
        token = MainPage.getToken()

        phoneInput = findViewById<TextInputEditText>(R.id.phone)
        birthDateInput = findViewById<TextInputEditText>(R.id.dateEditText)
        numberInput = findViewById<TextInputEditText>(R.id.number)

        phoneContainer = findViewById<TextInputLayout>(R.id.phoneContainer)
        birthDateContainer = findViewById<TextInputLayout>(R.id.dateContainer)
        numberContainer = findViewById<TextInputLayout>(R.id.numberContainer)
        avatar = findViewById<ImageView>(R.id.avatar)

        avatar.let { a ->
            a!!.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

        getUser()
        validate()

    }

    private fun getUser() {

        apiRepository.getDriver("Bearer " + token, userId!!, object :
            Callback<DriverResponse> {
            override fun onResponse(
                call: Call<DriverResponse>,
                response: Response<DriverResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        driver = user
                        fillData(user)
                    } else {
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(activity, ProfilePage::class.java)
                        startActivity(intent)
                        activity.finish()
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(activity, ProfilePage::class.java)
                    startActivity(intent)
                    activity.finish()
                }
            }

            override fun onFailure(call: Call<DriverResponse>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, ProfilePage::class.java)
                startActivity(intent)
                activity.finish()
            }
        })
    }

    fun fillData(user: DriverResponse) {
        var date1 = ""

        if (user.dateOfBirth != null) {
            date1 = LocalDate.parse(user.dateOfBirth)
                .format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
        }

        image = user.avatar

        phoneInput!!.setText(user.phoneNumber)
        birthDateInput!!.setText(date1)
        numberInput!!.setText(user.emergencyContact)

        val imageBytes = Base64.decode(user.avatar, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatar!!.setImageBitmap(decodedImage)
    }

    private fun validate() {
        phoneInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                phoneContainer?.let { c -> c.helperText = validatePhone() }
            }
        }

        birthDateInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                birthDateContainer?.let { c -> c.helperText = validateDate() }
            }
        }
        numberInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                numberContainer?.let { c -> c.helperText = validatePhoneEm() }
            }
        }
    }

    private fun validatePhone(): String {
        return validator.validatePhone(phoneInput?.text.toString().trim())
    }

    private fun validatePhoneEm(): String {
        return validator.validatePhone(numberInput?.text.toString().trim())
    }

    private fun validateDate(): String {
        return validator.validateDate(birthDateInput?.text.toString().trim())
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

    @SuppressLint("SetTextI18n")
    fun onDate(view: View?) {
        val calendar: Calendar = Calendar.getInstance()
        val mYear: Int = calendar.get(Calendar.YEAR)
        val mMonth: Int = calendar.get(Calendar.MONTH)
        val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(
            activity,
            { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                var day = dayOfMonth.toString()
                if (day.length < 2) {
                    day = "0$day";
                }

                var month = (monthOfYear + 1).toString()
                if (month.length < 2) {
                    month = "0$month";
                }

                birthDateInput?.setText(
                    "$day/$month/$year"
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog!!.show()
    }


    fun update(view: View) {
        val phone = validator.formatPhoneNumber(phoneInput?.text.toString().trim())
        val num = validator.formatPhoneNumber(numberInput?.text.toString().trim())

        val inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val localDate = LocalDate.parse(birthDateInput?.text.toString().trim(), inputFormat)
        val date = localDate.toString()
        if (date == null) {
            birthDateContainer?.helperText = "Invalid date of birth"
        }

        if (phone != null && date != null) {

            val user = UpdateDriverRequest(
                date,
                phone,
                driver!!.firstName.toString(),
                driver!!.lastName.toString(),
                image!!
            )

            Log.d("User", user.toString())

            apiRepository.updateDriver("Bearer " + token, userId!!, user,
                object :
                    Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        if (!response.isSuccessful) {
                            Log.d("Error1", response.code().toString())
                            Toast.makeText(
                                activity,
                                "Something went wrong during updating!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {
                            Toast.makeText(activity, "User is updated!", Toast.LENGTH_LONG)
                                .show()
                            val intent = Intent(activity, MainPage::class.java)
                            intent.putExtra("userId", userId)
                            intent.putExtra("token", token)
                            startActivity(intent)
                            activity.finish()
                        }

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(
                            activity,
                            "Something went wrong during updating!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                })

//            apiRepository.updateEmergencyContact("Bearer " + token, userId!!, num!!,
//                object :
//                    Callback<Void> {
//                    override fun onResponse(
//                        call: Call<Void>,
//                        response: Response<Void>
//                    ) {
//                        if (!response.isSuccessful) {
//                            Log.d("Error1", response.code().toString())
//                            Toast.makeText(
//                                activity,
//                                "Something went wrong during updating!",
//                                Toast.LENGTH_LONG
//                            )
//                                .show()
//                        } else {
//
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<Void>, t: Throwable) {
//                        Toast.makeText(
//                            activity,
//                            "Something went wrong during updating!",
//                            Toast.LENGTH_LONG
//                        )
//                            .show()
//                    }
//                })


        }
    }
}