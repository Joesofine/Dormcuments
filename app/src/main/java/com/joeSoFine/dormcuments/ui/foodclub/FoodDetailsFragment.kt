import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
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
        SmartTools.setUpOnBackPressed(requireActivity())

        val root = inflater.inflate(R.layout.fragment_food_details, container, false)
        var progressBar = root.findViewById<ProgressBar>(R.id.progressBar9)
        progressBar.visibility = View.VISIBLE
        val bundle = this.arguments
        val checked = root.findViewById<Switch>(R.id.switchJoin)
        var ch1 = ""
        var ch2 = ""
        auth = Firebase.auth
        var chefs = "".split("")


        getdata = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {

                if (bundle != null) {
                    var clubid = bundle.getString("id")
                    if (clubid != null) {

                        var w1 = p0.child(clubid).child("c1").getValue().toString().substring(1,3)
                        var w2 = p0.child(clubid).child("c2").getValue().toString().substring(1,3)
                        var par = p0.child(clubid).child("participants").getValue().toString()

                        if(w1.equals("on") || w2.equals("on")){
                            if(w1.equals("on") && w2.equals("on")){
                                w1 = "NA"
                                w2 = "NA"
                                root.findViewById<TextView>(R.id.parti).text = par

                            } else if (w1.equals("on")){
                                w1 = "NA"
                                ch2 = p0.child(clubid).child("c2").getValue().toString()
                                if (par.isEmpty()){
                                    root.findViewById<TextView>(R.id.parti).text = "$ch2"
                                } else {
                                    root.findViewById<TextView>(R.id.parti).text = "$ch2, $par"
                                }
                            } else  if (w2.equals("on")){
                                w2 = "NA"
                                ch1 = p0.child(clubid).child("c2").getValue().toString()
                                if (par.isEmpty()){
                                    root.findViewById<TextView>(R.id.parti).text = "$ch1"
                                } else {
                                    root.findViewById<TextView>(R.id.parti).text = "$ch1, $par"
                                }
                            }
                        } else {
                            ch1 = p0.child(clubid).child("c1").getValue().toString()
                            ch2 = p0.child(clubid).child("c2").getValue().toString()
                            if (par.isEmpty()){
                                root.findViewById<TextView>(R.id.parti).text = "$ch1, $ch2"
                            } else {
                                root.findViewById<TextView>(R.id.parti).text = "$ch1, $ch2, $par"
                            }
                        }


                        root.findViewById<TextView>(R.id.chefs).text = "$w1, $w2"
                        root.findViewById<TextView>(R.id.date).text = p0.child(clubid).child("date").getValue().toString()
                        root.findViewById<TextView>(R.id.dinner).text = p0.child(clubid).child("dinner").getValue().toString()
                        root.findViewById<TextView>(R.id.diets).text = p0.child(clubid).child("diets").getValue().toString()
                        root.findViewById<TextView>(R.id.note).text = p0.child(clubid).child("note").getValue().toString()
                        chefs = root.findViewById<TextView>(R.id.chefs).text.toString().split(", ")

                        setId(clubid)
                    }
                }
                progressBar.visibility = View.GONE
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
                    var roomnumber = p0.child(userid).child("number").getValue().toString()
                    var diet = p0.child(userid).child("diet").getValue().toString()

                    for (i in p0.children){
                        var nb = i.child("number").getValue().toString().substring(1,3)
                        if (nb.equals(chefs[0])) {
                            chf1Diet = i.child("diet").getValue().toString()
                            break
                        } else if (nb.equals(chefs[1])) {
                            chf2Diet = i.child("diet").getValue().toString()
                            break
                        }
                    }


                    setChefDiets(chf1Diet,chf2Diet)
                    setSwitchStatus(checked,roomnumber)
                    if (clubid != null) {
                        listenerOnChange(checked,roomnumber, diet, chf1Diet, chf2Diet, clubid)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @SuppressLint("SetTextI18n")
    private fun listenerOnChange(switch: Switch, rn: String, diet: String, ch1Diet: String, ch2Diet: String, clubid: String){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            val chefs = chefs.text.toString().split(", ")
            val room = rn.substring(1,3)
            var parti_string = ""
            var diet_string = ""
            if (isChecked) {
                if (chefs[0].contains(room) || chefs[1].contains(room)){
                    switch.isChecked = true
                } else {
                    parti_string = checkIfTextviewIsEmpty(parti, rn)
                    diet_string = checkIfTextviewIsEmpty(diets, diet)

                    parti_string = removeChefFromParticipants(chefs[0], parti_string)
                    parti_string = removeChefFromParticipants(chefs[1], parti_string)

                    diet_string = removeChefDietFromDiets(ch1Diet, diet_string)
                    diet_string = removeChefDietFromDiets(ch2Diet, diet_string)

                    pushToDatabase(clubid, "participants", parti_string)
                    pushToDatabase(clubid, "diets", diet_string)
                }

            } else {
                if (chefs[0].contains(room) || chefs[1].contains(room)) {
                    switch.isChecked = true
                } else {

                    parti_string = removeFromDatabaseAndTextview(parti, rn)
                    diet_string = removeFromDatabaseAndTextview(diets, diet)

                    parti_string = removeChefFromParticipants(chefs[0], parti_string)
                    parti_string = removeChefFromParticipants(chefs[1], parti_string)

                    diet_string = removeChefDietFromDiets(ch1Diet, diet_string)
                    diet_string = removeChefDietFromDiets(ch2Diet, diet_string)

                    pushToDatabase(clubid, "participants", parti_string)
                    pushToDatabase(clubid, "diets", diet_string)
                }
            }
        }
    }

    fun setSwitchStatus(switch: Switch, rn: String){
        if ( parti.text.toString().contains(rn)){ switch.isChecked = true}
    }

    fun setChefDiets(ch1Diet: String, ch2Diet: String){
        if(ch1Diet != "" || ch2Diet != ""){
            if(ch1Diet != "" && ch2Diet != ""){
                diets.text = "$ch1Diet, $ch2Diet"
            } else if (ch1Diet != ""){
                diets.text = ch1Diet
            } else  if (ch2Diet != ""){
                diets.text = ch2Diet
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
            if (string.contains("9$chef,")) {
                string = string.replace("9$chef, ", "")
            } else {
                string = string.replace("9$chef", "")
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
}