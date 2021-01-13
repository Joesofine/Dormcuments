import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.joeSoFine.dormcuments.R
import com.joeSoFine.dormcuments.ui.foodclub.EditFoodFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.joeSoFine.dormcuments.SmartTools
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_food_details.*


class FoodDetailsFragment : Fragment() {
    var database = FirebaseDatabase.getInstance().getReference("Foodclub")
    var databaseU = FirebaseDatabase.getInstance().getReference("Users")
    lateinit var getdata : ValueEventListener
    lateinit var getdataU : ValueEventListener
    lateinit var editBundle : Bundle
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_food_details, container, false)
        val lottie = root.findViewById<LottieAnimationView>(R.id.animation_view)
        val bundle = this.arguments
        val checked = root.findViewById<Switch>(R.id.switchJoin)
        auth = Firebase.auth
        var chefs_1 = ""
        var chefs_2 = ""
        var diet_db = ""


        getdata = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var clubid = bundle.getString("id")
                    if (clubid != null) {

                        var chef1 = p0.child(clubid).child("c1").getValue().toString()
                        var chef2 = p0.child(clubid).child("c2").getValue().toString()
                        var par = p0.child(clubid).child("participants").getValue().toString()

                        root.findViewById<TextView>(R.id.parti).text = setPartiWithChefs(chef1, chef2, root, par)
                        root.findViewById<TextView>(R.id.chefs).text = chef1
                        root.findViewById<TextView>(R.id.chefs2).text = chef2
                        root.findViewById<TextView>(R.id.date).text = p0.child(clubid).child("date").getValue().toString()
                        root.findViewById<TextView>(R.id.dinner).text = p0.child(clubid).child("dinner").getValue().toString()
                        root.findViewById<TextView>(R.id.note).text = p0.child(clubid).child("note").getValue().toString()
                        chefs_1 = root.findViewById<TextView>(R.id.chefs).text.toString()
                        chefs_2 = root.findViewById<TextView>(R.id.chefs2).text.toString()
                        diet_db = p0.child(clubid).child("diets").getValue().toString()

                        setId(clubid)
                    }
                }
                lottie.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        getdataU = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var chf1Diet = ""
                var chf2Diet = ""
                var clubid = bundle?.getString("id")
                val userid = auth.currentUser?.uid
                if (userid != null) {
                    var nameArr = p0.child(userid).child("fname").getValue().toString().split(" ")
                    var roomnumber = p0.child(userid).child("number").getValue().toString()
                    var diet = p0.child(userid).child("diet").getValue().toString()
                    var identity = "${nameArr[0]} - $roomnumber"

                    for (i in p0.children) {
                        var nb = i.child("number").getValue().toString()
                        var nameArrRes = i.child("fname").getValue().toString().split(" ")
                        var ChefIdentity = "${nameArrRes[0]} - $nb"


                        if ((chefs_1).equals(ChefIdentity)) {
                            chf1Diet = i.child("diet").getValue().toString()
                            break

                        } else if ((chefs_2).equals(ChefIdentity)) {
                            chf2Diet = i.child("diet").getValue().toString()
                            break
                        }
                    }

                    setChefDiets(chf1Diet, chf2Diet, diet_db)
                    setSwitchStatus(checked, identity)
                    if (clubid != null) {
                        listenerOnChange(checked,roomnumber, diet, chf1Diet, chf2Diet, clubid, identity)
                    }


                }
            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        databaseU.addValueEventListener(getdataU)
        databaseU.addListenerForSingleValueEvent(getdataU)



        root.findViewById<ImageView>(R.id.edit).setOnClickListener() {

            val tempFrag = EditFoodFragment()
            tempFrag.arguments = editBundle
            requireFragmentManager().beginTransaction().add(R.id.nav_host_fragment, tempFrag).addToBackStack(null).commit()
        }

        return root
    }

    private fun setId(clubid: String){
        editBundle = Bundle()
        editBundle.putString("id", clubid)
    }

    @SuppressLint("SetTextI18n")
    private fun listenerOnChange(switch: Switch, rn: String, diet: String, ch1Diet: String, ch2Diet: String, clubid: String, identiti: String){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            val chefs_1 = chefs.text.toString()
            val chefs_2 = chefs2.text.toString()
            var parti_string: String
            var diet_string: String
            if (isChecked) {
                if (chefs_1.contains(identiti) || chefs_2.contains(identiti)){
                    switch.isChecked = true
                } else {
                    parti_string = checkIfTextviewIsEmpty(parti, identiti)
                    diet_string = checkIfTextviewIsEmpty(diets, diet)

                    parseToDatabase(clubid,diet_string, parti_string, ch1Diet, ch2Diet, chefs_1, chefs_2)
                }

            } else {
                if (chefs_1.contains(identiti) || chefs_2.contains(identiti)){
                    switch.isChecked = true
                } else {
                    parti_string = removeFromDatabaseAndTextview(parti, identiti)
                    diet_string = removeFromDatabaseAndTextview(diets, diet)

                    parseToDatabase(clubid,diet_string, parti_string, ch1Diet, ch2Diet, chefs_1, chefs_2)
                }
            }
        }
    }

    fun setSwitchStatus(switch: Switch, identiti: String){
        if ( parti.text.toString().contains(identiti)){ switch.isChecked = true}
    }

    fun setChefDiets(ch1Diet: String, ch2Diet: String, diet_db: String){
        if (diet_db.isEmpty()){
            if(ch1Diet != "" || ch2Diet != ""){
                if(ch1Diet != "" && ch2Diet != ""){
                    diets.text = "$ch1Diet, $ch2Diet"
                } else if (ch1Diet != ""){
                    diets.text = ch1Diet
                } else  if (ch2Diet != ""){
                    diets.text = ch2Diet
                }
            }
        } else {
            if(ch1Diet != "" || ch2Diet != ""){
                if(ch1Diet != "" && ch2Diet != ""){
                    diets.text = "$ch1Diet, $ch2Diet, $diet_db"
                } else if (ch1Diet != ""){
                    diets.text = "$ch1Diet, $diet_db"
                } else  if (ch2Diet != ""){
                    diets.text = "$ch2Diet, $diet_db"
                }
            } else {
                diets.text = diet_db
            }
        }
    }

    fun checkIfTextviewIsEmpty(textview: TextView, input: String): String {
        var string = textview.text.toString()
        if (string.isEmpty()) {
            string = input
        } else {
            string = string + ", " + input
        }
        textview.text = string
        return string
    }

    fun removeChefFromParticipants (chef:String, st: String): String {
        var string = st
        if (!chef.contains("NA")) {
            if (string.contains("$chef,")) {
                string = string.replace("$chef, ", "")
            } else {
                string = string.replace("$chef", "")
            }
        }
        return string
    }

    fun removeChefDietFromDiets(chefDiet:String, st: String): String {
        var string = st
        if (string.contains(chefDiet)) {
            if (string.contains("$chefDiet, ")) {
                string = string.replace("$chefDiet, ", "")
            } else {
                string = string.replace("$chefDiet", "")
            }
        }
        return string
    }

    fun removeFromDatabaseAndTextview(textview: TextView, input: String): String {
        var string = textview.text.toString()
        if (string.contains(", $input")) {
            string = string.replace(", $input", "")
        } else {
            string = string.replace(input, "")
        }
        textview.text = string
        return string
    }

    fun pushToDatabase(id: String, path: String, string: String) {
        database.child(id).child(path).setValue(string)
    }

    fun parseToDatabase(clubid: String, diet_string:String, parti_string: String, ch1Diet: String,ch2Diet: String, ch1: String, ch2: String){
        var string_parti = ""
        var string_diet = ""

        string_parti = removeChefFromParticipants(ch1, parti_string)
        string_parti = removeChefFromParticipants(ch2, string_parti)

        string_diet = removeChefDietFromDiets(ch1Diet, diet_string)
        string_diet = removeChefDietFromDiets(ch2Diet, string_diet)

        pushToDatabase(clubid, "participants", string_parti)
        pushToDatabase(clubid, "diets", string_diet)
    }

    fun setPartiWithChefs(chef1: String, chef2: String, root: View, par: String): String {
        var st: String

        val chefInfo1 = checkIfChefIsNull(chef1)
        val chefInfo2 = checkIfChefIsNull(chef2)

        var str = "${chefInfo1[0]}${chefInfo2[0]}$par"
        var participant = str.split(",")
        val stri = participant[participant.size - 1]
        if (stri.equals(" ")) {
            st = str.substring(0, str.length - 2)
        } else {
            st = str
        }

        root.findViewById<TextView>(R.id.chefs).text = chefInfo1[1]
        root.findViewById<TextView>(R.id.chefs2).text = chefInfo2[1]

        return st
    }

    fun checkIfChefIsNull(chef: String): ArrayList<String> {
        var str = arrayListOf<String>()

        if (chef.equals("None")) {
            str.add("")
            str.add(chef)
        } else {
            str.add("$chef, ")
            str.add(chef)
        }
        return str
    }

}