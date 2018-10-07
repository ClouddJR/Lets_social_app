package com.lets.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lets.app.R
import com.lets.app.adapters.RVBigEventAdapter.ViewHolder
import com.lets.app.model.Event

class RVBigEventMapAdapter(private val eventsList: List<Event>, private val width: Int) :
        RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.big_event_card, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.width = width
        view.layoutParams = layoutParams
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginStart = 50
            holder.itemView.layoutParams = layoutParams
        }
        holder.bind(eventsList[position])
    }
}