package com.example.majika.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.SectionHeader

class SectionHeaderAdapter(private val title: SectionHeader) : RecyclerView.Adapter<SectionHeaderAdapter.SectionHeaderViewHolder>() {

    class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(title: SectionHeader) {
            val headerTitle = itemView.findViewById<TextView>(R.id.headerTitle)
            headerTitle.text = title.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SectionHeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.section_header, parent,
                false
            )
        )

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: SectionHeaderViewHolder, position: Int) =
        holder.bind(title)
}