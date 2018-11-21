package com.lets.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lets.app.R
import com.lets.app.adapters.RVMessagesEventAdapter.Companion.VIEW_TYPE_MESSAGE_SENT
import com.lets.app.model.Message

class RVInnerMessagesAdapter(private val messagesList: List<Message>, val type: Int) : RecyclerView.Adapter<RVInnerMessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if( type == VIEW_TYPE_MESSAGE_SENT )
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.messages_message_sent_message, parent, false))
        else
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.messages_message_received_message, parent, false))
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messagesList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: Message) {
            itemView.findViewById<TextView>(R.id.text).text = message.text
        }
    }
}