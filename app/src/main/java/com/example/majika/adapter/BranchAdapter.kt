package com.example.majika.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.databinding.ListBranchItemBinding
import com.example.majika.databinding.ListMenuItemBinding
import com.example.majika.model.BranchItem
import com.example.majika.model.MenuItem
import com.example.majika.view.BranchFragment

class BranchAdapter(
    private val context: BranchFragment
) : RecyclerView.Adapter<BranchAdapter.BranchViewHolder>() {
    private var branches: List<BranchItem> = mutableListOf()
    class BranchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val branchName = itemView.findViewById<TextView>(R.id.branch_name_text)
        val branchAddress = itemView.findViewById<TextView>(R.id.branch_address_text)
        val branchPhoneNumber = itemView.findViewById<TextView>(R.id.branch_phone_number_text)
        val branchMapsButton = itemView.findViewById<TextView>(R.id.branch_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        return BranchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_branch_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        val branchItem = branches[position]
        holder.branchName.text = branchItem.name
        holder.branchAddress.text = branchItem.address
        holder.branchPhoneNumber.text = branchItem.phone_number
        holder.branchMapsButton.setOnClickListener{
            val branchLat = branchItem.latitude
            val branchLng = branchItem.longitude
            val branchAddress = branchItem.address.replace(' ', '+')
            val gmmIntentUri = Uri.parse("geo:0,0?q=${branchLat},${branchLng}(${branchAddress})")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
            Log.d("MAPSCLICKED", "${branchLat}")
        }
    }

    override fun getItemCount(): Int {
        return branches.size
    }

    fun setBranches(branchList: List<BranchItem>) {
        branches = branchList.sortedBy { it.name }
    }
}
