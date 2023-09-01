package com.example.evolvoai

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var msgEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var msgView: RecyclerView
    private var chatList = mutableListOf<ChatMessage>()
    private var chatAdapter = ChatAdapter(chatList)

    private val jsonMedia: MediaType = "application/json; charset=utf-8".toMediaType()
    private var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        msgEditText = findViewById(R.id.messageET)
        sendButton = findViewById(R.id.sendBtn)
        msgView = findViewById(R.id.messages)
        msgView.adapter = chatAdapter
        val layoutManager  = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        msgView.layoutManager = layoutManager


        sendButton.setOnClickListener {
            val askMsg = msgEditText.text.toString().trim()
            if (askMsg.isNotEmpty()) {
                addToMessage(askMsg, ChatMessage.msgSentByUser)
                msgEditText.setText("")
                useChatGPT(askMsg)
            }

            else{
                Toast.makeText(this, "Can't answer without question", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addToMessage(msg : String, sender: String){
        runOnUiThread{
            chatList.add(ChatMessage(msg,sender))
            chatAdapter.notifyDataSetChanged()
            msgView.smoothScrollToPosition(chatAdapter.itemCount)

        }
    }

    private fun useChatGPT(msg: String){

        val json = JSONObject()

        try {
            json.put("model", "text-davinci-003")
            json.put("prompt", msg)
            json.put("max_tokens", 4000)
            json.put("temperature", 0)
        }

        catch (e: JSONException){
            e.printStackTrace()
        }

        val requestBody : RequestBody = json.toString().toRequestBody(jsonMedia)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer TYPE YOUR API KEY HERE")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addToResponse("Failed to give response due to ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val jsonBody: JSONObject?

                    try {
                        jsonBody = JSONObject(response.body?.string()!!)
                        val arrayOfJson = jsonBody.getJSONArray("choices")
                        val answer: String = arrayOfJson.getJSONObject(0).getString("text")
                        addToResponse(answer.trim())
                    }

                    catch (e: JSONException){
                        e.printStackTrace()
                    }
                }

                else{
                    addToResponse("Failed to give response due to ${response.body.toString()}")
                }
            }

        })

    }

    fun addToResponse(response: String){
        addToMessage(response, ChatMessage.msgSentByAI)
    }
}

