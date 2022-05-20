# Aadhaar verification - Prism SDK User Guide 
Enables your user to download compliant UIDAI Aadhaar XML inside your existing Android App using offline aadhaar verification as well allows for digilocker verification method. 

This SDK (Android) provides a set of screens and functionality to let your user complete Android Application itself. This reduces customer drop off as they do not need to navigate to UIDAI Aadhaar Website to download the same.

Aadhaar Offline or Digilocker is the only valid method to submit your Aadhaar identity to any RBI Regulated Entity in order to complete KYC. The Bureau SDK provides an easy to use Verification suite which will enable the most seamless customer onboarding.

## Steps in the SDK
**For the Aadhaar Flow**
1. User is guided to the UIDAI website to download the paperless e-KYC (Aadhaar .xml)
2. Inputs for "Aadhaar Number" & Captcha are filled by the end user.
3. On continuing
    - [x] An OTP is received by the end user which is then auto read by the SDK. The inVOID SDK only reads the then received OTP message through the screen.
4. Once the details entered are authenticated, the Aadhaar .xml is downloaded in a .zip which is password(share code) protected

**For Digilocker Flow**
1. User is guided to the Digilocker website to submit their Aadhaar details.
2. Input for "Aadhaar Number" are filled by the end user.
3. On continuing
    - [x] An OTP is received by the end user which should be entered in the next page.
4. Once the details entered are authenticated, the Aadhaar details are recieved by bureau backend server. 
5. App backend server will make an API call to bureau backend server and fetch the details of the user.  

## Minimum Requirements
- `minSdkVersion 21` 
- `AndroidX`

## Getting Started

Add following lines in your root ```build.gradle```
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" 
        //add credentials here
        credentials { username authToken }
        }
    }
}
```

Add following lines in your module level ```build.gradle```
```
android {
    ...
    compileOptions {
       sourceCompatibility = 1.8
       targetCompatibility = 1.8
    }
}
dependencies {
    ....
       implementation 'com.github.Bureau-Inc:prism-android-native-sdk:0.53.0'
}
```
Add the following line in your gradle.properties
```

authToken=YOUR_AUTH_TOKEN
```

This library also uses some common android libraries. So if you are not already using them then make sure you add these libraries to your module level `build.gradle`
- `androidx.appcompat:appcompat:1.2.0`

You might need to add the following code to the application tag in Android Manifest file if Mixpanel View Crawler error shows up
```
<meta-data
            android:name="com.mixpanel.android.MPConfig.DisableViewCrawler"
            android:value="true"/>
```          

## Initialize SDK

```     //Create prism object
        PrismEntryPoint prism;
        
        yourinitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
            //instantiate your prism 
            prism = PrismInstanceProvider.getInstance(context,activity);
            
            //Initialize your prism only once
               prism.initialize(your credentialId,your user id,
                new PrismCallBack(){

                    @Override
                    public void onKYCFinished(ClientAadhaarData aadhaarData, String methodName, Boolean isSuccess,String errorType) {
                               Log.w("Aadhaar Data",aadhaarData.getJsonString.toString())
                    }
                }
               ,your success redirection url,your failure redirection url,a boolean to indicate whether flow should be run on production configuration,
               PrismCustomisation.Builder().build());
               //You can Add customisation to SDK's UI by adding values to PrismCustomisation Builder
                
            //Adding config to priortize the flows by which Aadhaar data is to be taken can be added multiple times    
                prism.addConfig(new Config(residentUidaiAadhaarFlow, myAadhaarUidaiFlow,digilockerFlow));
                
                //The above order of methods can be rearranged based on priority
                
           //KYC initiate call
           prism.beginKYCFLow();
            }
        });
```
#### Notes 
##### 1.UserId and MerchantId are mandatory fields and should not be empty. UserId is a unique string that represents each unique user trying the flow.
##### 2.At least 2 methods should be set as first and second priority when adding config to sdk

## Aadhaar Fetching Methods
### 1.residentUidaiAadhaarFlow - URL : "https://resident.uidai.gov.in/offline-kyc" - Data available in SDK 
### 2.myAadhaarUidaiFlow - URL : "https://myaadhaar.uidai.gov.in/" - Data available in SDK 
### 3.digilockerFlow - Data available in backend API call. 

## Authorization 
To Obtain your organisation's credentialId contact Bureau. 

## UserTracking 
UserId can be used to call Bureau backend API to fetch data regarding user. It should be unique string in nature. For each KYC flow the userId should be uniquely generated. It can be mobile number or any email associated to a user also. 

## SDK Customization 
- To customize the theme of the activity use following theme in your `styles.xml` file
    ```
    <style name="PrismAppTheme">
        ...
        <!--Add Theme Customisation here-->
    </style>
    
    <!--To add customisation to toolbar in SDK's activity add the following style-->
    <style name="PrismToolbarTheme" parent="ThemeOverlay.AppCompat.Dark.ActionBar" >
        <item name="android:background">@drawable/invoid_button_bg</item>

    </style>
    <!--To add customisation to buttons in SDK add the following style-->
    <style name="PrismButtonTheme" parent="@android:style/Widget.Button">
        <item name="android:textColor">@android:color/white</item>
        <item name="android:shadowColor">#FF000000</item>
        <item name="android:shadowDx">0</item>
        <item name="android:shadowDy">-1</item>
        <item name="android:shadowRadius">0.2</item>
        <item name="android:background">@color/black</item>
        <item name="android:textSize">14sp</item>
        <item name="android:textStyle">bold</item>
    </style>

- Error message show to the user can be customized by defining following strings in your `strings.xml` file
    ```
    <!--Error message shown in case of OfflineAadhaarHelper.UIDAI_ERROR-->
    <string name="invoid_uidai_error">Server down, please try after some time</string>
    
    <!--Error message shown in case of OfflineAadhaarHelper.INVOID_AUTH_ERROR-->
    <string name="invoid_auth_error">Not authorized</string>
    
    <!--Error message shown in case of OfflineAadhaarHelper.INTERNET_ERROR-->
    <string name="invoid_check_internet_error">Please check your internet connection</string>
    ```
### Code Customisations
  ```     
  //Add Prism Customisation values to builder and add it to initialize function
 new PrismCustomisation.Builder()
 .toolBarTitle(YOUR_CUSTOM_TEXT) // adds a custom title to toolbar
 .hideToolbar(true) // TO show or hide toolbar in SDK's UI(The default is show)
 .build()
 
```  
    

## Response returned from the SDK
- xml file uri
- zip file uri
- share code to open zip file
- processedJSON

The PrismCallBack added in the initialize function retrun's whether we have successfuly fetched details or not 

```

new PrismCallBack(){

                    @Override
                    public void onKYCFinished(ClientAadhaarData aadhaarData, String methodName, Boolean isSuccess,String errorType) {
                               if(isSuccess)
                               {
                                    //Write your success logic here
                                    //the object aadhaar data contains the details
                                    //errorType will be empty here
                                    if(methodName==digilockerFlow)
                                    {
                                    //Make the backend API call to get digilocker data
                                    }
                                    else
                                    {
                                    Log.d("Aadhaar Data", aadhaarData.getJsonString.ToString());
                                    Log.d("XML File Uri", aadhaarData.getXmlFileUri().toString());
                                    Log.d("Aadhaar Data", aadhaarData.getZipFileUri().toString());
                                    Log.d("Aadhaar Data", aadhaarData.getShareCode.toString());
                                    }
                               }
                               else
                               {     //Check errorType here
                                      if(errorType== ENDPOINTS_DOWN)
                                        Log.w("Aadhaar Error","No endpoints Available")
                                      else if(errorType== INVOID_AUTH_ERROR)
                                        Log.w("Aadhaar Error","Not authorized")
                                      else if(errorType== DIGILOCKER_ERROR)
                                        Log.w("Aadhaar Error","Digilocker Site error")
                                      else if(errorType== UIDAI_ERROR)
                                        Log.w("Aadhaar Error","UIDAI Site Error")
                                      else if(errorType== INTERNET_ERROR)
                                        Log.w("Aadhaar Error","Intenet Error")
                                      else if(errorType== SDK_ERROR)
                                        Log.w("Aadhaar Error","SDK Error")
                                      else if(errorType== USER_CANCELLED)
                                         Log.w("Aadhaar Error","Cancelled by user")
                                         
                                         
                            
                                     //Write your failure logic here
                                     //You can call another method by reinitializing config and calling beginKYCFlow() as shown below
                                   prism.addConfig(new Config(myAadhaarUidaiFlow,residentUidaiAadhaarFlow,digilockerFlow));
                                   prism.beginKYCFLow();
                               }
                    }
                }
                

```

When Aadhaar fetch is successful the callback returns the isSuccess boolean as true and when a failure happens the callback returns the isSuccess boolean as false along with the methodName. 
By monitoring the method name we can identify which method was used to fetch the Aadhaar details.                  


## Backend API call to get data for a user completing the digilocker flow

UserId and AuthHeader will have to be replaced in the requests below. 

In staging 

```
curl --location --request GET 'https://api.overwatch.stg.bureau.id/v1/id/UserId/suppliers/offline-aadhaar' \
--header 'Authorization: Basic AuthHeader'

```
In production 

```
curl --location --request GET 'https://api.overwatch.bureau.id/v1/id/UserId/suppliers/offline-aadhaar' \
--header 'Authorization: Basic AuthHeader'

```


