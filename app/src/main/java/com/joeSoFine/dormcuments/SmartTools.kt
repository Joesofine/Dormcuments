package com.joeSoFine.dormcuments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.ui.signIn.SignIn
import java.io.ByteArrayOutputStream

object SmartTools {

    fun setUpOnBackPressed(activity: Activity){
        var activity = activity
        (activity as MainActivity).setOnBackPressedListener(BaseBackPressedListener(activity))
    }

    fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun signOut(context: Context, contextView: View){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitleSignOut)
        builder.setMessage(R.string.dialogMessageSignOut)
        builder.setIcon(R.drawable.ic_baseline_warning_24)

        builder.setPositiveButton("Continue"){ dialogInterface, which ->
            Firebase.auth.signOut()
            Snackbar.make(contextView,"You are now signed out.", Snackbar.LENGTH_SHORT)
                .show()
            val intent = Intent(context, SignIn::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
        builder.setNeutralButton("Cancel"){ dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}