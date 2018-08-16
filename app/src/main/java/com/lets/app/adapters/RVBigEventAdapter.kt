package com.lets.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lets.app.R
import com.lets.app.model.Event

class RVBigEventAdapter(private val eventsList: List<Event>) : RecyclerView.Adapter<RVBigEventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.big_event_card, parent, false))
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventsList[position])

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(event: Event) {

            itemView.setOnClickListener {
                it.findNavController().navigate(R.id.eventAction)
            }

            itemView.findViewById<TextView>(R.id.hostName).text = event.owner
            itemView.findViewById<TextView>(R.id.eventTitle).text = event.title
            itemView.findViewById<TextView>(R.id.peopleRemaining).text = "3/${event.maxPeople}"
        }

    }
}