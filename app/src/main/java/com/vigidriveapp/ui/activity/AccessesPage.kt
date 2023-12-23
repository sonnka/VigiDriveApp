package com.vigidriveapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.vigidriveapp.R
import com.vigidriveapp.model.response.AccessResponse
import com.vigidriveapp.network.ApiRepositoryImpl
import com.vigidriveapp.ui.adapter.AccessAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccessesPage : Fragment() {

    private val activity: Fragment = this@AccessesPage
    var tabLayout: TabLayout? = null
    private var token: String? = null
    private var userId: Long? = null
    private var accessIds: ArrayList<Long>? = null
    private var managerEmails: ArrayList<String>? = null
    private var accessDurations: ArrayList<String>? = null

    private var recyclerView: RecyclerView? = null

    private var accessAdapter: AccessAdapter? = null
    private val apiRepository = ApiRepositoryImpl()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccessesPage().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_accesses_page, container, false)
        init(v)
        return v
    }

    fun init(v: View) {
        userId = MainPage.getUserId()
        token = MainPage.getToken()

        accessIds = ArrayList<Long>()
        managerEmails = ArrayList<String>()
        accessDurations = ArrayList<String>()

        tabLayout = v.findViewById(R.id.tabs);

        recyclerView = v.findViewById(R.id.recycle_view)



        displayActiveAccesses()

        selectingTabs();
    }

    private fun setAdapterOnRecycleView() {
        Log.d("adapter", "")
        accessAdapter = AccessAdapter(
            getActivity(), accessIds, managerEmails, accessDurations
        )

        recyclerView!!.adapter = accessAdapter

        recyclerView!!.layoutManager = LinearLayoutManager(getActivity())
    }

    private fun displayActiveAccesses() {

        apiRepository.getActiveAccesses("Bearer " + token, userId!!, object :
            Callback<List<AccessResponse>> {
            override fun onResponse(
                call: Call<List<AccessResponse>>,
                response: Response<List<AccessResponse>>
            ) {
                if (response.isSuccessful) {
                    val dataList = response.body()
                    if (dataList != null) {
                        clearRecycleView()
                        setAdapterOnRecycleView()
                        fillData(dataList)
                        setAdapterOnRecycleView()
                        Log.d("List ", dataList.toString())

                    } else {
                        Toast.makeText(getActivity(), "Something went wrong1!", Toast.LENGTH_LONG)
                            .show()
                    }
                    setAdapterOnRecycleView()
                } else {
                    Log.d("Error2", response.code().toString())
                    Toast.makeText(getActivity(), "Something went wrong!2", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<AccessResponse>>, t: Throwable) {
                Log.d("error3", t.message.toString())
                Toast.makeText(getActivity(), "Something went wrong!3", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayInActiveAccesses() {

        apiRepository.getInActiveAccesses("Bearer " + token, userId!!, object :
            Callback<List<AccessResponse>> {
            override fun onResponse(
                call: Call<List<AccessResponse>>,
                response: Response<List<AccessResponse>>
            ) {
                if (response.isSuccessful) {
                    val dataList = response.body()
                    if (dataList != null) {
                        clearRecycleView()
                        setAdapterOnRecycleView()
                        fillData(dataList)
                        setAdapterOnRecycleView()
                        Log.d("List ", dataList.toString())

                    } else {
                        Toast.makeText(getActivity(), "Something went wrong1!", Toast.LENGTH_LONG)
                            .show()
                    }
                    setAdapterOnRecycleView()
                } else {
                    Log.d("Error2", response.code().toString())
                    Toast.makeText(getActivity(), "Something went wrong!2", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<AccessResponse>>, t: Throwable) {
                Log.d("error3", t.message.toString())
                Toast.makeText(getActivity(), "Something went wrong!3", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun clearRecycleView() {
        accessIds = ArrayList<Long>()
        managerEmails = ArrayList<String>()
        accessDurations = ArrayList<String>()
    }

    @SuppressLint("Range")
    private fun fillData(dataList: List<AccessResponse>) {
        if (dataList.size == 0) {
            Toast.makeText(activity.context, "No data!", Toast.LENGTH_LONG).show()
        } else {
            for (item in dataList) {
                accessIds!!.add(item.id!!)
                managerEmails!!.add(item.managerEmail!!)
                accessDurations!!.add(item.accessDuration!!)
            }
        }
    }


    private fun selectingTabs() {
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        clearRecycleView()
                        displayActiveAccesses()
                        setAdapterOnRecycleView()
                    }

                    1 -> {
                        clearRecycleView()
                        displayInActiveAccesses()
                        setAdapterOnRecycleView()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}