# prism-android-sdk-demo-app
Enables your user to download compliant UIDAI Aadhaar XML inside your existing Android App

This SDK (Android) provides a set of screens and functionality to let your user download their Aadhaar XML inside your Android Application itself. This reduces customer drop off as they do not need to navigate to UIDAI Aadhaar Website to download the same.
Aadhaar Offline is the only valid method to submit your Aadhaar identity to any RBI Regulated Entity in order to complete KYC. The Bureau SDK provides an easy to use Verification suite which will enable the most seamless customer onboarding.

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
4. Once the details entered are authenticated, the Aadhaar details are recieved.

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
       implementation 'com.github.Bureau-Inc:prism-android-native-sdk:0.29.0'
}
```

This library also uses some common android libraries. So if you are not already using them then make sure you add these libraries to your module level `build.gradle`
- `androidx.appcompat:appcompat:1.2.0`

## Initialize SDK

```     //Create prism object
        PrismEntryPoint prism;
        
        yourinitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
            //instantiate your prism 
            prism = PrismInstanceProvider.getInstance(context,activity)
            
            //Initialize your prism
               prism.initialize(your merchantId,your user id,
                new PrismCallBack(){

                    @Override
                    public void onKYCFinished(Client aadhaarData, String methodname, Boolean isSuccess) {
                               Log.w("TAG",aadhaarData?.jsonString.toString())
                    }
                }
               ,your success redirection url,your failure redirection url,a boolean to indicate whether flow should be run on production configuration)
                
            //Adding config to priortize the flows by which Aadhaar data is to be taken    
                prism.addConfig(Config(KYC_FIRSTFLOW, KYC_SECONDFLOW,DIGILOCKERFLOW))
                
                //The above order of methods can be rearranged based on priority
                
           //KYC initiate call
           prism.beginKYCFLow()
            }
        });
```
## Aadhaar Fetching Methods
1.KYC_FIRSTFLOW
2.KYC_SECONDFLOW
3.DIGILOCKERFLOW

## Authorization 
To Obtain your organisation's merchantId and user id, contact Bureau

## Customization 
- To customize the theme of the activity use following theme in your `styles.xml` file
    ```
    <style name="InvoidAppTheme.Custom">
        ...
    </style>
    
    <!--To make text in ActionBar dark, use this. Dark text is default-->
    <style name="InvoidAppTheme.AppBarOverlay" parent="ThemeOverlay.AppCompat.Dark.ActionBar" />
    
    <!--To make text in ActionBar white, use this.-->
    <style name="InvoidAppTheme.AppBarOverlay" parent="ThemeOverlay.AppCompat.Light" />
    ```   
- Following colors are customizable. Add them in your `colors.xml`  
    ```
    <!--Customize color of action bar in Activity-->
    <color name="invoidActionBarColor">@color/yourColor</color> 
    
    <!--Customize color of buttons in Activity-->
    <color name="invoidButtonColor">@color/yourColor</color> 
    ```  
- Error message show to the user can be customized by defining following strings in your `strings.xml` file
    ```
    <!--Error message shown in case of OfflineAadhaarHelper.UIDAI_ERROR-->
    <string name="invoid_uidai_error">Server down, please try after some time</string>
    
    <!--Error message shown in case of OfflineAadhaarHelper.INVOID_AUTH_ERROR-->
    <string name="invoid_auth_error">Not authorized</string>
    
    <!--Error message shown in case of OfflineAadhaarHelper.INTERNET_ERROR-->
    <string name="invoid_check_internet_error">Please check your internet connection</string>
    ```

## Response returned from the SDK
- xml file uri
- zip file uri
- share code to open zip file
- processedJSON

```
The PrismCallBack added in the initialize function retruns the data of Aadhaar 

new PrismCallBack(){

                    @Override
                    public void onKYCFinished(Client aadhaarData, String methodname, Boolean isSuccess) {
                               Log.w("Aadhaar Xml",aadhaarData.jsonString.toString())
                               Log.w("XMLURI",aadhaarData.xmlFileUri.toString())
                               Log.w("ZIPURI",aadhaarData.zipFileUri.toString())
                               Log.w("ZIPURI",aadhaarData.shareCode.toString())
                               Log.w("Excecuted Fetching Method",methodname.toString())
                               Log.w("Aadhaar Fetched Status","" + isSuccess)
                    }
                }

```

