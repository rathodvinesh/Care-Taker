package com.example.caretaker.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caretaker.adapter.VolunteerApplicationsAdapter
import com.example.caretaker.databinding.FragmentApplcationsBinding
import com.example.caretaker.models.VolunteerApplication
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject

class ApplicationsFragment : Fragment() {
    private lateinit var binding: FragmentApplcationsBinding
    private lateinit var adapter: VolunteerApplicationsAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var volAppliArrayList:ArrayList<VolunteerApplication>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplcationsBinding.inflate(layoutInflater, container, false)


        recyclerView = binding.rvAppli
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        volAppliArrayList = arrayListOf()

        adapter = VolunteerApplicationsAdapter(volAppliArrayList)

        EventChangeListner()

        return binding.root
    }

    private fun EventChangeListner() {
        db = FirebaseFirestore.getInstance()
        db.collection("VOLUNTEERS").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if(error!=null){
                    Log.e("Firestore error: ",error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        if (dc.document.toObject(VolunteerApplication::class.java).hireStatus != "Un-Hired") {
                            volAppliArrayList.add(dc.document.toObject(VolunteerApplication::class.java))
                        }
                    }
                }
//                Log.d("datahere", volAppliArrayList.toString())
//                adapter.notifyDataSetChanged()
//                        adapter.notifyItemInserted(volArrayList.size-1)
                recyclerView.adapter=adapter
            }

        })
    }
}



























//class ApplicationsFragment : Fragment() {
//
//    public lateinit var auth: FirebaseAuth
//    private lateinit var navController: NavController
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
////        navController = findNavController()
//
//        return ComposeView(requireContext()).apply {
//            // Dispose of the Composition when the view's LifecycleOwner
//            // is destroyed
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setContent {
//                MaterialTheme {
//                    // In Compose world
//                    concessionApplicationScreen()
//
//                }
//            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun concessionApplicationScreen() {
//    var list by remember { mutableStateOf(emptyList<Volunteer>()) }
//    var showProgressBar by remember { mutableStateOf(true) } // New state for progress bar
//
//    if (showProgressBar) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator() // Circular indeterminate progress indicator
//        }
//    }
//
////    showProgressBar = true
//
//
//    DisposableEffect(firebaseConfig.rootReference) {
//        showProgressBar = true
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
////                showProgressBar = false
//
//                val newMessages =
//                    snapshot.children.mapNotNull { it.getValue(Volunteer::class.java) }
//                Log.d("DataFetch", "Fetched messages: $newMessages")
//                list = newMessages
//                showProgressBar = false
//
////                loading = false
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                showProgressBar = false
//
//                Log.e("DataFetch", "Data fetch error: $error")
////                loading = false
//            }
//        }
//
//        var auth: FirebaseAuth = Firebase.auth
//        val email = auth.currentUser?.email
//        val uid = auth.currentUser?.uid
////        val clgId = email?.substring(0, 11)?.toUpperCase()
//        firebaseConfig.volunteerRef.child("$uid").addValueEventListener(listener)
////        database.child(studentClassName).child("ALL").addValueEventListener(listener)
//
//        onDispose {
////            firebaseConfig.volunteerRef.child("VU1F2122010").removeEventListener(listener)
//            Log.d("DataFetch", "Listener removed")
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Display your status text, divider, and volunteer list using LazyColumn
//        Text(text = "Your Status", modifier = Modifier.padding(10.dp))
//        Divider()
//        if (list.isNotEmpty()) {
//            LazyColumn {
//                items(list) { volunteer ->
//                    ConcessionListItem(
//                        volunteer = volunteer,
//                        onDeleteClick = {
//                            // Handle delete action
//                            // You can call ClientActionData function here to delete the volunteer from Firestore
//                        }
//                    )
//                }
//            }
//        } else {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "No History Found")
//            }
//        }
//    }
//
////    Column(
////        modifier = Modifier.fillMaxSize(),
////        horizontalAlignment = Alignment.CenterHorizontally
////    ) {
////        Text(text = "Your Status", modifier = Modifier.padding(10.dp))
////        Divider()
////        if (list.isNotEmpty()) {
////            LazyColumn {
////                items(list) { item ->
//////                    showProgressBar = false
////                    ConcessionListItem(item,
//////                        navController,
////                    )
////
////                }
////            }
////        } else {
////            Box(
////                modifier = Modifier.fillMaxSize(),
////                contentAlignment = Alignment.Center
////            ) {
//////                showProgressBar = false
////                Text(text = "No History Found")
////
////            }
////        }
////    }
//
//
//
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ConcessionListItem(
//    volunteer: Volunteer,
//    onDeleteClick: () -> Unit // Callback function for delete button click
//) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp)
//        ) {
//            // Display volunteer details
//            Text(text = "Name: ${volunteer.name}", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Age: ${volunteer.age}", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Gender: ${volunteer.gender}", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Contact: ${volunteer.contact}", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Service: ${volunteer.service}", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Amount: ${volunteer.amount}", fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(text = "Status: ${volunteer.status}", fontWeight = FontWeight.Bold)
//
//            // Button row for update and delete actions
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = {
//                        // Handle update action
//                    },
//                    enabled = volunteer.status !in listOf("Accepted", "Rejected")
//                ) {
//                    Text(text = "Update")
//                }
//
//                Button(
//                    onClick = onDeleteClick,
//                    enabled = volunteer.status !in listOf("Accepted", "Rejected")
//                ) {
//                    Text(text = "Delete")
//                }
//            }
//        }
//    }
//}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ConcessionListItem(
//    item: Volunteer,
////    source: String,
////    destination: String,
////    classs: String,
////    duration: String,
////    voucherNo: String,
////    appliedDate: String
////    navController: NavController
//) {
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var showSnackbar by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp),
////        elevation = 4.dp
//    ) {
//
////        showProgressBar = true
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.Start, modifier = Modifier
//                        .weight(1f)
//                ) {
//                    Text(text = "Name : ")
//                    Text(text = item.name.toString(), fontWeight = FontWeight.Bold)
////                    showProgressBar = false
//
//                }
////                Spacer(modifier = Modifier.weight(1f))
//                Row(
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Text(text = "Age : ")
//                    Text(text = item.age.toString(), fontWeight = FontWeight.Bold)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.Start, modifier = Modifier
//                        .weight(1f)
//                ) {
//                    Text(text = "Gender : ")
//                    Text(text = item.gender.toString(), fontWeight = FontWeight.Bold)
//                }
////                Spacer(modifier = Modifier.weight(1f))
//                Row(
//                    horizontalArrangement = Arrangement.End, modifier = Modifier
//                        .weight(1f)
//                ) {
//                    Text(text = "Contact : ")
//                    Text(text = item.contact.toString(), fontWeight = FontWeight.Bold)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Text(text = "Service : ")
//                Text(text = item.service.toString(), fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Text(text = "Amount : ")
//                Text(text = item.amount.toString(), fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Text(text = "Status : ")
//                Text(text = item.status, fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.Start, modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth()
//                ) {
//                    Button(
//                        onClick = {
////                            navController.navigate(R.id.action_views_to_update)
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 4.dp),
//                        colors = ButtonDefaults.buttonColors(Color.Blue),
//                        enabled = item.status !in listOf("Accepted", "Rejected")
//                    ) {
//                        Text(text = "Update")
//
//                    }
//                }
//                Row(
//                    horizontalArrangement = Arrangement.End,
//                    modifier = Modifier
//                        .weight(1f),
//                )
//                {
//                    Button(
//                        onClick = {
//                            showDeleteDialog = true
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(start = 4.dp),
//                        colors = ButtonDefaults.buttonColors(Color.Red),
//                        enabled = item.status !in listOf("Accepted", "Rejected")
//                    ) {
//                        Text(text = "Delete")
//                    }
//                }
//            }
//        }
//
//        if (showSnackbar) {
//            Snackbar(
//                modifier = Modifier.padding(16.dp),
//                content = {
//                    Text("Item deleted")
//                }
//            )
//        }
//
//
//        if (showDeleteDialog) {
//            AlertDialog(
//                onDismissRequest = { showDeleteDialog = false },
//                title = {
//                    Text(text = "Delete Item")
//                },
//                text = {
//                    Text(text = "Are you sure you want to delete this item?")
//                },
//                confirmButton = {
//                    TextButton(
//                        onClick = {
//
//                            var auth: FirebaseAuth = Firebase.auth
//                            val email = auth.currentUser?.email
//                            val uid = auth.currentUser?.uid
////                            val clgId = email?.substring(0, 11)?.uppercase(Locale.ROOT)
//
//                            val feild= "uid"
//                            val value = "${uid}"
//
//                            val query: Query = firebaseConfig.volunteerRef.child("$uid").orderByChild(feild).equalTo(value)
//
//                            query.addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    for(dataSnapshot in snapshot.children){
//                                        dataSnapshot.ref.removeValue()
//                                    }
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                    TODO("Not yet implemented")
//                                }
//                            })
//
////                            val timeForRetrieve = ArrayList<String>()
////
////                            firebaseConfig.userRef.child("$clgId/CLIST")
////                                .addValueEventListener(object : ValueEventListener {
////                                    @RequiresApi(Build.VERSION_CODES.O)
////                                    override fun onDataChange(snapshot: DataSnapshot) {
////                                        if (snapshot.exists()) {
////                                            for (snapshot in snapshot.children) {
////                                                val key = snapshot.key
////                                                if (key != null) {
////                                                    timeForRetrieve.add(key)
//////                            Toast.makeText(this@concession_form, key, Toast.LENGTH_SHORT).show()
////                                                }
////                                            }
////
////                                            // Get the last element from the list
////                                            val lastTime = timeForRetrieve.lastOrNull()
////
////                                            firebaseConfig.userRef.child("$clgId/CLIST")
////                                                .addValueEventListener(object :
////                                                    ValueEventListener {
////                                                    override fun onDataChange(snapshot: DataSnapshot) {
////                                                        if (snapshot.exists()) {
////                                                            firebaseConfig.userRef.child("$clgId/CLIST").removeValue()
////                                                                .addOnSuccessListener {
////
////                                                                }
////                                                                .addOnFailureListener {
////
////                                                                }
////                                                        }
////                                                    }
////
////                                                    override fun onCancelled(error: DatabaseError) {
////                                                        // Handle the error
////                                                    }
////                                                })
////                                        }
////                                    }
////
////                                    override fun onCancelled(error: DatabaseError) {
//////                                    Toast.makeText(requireContext(), "Cannot get key", Toast.LENGTH_SHORT).show()
////                                    }
////                                })
//
//                            showDeleteDialog = false
//                        }
//                    ) {
//                        Text(text = "Yes")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showDeleteDialog = false }) {
//                        Text(text = "No")
//
//                    }
//                }
//            )
//        }
//    }
//}