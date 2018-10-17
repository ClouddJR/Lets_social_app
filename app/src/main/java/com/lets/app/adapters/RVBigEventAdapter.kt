package com.lets.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lets.app.R
import com.lets.app.model.Event
import com.lets.app.repositories.UserRepository
import org.jetbrains.anko.bundleOf

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(event: Event) {

            itemView.setOnClickListener {
                openEventDetailFragment(it.findNavController(), event)
            }

            itemView.findViewById<TextView>(R.id.hostName).text = event.ownerName
            itemView.findViewById<TextView>(R.id.eventTitle).text = event.title

            Glide.with(itemView.context)
                    .load(UserRepository.buildCustomUserImageURL(event.owner))
                    .into(itemView.findViewById(R.id.profile_image))

            itemView.findViewById<TextView>(R.id.peopleRemaining).text = if (event.maxPeople != 0) {
                val remainingPeopleText = "${event.joined.size}/${event.maxPeople}"
                remainingPeopleText
            } else {
                "unlimited"
            }
        }

        private fun openEventDetailFragment(navController: NavController, event: Event) {
            val bundle = bundleOf("eventId" to event.id,
                    "eventOwner" to event.owner)
            navController.navigate(R.id.eventAction, bundle)
        }

    }
}