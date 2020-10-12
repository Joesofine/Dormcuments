import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dormcuments.R
import com.example.dormcuments.ui.foodclub.EditFoodFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
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
        val bundle = this.arguments
        val checked = root.findViewById<Switch>(R.id.switchJoin)
        var ch1 = ""
        var ch2 = ""
        auth = Firebase.auth



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
                        root.findViewById<TextView>(R.id.note).text = p0.child(clubid).child("note").getValue().toString()
                        setId(clubid)
                    }
                }

            }
            override fun onCancelled(p0: DatabaseError) { println("err") }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

        getdataU = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var clubid = bundle?.getString("id")
                val userid = auth.currentUser?.uid
                if (userid != null) {
                    var roomnumber = p0.child(userid).child("number").getValue().toString()
                    var diet = p0.child(userid).child("diet").getValue().toString()

                    setSwitchStatus(checked,roomnumber)
                    if (clubid != null) {
                        listenerOnChange(checked,roomnumber, diet, clubid)
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
    private fun listenerOnChange(switch: Switch, rn: String, diet: String, clubid: String){
        switch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            val chefs = chefs.text.toString().split(", ")
            var st = ""
            var fst = ""
            if (isChecked) {
                if (chefs[0].contains(rn.substring(1,3)) || chefs[1].contains(rn.substring(1,3))){
                    switch.isChecked = true
                } else {
                    if (parti.text.toString().isEmpty()) {
                        parti.text = rn
                    } else {
                        st = parti.text.toString() + ", " + rn
                        parti.text = st
                    }
                    if (diets.text.toString().isEmpty()) {
                        diets.text = diet
                    } else {
                        fst = diets.text.toString() + ", " + diet
                        diets.text = fst
                    }

                    if (!chefs[0].contains("NA")) {
                        val ch1 = chefs[0]
                        if (parti.text.toString().contains("9$ch1,")) {
                            st = st.replace("9$ch1, ", "")
                        } else {
                            st = st.replace("9$ch1", "")
                        }
                    }
                    if (!chefs[1].contains("NA")) {
                        val ch2 = chefs[1]
                        if (parti.text.toString().contains("9$ch2,")) {
                            st = st.replace("9$ch2, ", "")
                        } else {
                            st = st.replace("9$ch2", "")
                        }
                    }
                    database.child(clubid).child("participants").setValue(st).addOnSuccessListener {
                        Toast.makeText(context, "succesfully joined foodclub", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {}

                    database.child(clubid).child("diets").setValue(st).addOnSuccessListener {}
                        .addOnFailureListener {}
                }

            } else {
                if (chefs[0].contains(rn.substring(1, 3)) || chefs[1].contains(rn.substring(1, 3))) {
                    switch.isChecked = true
                } else {

                    if (parti.text.toString().contains(", $rn")) {
                        st = parti.text.toString().replace(", $rn", "")
                    } else {
                        st = parti.text.toString().replace(rn, "")
                    }
                    if (diets.text.toString().contains(", $diet")) {
                        fst = diets.text.toString().replace(", $diet", "")
                    } else {
                        fst = diets.text.toString().replace(diet, "")
                    }
                    parti.text = st
                    diets.text = fst

                    if (!chefs[0].contains("NA")) {
                        val ch1 = chefs[0]
                        if (parti.text.toString().contains("9$ch1,")) {
                            st = st.replace("9$ch1, ", "")
                        } else {
                            st = st.replace("9$ch1", "")
                        }
                    }
                    if (!chefs[1].contains("NA")) {
                        val ch2 = chefs[1]
                        if (parti.text.toString().contains("9$ch2,")) {
                            st = st.replace("9$ch2, ", "")
                        } else {
                            st = st.replace("9$ch2", "")
                        }
                    }

                    database.child(clubid).child("participants").setValue(st).addOnSuccessListener {
                        Toast.makeText(context, "Sign up deleted", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { }
                    database.child(clubid).child("diets").setValue(fst).addOnSuccessListener {
                    }.addOnFailureListener {}
                }
            }
        }
    }

    private fun setSwitchStatus(switch: Switch, rn: String){
        if ( parti.text.toString().contains(rn)){ switch.isChecked = true}
    }

}