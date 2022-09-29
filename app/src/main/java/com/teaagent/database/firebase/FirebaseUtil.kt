package com.teaagent.data

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.domain.firemasedbEntities.Customer
import com.teaagent.domain.firemasedbEntities.PhoneUser
import com.teaagent.repo.FirebaseEntryAddedCallback


object FirebaseUtil {
    var mDatabase =
        FirebaseDatabase.getInstance("https://teaagentbookkeeping-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference();


    var firestoreDb = FirebaseFirestore.getInstance()

    var TAG = "FirebaseUtil"

    /*  //firebaserealdb
      private var tablePhoneUserRealDB: DatabaseReference? = null
      private var tableCustomerRealDB: DatabaseReference? = null*/

    //firebeCloudStorage
    private var tablePhoneUser: CollectionReference? = null
    private var tableCustomer: CollectionReference? = null
    private var tableCollectionEntry: CollectionReference? = null


    val phoneUser: PhoneUser = getCurrentPhoneUser()
    private var firebaseEntryAddedCallback: FirebaseEntryAddedCallback? = null

    fun setFirebaseEntryAddedCallback(
        f: FirebaseEntryAddedCallback
    ) {
        firebaseEntryAddedCallback = f
    }
    fun getCurrentPhoneUser(): PhoneUser {
        return PhoneUser("1212", "Guna", "785665")
    }

    init {
        /*  tablePhoneUserRealDB = mDatabase.child("PhoneUser");
          tableCustomerRealDB = mDatabase.child("Customer");*/

        tableCustomer = firestoreDb.collection("Customer")
        tablePhoneUser = firestoreDb.collection("PhoneUser")
        tableCollectionEntry = firestoreDb.collection("Transactions")
        //TODO use only one in lifetime for that user
        addPhoneUser(phoneUser)
    }


    fun addCustomer(customer: Customer?) {
        /*   val key: String? = tableCustomer?.push()?.getKey()
           Log.v(TAG, "key " + key)
           customer?.phoneUserId ?: phoneUser.phoneUserId
           if (key != null) {
               tableCustomer?.child(key)?.setValue(customer)
           }*/

        if (customer != null) {
            tableCustomer?.add(customer)
                ?.addOnFailureListener(OnFailureListener { e ->
                    {
                        Log.e(TAG, "OnSuccessListener documentReference " + e.message)

                    }
                })
                ?.addOnSuccessListener(OnSuccessListener<DocumentReference?> { documentReference -> //this gets triggered when I run
                    Log.i(TAG, "OnSuccessListener documentReference " + documentReference.id)
                    firebaseEntryAddedCallback?.onCustomerAddedSuccessfully(documentReference.id)
                })
                ?.addOnCompleteListener(OnCompleteListener<DocumentReference?> { task -> //this also gets triggered when I run
                    Log.i(TAG, " OnCompleteListener documentReference " + task.result.get())
                })
        }
        /* tablePhoneUser?.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 val map = dataSnapshot.value as Map<String, Any>?
                 Log.d(TAG, "Value is: $map")
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.w(TAG, "Failed to read value.", error.toException())
             }
         })*/
    }

    fun addPhoneUser(phoneUser: PhoneUser?) {
        if (phoneUser != null) {
            tablePhoneUser?.add(phoneUser)

                ?.addOnFailureListener(OnFailureListener { e ->
                    {
                        Log.e(TAG, "OnSuccessListener documentReference " + e.message)

                    }
                })
                ?.addOnSuccessListener(OnSuccessListener<DocumentReference?> { documentReference -> //this gets triggered when I run
                    Log.i(TAG, "OnFailureListener documentReference " + documentReference)
                })
                ?.addOnCompleteListener(OnCompleteListener<DocumentReference?> { task -> //this also gets triggered when I run
                    Log.i(TAG, " OnCompleteListener documentReference " + task.result)
                })
        }
        /*  val key: String? = tablePhoneUserRealDB?.push()?.getKey()
          Log.v(TAG, "key " + key)
          phoneUser?.phoneUserId ?: key
          if (key != null) {
              tablePhoneUserRealDB?.child(key)?.setValue(phoneUser)
          }
  */
        // Read from the database
        // Read from the database

        /* tablePhoneUser?.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 val map = dataSnapshot.value as Map<String, Any>?
                 Log.d(TAG, "Value is: $map")
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.w(TAG, "Failed to read value.", error.toException())
             }
         })*/
    }


    public suspend fun getAllCustomers(): Task<QuerySnapshot>? {
        val query = tableCustomer?.whereEqualTo("phoneUserId", getCurrentPhoneUser().phoneUserId)
        return query?.get()

    }

    fun addCollectionEntry(collectionEntry: CollectionEntry?) {
        if (collectionEntry != null) {
            tableCollectionEntry?.add(collectionEntry)
                ?.addOnFailureListener(OnFailureListener { e ->
                    {
                        Log.e(TAG, "OnSuccessListener documentReference " + e.message)

                    }
                })
                ?.addOnSuccessListener(OnSuccessListener<DocumentReference?> { documentReference -> //this gets triggered when I run
                    Log.i(TAG, "OnSuccessListener documentReference " + documentReference.id)
                })
                ?.addOnCompleteListener(OnCompleteListener<DocumentReference?> { task -> //this also gets triggered when I run
                    Log.i(TAG, " OnCompleteListener documentReference " + task.result.get())
                })
        }
    }

    fun getByNameAndDate(customerName: String, timestamp: Long?): Task<QuerySnapshot>? {
        var entryTimestampDate = timestamp?.div((1000 * 60 * 60 * 24))
//        var  inDbTimestampDate= timestamp?.div((1000 * 60 * 60 * 24))
        val query = tableCollectionEntry
//            ?.whereEqualTo("phoneUserId", getCurrentPhoneUser().phoneUserId)
//            ?.whereEqualTo("customerName", customerName)
            ?.whereEqualTo("quantity", 1)
            ?.whereEqualTo("labourAmount", 1)

//        tableCollectionEntry?.document("CJxgOxtYCwYIyDdKkwSs")
//            ?.collection("phoneUserAndCustomer.")?.document("hi")
//


//             ?.orderBy("date")?.startAt( entryTimestampDate)
        return query?.get()
    }
}