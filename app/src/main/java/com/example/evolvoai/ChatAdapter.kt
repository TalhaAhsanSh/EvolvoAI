package com.example.evolvoai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(chatMsg: MutableList<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var leftOfChat: LinearLayout
        var rightOfChat : LinearLayout
        var leftText : TextView
        var rightText: TextView

        init {

            leftOfChat = itemView.findViewById(R.id.chatMsgL)
            rightOfChat = itemView.findViewById(R.id.chatMsgR)
            leftText = itemView.findViewById(R.id.chatMsgLT)
            rightText = itemView.findViewById(R.id.chatMsgRT)
        }

    }

    var chatMsg = mutableListOf<ChatMessage>()

    init {
        this.chatMsg = chatMsg
    }


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatMsgView : View = LayoutInflater.from(parent.context).inflate(R.layout.messagerecyclerlayout, null)
        return ChatViewHolder(chatMsgView)
    }

    override fun getItemCount(): Int {
        return chatMsg.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat: ChatMessage = chatMsg[position]

        if(chat.msgBy == ChatMessage.msgSentByUser){
            holder.leftOfChat.visibility = View.GONE
            holder.rightOfChat.visibility = View.VISIBLE
            holder.rightText.text = chat.chatMsg
        }

        else{
            holder.rightOfChat.visibility = View.GONE
            holder.leftOfChat.visibility = View.VISIBLE
            holder.leftText.text = chat.chatMsg
        }


    }

}