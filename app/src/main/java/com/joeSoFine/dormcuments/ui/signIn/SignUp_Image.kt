package com.joeSoFine.dormcuments.ui.signIn

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.joeSoFine.dormcuments.MainActivity
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.databaseService
import kotlinx.android.synthetic.main.activity_sign_up.save
import kotlinx.android.synthetic.main.activity_sign_up2.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class SignUp_Image : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1

    lateinit var imageUri: Uri
    var database = FirebaseDatabase.getInstance().getReference("Users")
    var bool = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        choosePic.setOnClickListener() {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) { //permission denied
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
            bool = true
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
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        user.url = uri.toString()

                        if (userId != null) {
                            database.child(userId).setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "SUCCES", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                }
                                .addOnFailureListener {
                                    // Write failed
                                    Toast.makeText(applicationContext, "Try again", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!bool) {
            Firebase.auth.currentUser?.delete()
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        var tempDir: File = Environment.getExternalStorageDirectory()
        tempDir = File(tempDir.getAbsolutePath().toString() + "/.temp/")
        tempDir.mkdir()
        val tempFile: File = File.createTempFile(title.toString(), ".jpg", tempDir)
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bitmapData = bytes.toByteArray()
        //write the bytes in file

        //write the bytes in file
        val fos = FileOutputStream(tempFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return Uri.fromFile(tempFile)

        //val bytes = ByteArrayOutputStream()
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        //val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        //return Uri.parse(path.toString())
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;

        private val RESULT_LOAD_IMAGE = 1
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

        if (resultCode == Activity.RESULT_OK){
            imageUri = data?.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            val height_dimension: Int = getSquareCropDimensionForBitmap(bitmap)
            val width_dimension = height_dimension + 300
            var croped_bitmap = ThumbnailUtils.extractThumbnail(bitmap, height_dimension, width_dimension)
            var cropUri = getImageUriFromBitmap(applicationContext, croped_bitmap)
            println(cropUri)
            imageUri = cropUri
            downloaded_picture.setImageBitmap(croped_bitmap)
        }
    }

    fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(
                this,
                "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }


}