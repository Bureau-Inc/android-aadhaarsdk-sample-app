package com.prism.android.prism_android_sdkdemo_app


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.prism.android.prismandroidnativesdk.PrismCallBack
import com.prism.android.prismandroidnativesdk.PrismEntryPoint
import com.prism.android.prismandroidnativesdk.PrismInstanceProvider
import com.prism.android.prismandroidnativesdk.models.*
import com.prism.android.prismsampleapp.R

class MainActivity : AppCompatActivity() {
    lateinit var prism:PrismEntryPoint
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button1).setOnClickListener {
 prism = PrismInstanceProvider.getInstance(this,this)
            prism.initialize("auth0|6215d57ce2b178006ac73777","user123_random-8283",
                object : PrismCallBack {
                    override fun onKYCFinished(
                        aadhaarData: ClientAadhaarData?,
                        methodname: String?,
                        isSuccess: Boolean?
                    ) {
                        if(isSuccess==true)
                            Log.w("TAG",aadhaarData?.jsonString.toString())
                    }

                },"https://en3yadd32v3d97l.m.pipedream.net/","https://en3yadd32v3d97l.m.pipedream.net/",false)
            //Diglocker sucess and failure url
            prism.addConfig(Config(residentUidaiAadhaarFlow, myAadhaarUidaiFlow, digilockerFlow))
            prism.beginKYCFLow()
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            prism = PrismInstanceProvider.getInstance(this,this)
            prism.initialize("auth0|6215d57ce2b178006ac73777","user123_random-8283",
                object : PrismCallBack {
                    override fun onKYCFinished(
                        aadhaarData: ClientAadhaarData?,
                        methodname: String?,
                        isSuccess: Boolean?
                    ) {
                        if(isSuccess==true)
                        Log.w("TAG",aadhaarData?.jsonString.toString())
                    }

                },"https://en3yadd32v3d97l.m.pipedream.net/","https://en3yadd32v3d97l.m.pipedream.net/",false)
            //Diglocker sucess and failure url
            prism.addConfig(Config(digilockerFlow, myAadhaarUidaiFlow))
            prism.beginKYCFLow()
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            prism = PrismInstanceProvider.getInstance(this,this)
            prism.initialize("auth0|6215d57ce2b178006ac73777","user123_random-8283",
                object : PrismCallBack {
                    override fun onKYCFinished(
                        aadhaarData: ClientAadhaarData?,
                        methodname: String?,
                        isSuccess: Boolean?
                    ) {
                        if(isSuccess==true)
                            Log.w("TAG",aadhaarData?.jsonString.toString())
                    }

                },"https://en3yadd32v3d97l.m.pipedream.net/","https://en3yadd32v3d97l.m.pipedream.net/",false)
            //Diglocker sucess and failure url
            prism.addConfig(Config(myAadhaarUidaiFlow, digilockerFlow))
            prism.beginKYCFLow()
        }

    }

}