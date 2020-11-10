package com.joeSoFine.dormcuments.ui.residents

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.ui.signIn.SignIn
import com.joeSoFine.dormcuments.ui.signIn.SignUp_Image
import com.joeSoFine.dormcuments.ui.signIn.User
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.city_signup
import kotlinx.android.synthetic.main.activity_sign_up2.*
import kotlinx.android.synthetic.main.fragment_edit_food.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.util.*


class profileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var getdata : ValueEventListener
    var database = FirebaseDatabase.getInstance().getReference("Users")
    var targetHeight = 0
    var targetWidth = 0
    private val PERMISSION_REQUEST_CODE = 1
    lateinit var imageUri: Uri
    lateinit var url: String



    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = Firebase.auth

        val date: EditText = root.findViewById(R.id.date)
        val name_signup = root.findViewById<EditText>(R.id.name_signup)
        val room_spinner: Spinner = root.findViewById(R.id.room_spinner)
        val from: EditText = root.findViewById(R.id.city_signup)
        val diet: EditText = root.findViewById(R.id.diet)
        val funfact: EditText = root.findViewById(R.id.funfact)
        val datePicker: DatePicker = root.findViewById<DatePicker>(R.id.datePicker)
        val close: Button = root.findViewById(R.id.close)
        val city_edit:EditText = root.findViewById(R.id.city_edit)
        val country_edit:EditText = root.findViewById(R.id.country_edit)
        val userImage = root.findViewById<ImageView>(R.id.userImage)
        val editImage = root.findViewById<ImageView>(R.id.editImageButton)
        val userid = auth.currentUser?.uid

        if (auth.currentUser != null) {
            for (userInfo in auth.currentUser!!.getProviderData()) {
                if (userInfo.getProviderId().equals("facebook.com")) {
                    root.findViewById<Button>(R.id.resetPassword).visibility = View.GONE
                }
            }
        }

        editImage.setOnClickListener() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, profileFragment.PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        getdata = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (userid != null) {
                    var name = p0.child(userid).child("fname").getValue().toString()
                    var rnumber = p0.child(userid).child("number").getValue().toString()
                    var birthday = p0.child(userid).child("bdate").getValue().toString()
                    var orgin = p0.child(userid).child("from").getValue().toString()
                    var food = p0.child(userid).child("diet").getValue().toString()
                    var fact = p0.child(userid).child("funfact").getValue().toString()
                    url = p0.child(userid).child("url").getValue().toString()

                    val byear = birthday[2].toInt()
                    val bmonth = birthday[1].toInt()
                    val bday = birthday[0].toInt()
                    val cc: List<String> = orgin.split(", ")

                    name_signup.setText(name)
                    from.setText(orgin)
                    city_edit.setText(cc[0])
                    country_edit.setText(cc[1])
                    diet.setText(food)
                    funfact.setText(fact)
                    date.setText(birthday)
                    room_spinner.setSelection((room_spinner.adapter as ArrayAdapter<String>).getPosition(rnumber))

                    context?.let { Glide.with(it).load(url).into(userImage) }


                    datePicker.init(birthday[2].toInt(), birthday[1].toInt() - 1, birthday[0].toInt()) { view, year, month, day ->
                        val month = month + 1
                        val msg = "$day/$month/$year"
                        date.setText(msg)
                        date.setTextColor(resources.getColor(R.color.White))
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        date.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                datePicker.visibility = View.VISIBLE
                close.visibility = View.VISIBLE
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_pen_icon_tint, 0)
                date.background.mutate().setColorFilter(
                    resources.getColor(android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                datePicker.visibility = View.GONE
                close.visibility = View.GONE
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_pen_icon_white, 0)
                date.background.mutate().setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }
        from.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                city_signup.visibility = View.GONE
                city_edit.visibility = View.VISIBLE
                country_edit.visibility = View.VISIBLE
            } else {
                city_signup.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_pen_icon_white, 0)
                city_signup.background.mutate().setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }




        val myAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, resources.getStringArray(R.array.spinner))
        myAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
        room_spinner.adapter = myAdapter

        setIconsTint(name_signup, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        //setIconsTint(from, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(diet, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(funfact, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(city_edit, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)
        setIconsTint(country_edit, R.drawable.edit_pen_icon_white, R.drawable.edit_pen_icon_tint)

        close.setOnClickListener(){
            datePicker.visibility = View.GONE
            close.visibility = View.GONE
        }

        root.findViewById<Button>(R.id.resetPassword).setOnClickListener(){
            val user = auth.currentUser
            var newPassword = ""
            var oldPassword = ""
            val alert = AlertDialog.Builder(context)
            getTagetSize()

            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val title = TextView(context)
            title.text = "Reset Password"
            title.textSize = 18F
            val width = (targetWidth - title.width - 550) / 2
            title.setPadding(width, 0, 0, 0)
            title.setTextColor(R.color.Black)
            layout.addView(title)

            val edittextOld = EditText(context)
            edittextOld.hint = "Old Password"
            edittextOld.setTransformationMethod(PasswordTransformationMethod.getInstance())
            layout.addView(edittextOld)

            val edittextNew = EditText(context)
            edittextNew.hint = "New Password"
            edittextNew.setTransformationMethod(PasswordTransformationMethod.getInstance())
            layout.addView(edittextNew)

            alert.setView(layout)

            alert.setPositiveButton("Save Password") { dialog, whichButton ->
                newPassword = edittextNew.text.toString()
                oldPassword = edittextOld.text.toString()

                val credential = EmailAuthProvider
                    .getCredential(user?.email.toString(), oldPassword)

                user?.reauthenticate(credential)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            if (newPassword != "") {
                                user.updatePassword(newPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) { Toast.makeText(context, "Password is changed", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Password couldn't be changed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                             }else {
                                Toast.makeText(context, "New password is null", Toast.LENGTH_SHORT).show()
                            }
                        } else
                            Toast.makeText(context, "Old password is wrong", Toast.LENGTH_SHORT).show()
                    }
            }

            alert.setNeutralButton("Cancel") { dialog, whichButton -> }
            alert.show()
        }

        root.findViewById<Button>(R.id.signout).setOnClickListener(){
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.dialogTitleSignOut)
            builder.setMessage(R.string.dialogMessageSignOut)
            builder.setIcon(R.drawable.ic_baseline_warning_24)

            builder.setPositiveButton("Continue"){ dialogInterface, which ->
                signOut()
                Toast.makeText(context, "You are now signed out", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, SignIn::class.java)
                startActivity(intent)
                }
            builder.setNeutralButton("Cancel"){ dialogInterface, which ->
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        root.findViewById<Button>(R.id.delete).setOnClickListener(){
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.dialogTitleDelete)
            builder.setMessage(R.string.dialogMessageDelete)
            builder.setIcon(R.drawable.ic_baseline_warning_24)

            builder.setPositiveButton("Continue"){ dialogInterface, which ->

                auth.currentUser?.delete()?.addOnSuccessListener {
                    if (userid != null) {
                        deleteUser(userid)
                        Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, SignIn::class.java)
                        startActivity(intent)
                    }
                }?.addOnFailureListener{
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNeutralButton("Cancel"){ dialogInterface, which ->
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        root.findViewById<Button>(R.id.save).setOnClickListener(){
            var storageRef = Firebase.storage.reference
            val fname = name_signup.text.toString()
            val number = room_spinner.selectedItem.toString()
            val bdate = date.text.toString()
            val city = city_edit.text.toString()
            val country = country_edit.text.toString()
            val from = "$city, $country"
            val diet = diet.text.toString()
            val fact = funfact.text.toString()

            if (fname.isEmpty()) {
                name_signup.error = "Please write a name"
            } else if (number == "Roomnumber") {
                Toast.makeText(requireContext(), "Please choose roomnumber", Toast.LENGTH_SHORT).show()
            } else if (bdate.isEmpty()) {
                date.error = "Please choose birthday"
            } else if (city.isEmpty()) {
                city_signup.error = "Please let us know where you are from"
            } else if (country.isEmpty()) {
                country_signup.error = "Please let us know where you are from"
            } else {
                val user = User(fname, number, bdate, from, diet, fact, url)
                if (user != null) {
                    if (userid != null) {
                        if (imageUri != null) {
                            var file = imageUri
                            val imagesRef = storageRef.child("images/${file.lastPathSegment}")

                            var uploadTask = imagesRef.putFile(file)
                            uploadTask.addOnFailureListener {
                                Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener { taskSnapshot ->
                                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                                    user.url = uri.toString()

                                        database.child(userid).setValue(user)
                                         .addOnSuccessListener {
                                               Toast.makeText(context, "Changes are saved", Toast.LENGTH_SHORT).show()
                                               getFragmentManager()?.popBackStack()
                                            }
                                            .addOnFailureListener {
                                               // Write failed
                                                 Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
                                             }
                                }
                            }
                        } else {
                            database.child(userid).setValue(user).addOnSuccessListener {
                                Toast.makeText(context, "Changes are saved", Toast.LENGTH_SHORT).show()
                                getFragmentManager()?.popBackStack()
                            }
                            .addOnFailureListener {
                                // Write failed
                                Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return root
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun setIconsTint(edit: EditText, noTint: Int, tint: Int){
        edit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, tint, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            else {
                edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, noTint, 0)
                edit.getBackground().mutate().setColorFilter(
                    getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }
    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

    private fun getTagetSize(){
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        targetWidth = width
        targetHeight = height
    }
    private fun deleteUser(userid: String){
        var dName = database.child(userid)

        dName.removeValue()
        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
    }

    private fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, profileFragment.IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            profileFragment.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == profileFragment.IMAGE_PICK_CODE){
            requestPermission()
            imageUri = data?.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)

            val height_dimension: Int = getSquareCropDimensionForBitmap(bitmap)
            val width_dimension = height_dimension + 300
            var croped_bitmap = ThumbnailUtils.extractThumbnail(bitmap, height_dimension, width_dimension)
            var cropUri = context?.let { getImageUriFromBitmap(it, croped_bitmap) }
            println(cropUri)
            imageUri = cropUri!!
            userImage.setImageBitmap(croped_bitmap)
        }
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(
                context,
                "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

 }