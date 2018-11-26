package com.lets.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lets.app.R
import com.lets.app.model.MessagePack
import com.lets.app.repositories.UserRepository

class RVMessagesEventAdapter(private val messagesList: List<MessagePack>) : RecyclerView.Adapter<RVMessagesEventAdapter.MessageViewHolder>() {

    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == VIEW_TYPE_MESSAGE_SENT)
            SentMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.messages_message_sent, parent, false))
        else
            ReceivedMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.messages_message_received, parent, false))
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messagePack = messagesList[position]

        return if (messagePack.authorId == UserRepository.getUserId()) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messagesList[position], position)
    }

    abstract inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(messagePack: MessagePack, position: Int)
    }

    inner class SentMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {

        override fun bind(messagePack: MessagePack, position: Int) {
//            itemView.setOnClickListener {
//                TODO show time
//            }

            itemView.findViewById<RecyclerView>(R.id.innerMessagesRV).adapter = RVInnerMessagesAdapter(messagePack.messages, getItemViewType(position))
        }
    }

    inner class ReceivedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {

        override fun bind(messagePack: MessagePack, position: Int) {
            // TODO image
            itemView.findViewById<RecyclerView>(R.id.innerMessagesRV).adapter = RVInnerMessagesAdapter(messagePack.messages, getItemViewType(position))
            itemView.findViewById<TextView>(R.id.authorName).text = messagePack.authorName
        }
    }
}