package com.example.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dormcuments.MainActivity
import com.example.dormcuments.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import org.json.JSONObject


@Suppress("DEPRECATION")
class SignIn : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    private var callbackManager: CallbackManager? = null
    private lateinit var auth: FirebaseAuth
    var database = FirebaseDatabase.getInstance().getReference("Users")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        // Code for generating hash key
        /* try {
            val info = packageManager.getPackageInfo(
                "com.example.dormcuments",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                println("--------------------------------------------------------------------------------------------")
                println((Base64.getEncoder().encodeToString(md.digest())))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

         */


        setIconsTint(mail, R.drawable.email_icon_white, R.drawable.email_icon_tint)
        setIconsTint(password, R.drawable.password_icon_white, R.drawable.password_icon_tint)

        signIpButton.setOnClickListener(View.OnClickListener {
            signIn(mail.text.toString().toLowerCase().replace(" ", ""), password.text.toString())
        })

        SignUpButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
        })

        ForgotPasswordBotton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ForgottenPassword::class.java)
            startActivity(intent)
        })


        var login: LoginButton = findViewById(R.id.loginButton)
            callbackManager = CallbackManager.Factory.create()
        login.setReadPermissions(
            "public_profile",
            "email",
            "user_birthday"
        )
        LoginManager.getInstance().logOut()
        login.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val credenials =
                        FacebookAuthProvider.getCredential(loginResult.accessToken.token);


                    auth.signInWithCredential(credenials).addOnCompleteListener() { task ->
                        if (task.result?.additionalUserInfo?.isNewUser!!) {

                            val request =
                                GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                                    try {
                                        println(`object`.toString())

                                        val user = User(
                                            `object`.getString("name"), "Roomnumber",
                                            `object`.getString("birthday"), "", "", "")

                                        val userId = auth.currentUser?.uid
                                        if (userId != null) {
                                            database.child(userId).setValue(user)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "User Created",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    startActivity(Intent(applicationContext, SignUpWithFacebookFragment::class.java))
                                                }
                                                .addOnFailureListener {
                                                    // Write failed
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Try again",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                        }


                                        if (`object`.has("id")) {
                                            //handleSignInResultFacebook(`object`)
                                        } else {
                                            println(`object`.toString())
                                        }

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        //dismissDialogLogin()
                                    }
                                }
                            val parameters = Bundle()
                            parameters.putString("fields", "name,email,birthday,id,picture.type(large)")
                            request.parameters = parameters
                            request.executeAsync()
                        } else {
                            println("Facebook token: " + loginResult.accessToken.token)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        }
                    }
                }

                override fun onCancel() {
                    println("Facebook onCancel"); }

                override fun onError(error: FacebookException) {
                    println("Facebook onError")
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun setIconsTint(edit: EditText, noTint: Int, tint: Int){
        edit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                edit.setCompoundDrawablesWithIntrinsicBounds(tint, 0, 0, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            else {
                edit.setCompoundDrawablesWithIntrinsicBounds(noTint, 0, 0, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                    status.visibility = View.VISIBLE
                }
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }
    private fun validateForm(): Boolean {
        var valid = true

        val email = mail.text.toString()
        if (TextUtils.isEmpty(email)) {
            mail.error = "Required."
            valid = false
        } else {
            mail.error = null
        }

        val Upassword = password.text.toString()
        if (TextUtils.isEmpty(Upassword)) {
            password.error = "Required."
            valid = false
        } else {
            password.error = null
        }

        return valid
    }

    private fun getFbInfo() {
        val request: GraphRequest = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken(),
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(
                    `object`: JSONObject,
                    response: GraphResponse
                ) {
                    try {
                        val id: String = `object`.getString("id")
                        val first_name: String = `object`.getString("first_name")
                        val last_name: String = `object`.getString("last_name")
                        val gender: String = `object`.getString("gender")
                        val birthday: String = `object`.getString("birthday")
                        val image_url = "http://graph.facebook.com/$id/picture?type=large"
                        val email: String
                        if (`object`.has("email")) {
                            email = `object`.getString("email")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id,first_name,last_name,email,gender,birthday"
        ) // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters)
        request.executeAsync()
    }


    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}