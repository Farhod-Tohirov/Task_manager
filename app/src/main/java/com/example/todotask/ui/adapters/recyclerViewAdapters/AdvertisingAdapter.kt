package com.example.todotask.ui.adapters.recyclerViewAdapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todotask.R
import com.example.todotask.utils.inflate
import kotlinx.android.synthetic.main.advertising_page.view.*

class AdvertisingAdapter(private val data: List<Int>) : RecyclerView.Adapter<AdvertisingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.advertising_page))
    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            itemView.advertisingPage.setBackgroundResource(data[adapterPosition])
        }
    }
}