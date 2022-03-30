package com.lambda.modules

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.lambda.LambdaUtilities
import com.lambda.client.event.events.ConnectionEvent
import com.lambda.client.event.listener.listener
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.text.MessageSendHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sun.rmi.runtime.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


internal object ConnectionTest : PluginModule(
    name = "ConnectionTest",
    category = Category.MISC,
    description = "Automatically tests your connection (actually a http get/post test)",
    pluginMain = LambdaUtilities
) {
    private fun <T> runConnection(url: String, block: (HttpsURLConnection) -> T?, catch: (Exception) -> Unit = { it.printStackTrace() }): T? {
        (URL(url).openConnection() as HttpsURLConnection).run {
            return try {
                doOutput = true
                doInput = true
                block(this)
            } catch (e: Exception) {
                catch(e)
                null
            } finally {
                disconnect()
            }
        }
    }
    private fun getUrlContents(url: String): String {
        val content = StringBuilder()

        runConnection(url, block = { connection ->
            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            bufferedReader.forEachLine { content.append("$it\n") }
        }, catch = {
            it.printStackTrace()
        })
        MessageSendHelper.sendChatMessage(content.toString())
        println(content.toString())
        return content.toString()
    }
    
    private fun postRequest() {
        val url = URL("https://api.openai.com/v1/engines/text-davinci-002/completions")
        //val postJson = {"prompt":"Suggest one name for a horse.","temperature":0,"max_tokens":100}
        val postData = "{\"prompt\":\"How are you?\",\"temperature\":0,\"max_tokens\":100}"
        val bearer = "Bearer sess-IV9vOT4j5Ju20pH8XdlmpG3JkPJmyPYmJvtZEbC0"

        val conn = url.openConnection()
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("authorization", bearer)
        conn.setRequestProperty("Content-Length", postData.length.toString())

        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
        BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                println(line.toString())
            }
        }
    }

    init {
        onEnable {
            //println(getUrlContents(url))
            //MessageSendHelper.sendChatMessage(getUrlContents(url))
            postRequest()
            MessageSendHelper.sendChatMessage("Enabled Connection testing!")
        }

        onDisable {
            MessageSendHelper.sendChatMessage("Disabled Connection testing!")
        }

        listener<ConnectionEvent.Disconnect> {
            println("a")
            MessageSendHelper.sendChatMessage("a")
        }
    }
}