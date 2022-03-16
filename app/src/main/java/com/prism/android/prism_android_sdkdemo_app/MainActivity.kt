package com.prism.android.prism_android_sdkdemo_app


import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.core.view.get
import com.prism.android.prismandroidnativesdk.PrismCallBack
import com.prism.android.prismandroidnativesdk.PrismEntryPoint
import com.prism.android.prismandroidnativesdk.PrismInstanceProvider
import com.prism.android.prismandroidnativesdk.models.*
import com.prism.android.prismandroidnativesdk.utils.*
import com.prism.android.prismsampleapp.R

class MainActivity : AppCompatActivity() {
    lateinit var prism:PrismEntryPoint
    private var alertDialog:AlertDialog.Builder?=null
    private var merchantId = ""
    private var userId = ""
    private var isProduction:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        alertDialog = AlertDialog.Builder(this)
        prism = PrismInstanceProvider.getInstance(this, this)
        var button  = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            isProduction = !isProduction
            if(isProduction)
            {
                button.text = "Set Environment to Staging"
            }
            else
            {
                button.text = "Set Environment to Production"
            }
        }
        findViewById<Button>(R.id.init_button).setOnClickListener {
            merchantId = findViewById<EditText>(R.id.merchant_id_edit).text.toString().trim()
            userId = findViewById<EditText>(R.id.user_id_edit).text.toString().trim()
            if (merchantId.isNotEmpty() && userId.isNotEmpty()){
                prism.initialize(
                    merchantId,
                    userId,
                    object : PrismCallBack {
                        override fun onKYCFinished(
                            aadhaarData: ClientAadhaarData?,
                            methodName: String?,
                            isSuccess: Boolean?, errorType: String
                        ) {
                            if (!errorType.isNullOrEmpty()) {
                                if (errorType == ENDPOINTS_DOWN)
                                    Log.w("Aadhaar Error", "No endpoints Available")
                                else if (errorType == INVOID_AUTH_ERROR)
                                    Log.w("Aadhaar Error", "Not authorized")
                                else if (errorType == DIGILOCKER_ERROR)
                                    Log.w("Aadhaar Error", "Digilocker Site error")
                                else if (errorType == UIDAI_ERROR)
                                    Log.w("Aadhaar Error", "UIDAI Site Error")
                                else if (errorType == INTERNET_ERROR)
                                    Log.w("Aadhaar Error", "Intenet Error")
                                else if (errorType == SDK_ERROR)
                                    Log.w("Aadhaar Error", "SDK Error")
                                else if (errorType == USER_CANCELLED)
                                    Log.w("Aadhaar Error", "Cancelled by user")
                            }
                            if (isSuccess == true)
                                Log.w("TAG", aadhaarData?.jsonString.toString())
                            else
                                showDialog(methodName)
                        }

                    },
                    "https://en3yadd32v3d97l.m.pipedream.net/",
                    "https://en3yadd32v3d97l.m.pipedream.net/",
                    isProduction
                )
                findViewById<Group>(R.id.aadhaar_flow_group).visibility= View.VISIBLE
            }
            else
            {
                Toast.makeText(this,"Please add merchantId and User Id",Toast.LENGTH_LONG).show()
                if(merchantId.isEmpty())
                findViewById<EditText>(R.id.merchant_id_edit).error = "This field can't be blank"
                if(userId.isEmpty())
                findViewById<EditText>(R.id.user_id_edit).error = "This field can't be blank"
            }
        }


        findViewById<Button>(R.id.button1).setOnClickListener {

            prism.addConfig(Config(residentUidaiAadhaarFlow, myAadhaarUidaiFlow, digilockerFlow))
            prism.beginKYCFLow()
        }

        findViewById<Button>(R.id.button2).setOnClickListener {

            prism.addConfig(Config(digilockerFlow, myAadhaarUidaiFlow))
            prism.beginKYCFLow()
        }

        findViewById<Button>(R.id.button3).setOnClickListener {

            prism.addConfig(Config(myAadhaarUidaiFlow, digilockerFlow))
            prism.beginKYCFLow()
        }

    }

    fun showDialog(methodName:String?)
    {
      methodName?.let {
          alertDialog?.setTitle(" Try another method? ")?.setMessage("Do you want try " +
              if(methodName== residentUidaiAadhaarFlow)
              {
                  "myAadhaarUidaiFlow"
              }
              else if(methodName== myAadhaarUidaiFlow)
              {
                  "digilockerFlow"
              }
              else
              {
                 "residentUidaiAadhaarFlow"
              } +
          " next?"
          )
          alertDialog?.setPositiveButton("Yes",object :DialogInterface.OnClickListener{
              override fun onClick(p0: DialogInterface?, p1: Int) {
                  if(methodName== residentUidaiAadhaarFlow)
                  {
                      prism.addConfig(Config(myAadhaarUidaiFlow, digilockerFlow))
                      prism.beginKYCFLow()
                  }
                  else if(methodName== myAadhaarUidaiFlow)
                  {
                      prism.addConfig(Config(digilockerFlow, residentUidaiAadhaarFlow))
                      prism.beginKYCFLow()
                  }
                  else if(methodName == digilockerFlow)
                  {
                      prism.addConfig(Config(residentUidaiAadhaarFlow, myAadhaarUidaiFlow))
                      prism.beginKYCFLow()
                  }
              }

          })?.setNegativeButton("No",null)?.setIcon(android.R.drawable.ic_dialog_alert)?.show()
      }
    }


}