package com.joeSoFine.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.MainActivity
import com.joeSoFine.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_in.*


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

        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        // Code for generating hash key
        /* try {
            val info = packageManager.getPackageInfo(
                "com.joeSoFine.dormcuments",
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
            progressBar10.visibility = View.VISIBLE
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
                    Toast.makeText(
                        applicationContext,
                        "Facebook are still only avablable for developers",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar10.visibility = View.VISIBLE
                    val credenials =
                        FacebookAuthProvider.getCredential(loginResult.accessToken.token);


                    auth.signInWithCredential(credenials).addOnCompleteListener() { task ->
                        if (task.result?.additionalUserInfo?.isNewUser!!) {

                            val request =
                                GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                                    try {
                                        println(`object`.toString())

                                        val dimensionPixelSize =
                                            resources.getDimensionPixelSize(com.facebook.R.dimen.com_facebook_profilepictureview_preset_size_large)
                                        val profilePictureUri: Uri = Profile.getCurrentProfile().getProfilePictureUri(dimensionPixelSize, dimensionPixelSize)

                                        val user = User(
                                            `object`.getString("name"), "",
                                            `object`.getString("birthday"), "", "", "", profilePictureUri.toString()
                                        )

                                        val userId = auth.currentUser?.uid
                                        if (userId != null) {
                                            database.child(userId).setValue(user)
                                                .addOnSuccessListener {
                                                    val intent = Intent(applicationContext, SignUpWithFacebookFragment::class.java)
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    startActivity(intent)
                                                    progressBar10.visibility = View.GONE

                                                }
                                                .addOnFailureListener {
                                                    // Write failed
                                                    progressBar10.visibility = View.GONE
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
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            progressBar10.visibility = View.GONE
                        }

                        Toast.makeText(
                            applicationContext,
                            "Facebook are still only avablable for developers",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onCancel() {
                    println("Facebook onCancel");
                    progressBar10.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "Facebook are still only avablable for developers",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException) {
                    println("Facebook onError")
                    progressBar10.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "Facebook are still only avablable for developers",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    progressBar10.visibility = View.GONE
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    progressBar10.visibility = View.GONE
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
                    progressBar10.visibility = View.GONE
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

    companion object {
        private const val TAG = "EmailPassword"
    }
}