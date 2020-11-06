package com.joeSoFine.dormcuments.ui.signIn

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.joeSoFine.dormcuments.R
import kotlinx.android.synthetic.main.activity_sign_up.save
import kotlinx.android.synthetic.main.activity_sign_up2.*
import java.io.File
import java.io.FileInputStream


class SignUp_Image : AppCompatActivity() {

    lateinit var imageUri: Uri
    var database = FirebaseDatabase.getInstance().getReference("Users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)


        choosePic.setOnClickListener() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        save.setOnClickListener(View.OnClickListener {
            var storageRef = Firebase.storage.reference

            val user: User? = intent.getParcelableExtra("user")
            val userId: String? = intent.getStringExtra("userid")

            var file = imageUri

            val imagesRef = storageRef.child("images/${file.lastPathSegment}")

            var uploadTask = imagesRef.putFile(file)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Toast.makeText(applicationContext, "FAILED", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...

                if (user != null) {
                    user.url = imagesRef.downloadUrl.toString()

                    if (userId != null) {
                        database.child(userId).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext, "SUCCES", Toast.LENGTH_SHORT).show()

                                val intent = Intent(applicationContext, SignIn::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                // Write failed
                                Toast.makeText(applicationContext, "Try again", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        })
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageUri = data?.data!!
            downloaded_picture.setImageURI(imageUri)
            var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            val height_dimension: Int = getSquareCropDimensionForBitmap(bitmap)
            val width_dimension = height_dimension + 300
            var croped_bitmap = ThumbnailUtils.extractThumbnail(bitmap, height_dimension, width_dimension)
            downloaded_picture.setImageBitmap(croped_bitmap)
            //downloaded_picture.setImageURI(imageUri)
        }
    }

    fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }
}