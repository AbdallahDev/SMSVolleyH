package com.example.abdallahsarayrah.smsvolleyh

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val pdus = bundle.get("pdus") as Array<*>
        var mobile = ""
        var txt = ""
        var result: String

        for (index in 0 until pdus.size) {
            val sms = SmsMessage.createFromPdu(pdus[index] as ByteArray)
            mobile = sms.displayOriginatingAddress
            txt = sms.displayMessageBody
        }

        Toast.makeText(context, "$mobile - $txt", Toast.LENGTH_SHORT).show()

        val url = "http://193.188.88.148/salesh/item_get.php?itemId=$txt"
        val rq = Volley.newRequestQueue(context)
        val rqType = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            val sms = SmsManager.getDefault()
            if (response != null) {
                result = response.getString("item_name") + "\n" + response.getString("item_price")
                sms.sendTextMessage(mobile, null, result, null, null)
            } else sms.sendTextMessage(mobile, null, "choose another item, this one not found", null, null)
        }, Response.ErrorListener { error -> })
        rq.add(rqType)
    }
}
