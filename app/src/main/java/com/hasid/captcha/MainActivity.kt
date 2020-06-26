package com.hasid.captcha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import org.json.JSONArray

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var TAG = "MainActivity"
    lateinit var btnverifyCaptcha: Button
    private var SITE_KEY = "6Ldoq6kZAAAAACpdrmJN1CcobNtR2FJVLqzzaW-c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnverifyCaptcha = findViewById(R.id.button)
        btnverifyCaptcha.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this) { response ->
                    if (!response.tokenResult.isEmpty()) {
                        handleVerify(response.tokenResult)
                    }
                }
                .addOnFailureListener(this) { e ->
                    if (e is ApiException) {
                        Log.d(TAG,("Error message: " + CommonStatusCodes.getStatusCodeString(e.statusCode)))
                    } else {
                        Log.d(TAG, "Unknown type of error: " + e.message)
                    }
                }
    }

    protected fun handleVerify(responseToken: String) {
        //it is google recaptcha siteverify server
        //you can place your server url
        val url = "https://www.google.com/recaptcha/api/siteverify"
        AndroidNetworking.get(url)
                .addHeaders("token", responseToken)
                .setTag("MY_NETWORK_CALL")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        Toast.makeText(applicationContext,"Response: "+response,Toast.LENGTH_LONG).show()
                        // do anything with response
                    }

                    override fun onError(error: ANError) {
                        // handle error
                    }
                })
    }
}