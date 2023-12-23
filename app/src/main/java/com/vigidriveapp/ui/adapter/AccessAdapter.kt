package com.vigidriveapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vigidriveapp.R
import com.vigidriveapp.ui.activity.AccessInfoPage

class AccessAdapter(
    var context: Context?,
    var accessIds: ArrayList<Long>?,
    var managerEmails: ArrayList<String>?,
    var accessDurations: ArrayList<String>?
) : RecyclerView.Adapter<AccessAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("inside0", "")
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.row_access, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.accessId.setText(accessIds!![position].toString())
        holder.managerEmail.setText(managerEmails!![position])
        holder.accessDuration.setText(accessDurations!![position])

        holder.view.setOnClickListener { v ->
            val txt = v.findViewById<TextView>(R.id.accessId)
            val intent = Intent(context, AccessInfoPage::class.java)
            intent.putExtra("accessId", txt.text.toString().toLong())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return accessIds!!.size
    }


    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var accessId: TextView = itemView.findViewById<TextView>(R.id.accessId)
        var managerEmail: TextView = itemView.findViewById<TextView>(R.id.managerEmail)
        var accessDuration: TextView = itemView.findViewById<TextView>(R.id.accessDuration)
        var view: View = itemView

        fun getItemView(): View {
            return view
        }
    }
}