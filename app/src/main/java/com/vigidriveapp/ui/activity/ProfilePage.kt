package com.vigidriveapp.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.vigidriveapp.R
import com.vigidriveapp.model.response.DriverResponse
import com.vigidriveapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProfilePage : Fragment() {

    private val activity: Fragment = this@ProfilePage
    private var token: String? = null
    private var userId: Long? = null

    private var firstName: TextView? = null
    private var lastName: TextView? = null
    private var email: TextView? = null
    private var date: TextView? = null
    private var phoneNumber: TextView? = null
    private var emergencyNumber: TextView? = null

    private val apiRepository = ApiRepositoryImpl()
    private var editUserButton: MaterialButton? = null
    private var signout: TextView? = null
    private var avatar: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_profile_page, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        userId = MainPage.getUserId()
        token = MainPage.getToken()

        firstName = v.findViewById(R.id.firstNameText)
        lastName = v.findViewById(R.id.lastNameText)
        email = v.findViewById(R.id.emailText)
        date = v.findViewById(R.id.dateOfBirthText)
        phoneNumber = v.findViewById(R.id.phoneText)
        emergencyNumber = v.findViewById(R.id.emergencyText)

        editUserButton = v.findViewById(R.id.editUserButton)
        signout = v.findViewById(R.id.singOut)

        avatar = v.findViewById(R.id.avatar)

        editUserButton.let { e ->
            e!!.setOnClickListener {
                editUser()
            }
        }

        signout.let { s ->
            s!!.setOnClickListener {
                logout()
            }
        }

        getUser()
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
                        Log.d("driver", user.toString())
                        fillData(user)
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<DriverResponse>, t: Throwable) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fillData(user: DriverResponse) {
        var date0 = "Date of birth: - "
        var phone = "Phone number: - "
        var emergency = "Emergency number: - "

        if (user.dateOfBirth != null) {
            var date1 = LocalDate.parse(user.dateOfBirth)
            date0 = "Date of birth: " + date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))
        }

        if (user.phoneNumber != null) {
            phone = "Phone number: " + user.phoneNumber
        }

        if (user.emergencyContact != null) {
            emergency = "Emergency number: " + user.emergencyContact
        }

        firstName!!.text = user.firstName
        lastName!!.text = user.lastName
        email!!.text = "Email: " + user.email
        date!!.text = date0
        phoneNumber!!.text = phone
        emergencyNumber!!.text = emergency

        val imageBytes = Base64.decode(user.avatar, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatar!!.setImageBitmap(decodedImage)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePage().apply {
                arguments = Bundle().apply {

                }
            }
    }

    fun editUser() {
        val intent = Intent(activity.context, EditProfilePage::class.java)
        startActivity(intent)
    }

    fun logout() {
        val intent = Intent(activity.context, LoginPage::class.java)
        startActivity(intent)
        activity.requireParentFragment().requireActivity().finish()
    }
}