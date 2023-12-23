package com.vigidriveapp.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vigidriveapp.R
import com.vigidriveapp.model.response.DriverResponse
import com.vigidriveapp.model.response.HealthResponse
import com.vigidriveapp.model.response.SituationResponse
import com.vigidriveapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class InfoPage : Fragment() {

    private val apiRepository = ApiRepositoryImpl()
    private var token: String? = null
    private var userId: Long? = null

    private var firstName: TextView? = null
    private var lastname: TextView? = null
    private var stressLevel: TextView? = null
    private var concLevel: TextView? = null
    private var sleepLevel: TextView? = null
    private var generalLevel: TextView? = null

    private var typeSituation: TextView? = null
    private var dateSituation: TextView? = null
    private var description: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_info_page, container, false)
        init(v)
        return v
    }

    fun init(v: View) {
        token = MainPage.getToken()
        userId = MainPage.getUserId()

        firstName = v.findViewById(R.id.firstNameText)
        lastname = v.findViewById(R.id.lastNameText)
        stressLevel = v.findViewById(R.id.stressLevel)
        concLevel = v.findViewById(R.id.concLevel)
        sleepLevel = v.findViewById(R.id.sleepLevel)
        generalLevel = v.findViewById(R.id.generalLevel)
        typeSituation = v.findViewById(R.id.type)
        dateSituation = v.findViewById(R.id.period)
        description = v.findViewById(R.id.description)

        getDriver()

    }

    private fun getDriver() {

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
                        fillDriverData(user)
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

    fun fillDriverData(data: DriverResponse) {
        firstName!!.setText(data.firstName)
        lastname!!.setText(data.lastName)

        getHealthInfo()
        getSituation()
    }

    fun fillHealthData(data: HealthResponse) {
        stressLevel!!.setText("Stress level: " + data.stressLevel + "/10")
        concLevel!!.setText("Concentration level: " + data.concentrationLevel + "/10")
        sleepLevel!!.setText("Sleep level: " + data.sleepinessLevel + "/10")

        if (data.generalStatus!! >= 80) {
            generalLevel!!.setBackgroundColor(Color.GREEN)
        } else if (data.generalStatus!! < 50) {
            generalLevel!!.setBackgroundColor(Color.RED)
        } else {
            generalLevel!!.setBackgroundColor(Color.YELLOW)
        }

        generalLevel!!.setText("General level: " + data.generalStatus + "/100")
    }

    fun fillSituationData(data: SituationResponse) {
        typeSituation!!.setText(data.type)
        dateSituation!!.setText(
            LocalDateTime.parse(data.start)
                .format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss"))
                    + " - " + LocalDateTime.parse(data.end)
                .format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss"))
        )
        description!!.setText(data.description)
    }


    private fun getHealthInfo() {

        apiRepository.getHealthInfo("Bearer " + token, userId!!, object :
            Callback<HealthResponse> {
            override fun onResponse(
                call: Call<HealthResponse>,
                response: Response<HealthResponse>
            ) {
                if (response.isSuccessful) {
                    val health = response.body()
                    if (health != null) {
                        fillHealthData(health)
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<HealthResponse>, t: Throwable) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getSituation() {

        apiRepository.getSituation("Bearer " + token, userId!!, object :
            Callback<SituationResponse> {
            override fun onResponse(
                call: Call<SituationResponse>,
                response: Response<SituationResponse>
            ) {
                if (response.isSuccessful) {
                    val situation = response.body()
                    if (situation != null) {
                        fillSituationData(situation)
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<SituationResponse>, t: Throwable) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoPage().apply {
                arguments = Bundle().apply {

                }
            }
    }
}