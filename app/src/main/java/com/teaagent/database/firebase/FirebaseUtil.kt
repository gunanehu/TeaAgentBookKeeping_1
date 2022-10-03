package com.teaagent.data

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.teaagent.AppHelper
import com.teaagent.database.TeaAgentsharedPreferenceUtil
import com.teaagent.database.TeaAgentsharedPreferenceUtil.addToPreferenceTabId
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.domain.firemasedbEntities.Customer
import com.teaagent.domain.firemasedbEntities.PhoneUser
import com.teaagent.repo.FirebaseEntryAddedCallback


object FirebaseUtil {
    var mDatabase =
        FirebaseDatabase.getInstance(/*"https://teaagentbookkeeping-default-rtdb.asia-southeast1.firebasedatabase.app"*/)
            .getReference();


    var firestoreDb = FirebaseFirestore.getInstance()
    var TAG = "FirebaseUtil"
    val phoneUser: PhoneUser = getCurrentPhoneUser()
    private var firebaseEntryAddedCallback: FirebaseEntryAddedCallback? = null


    //firebeCloudStorage
    private var tablePhoneUser: CollectionReference? = null
    private var tableCustomer: CollectionReference? = null
    private var tableCollectionEntry: CollectionReference? = null

    init {
        tableCustomer = firestoreDb.collection("Customer")
        tablePhoneUser = firestoreDb.collection("PhoneUser")
        tableCollectionEntry = firestoreDb.collection("Transactions")
        //TODO use only one in lifetime for that user
//        addPhoneUser(account.id, phoneUser)
    }


    fun setFirebaseEntryAddedCallback(
        f: FirebaseEntryAddedCallback
    ) {
        firebaseEntryAddedCallback = f
    }


    fun addCustomer(customer: Customer?) {
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

    }

    fun addPhoneUser(id: String?, phoneUser: PhoneUser?) {
        val phoneId: String? = TeaAgentsharedPreferenceUtil.getAppId()

        if (phoneId.equals("")) {
            if (phoneUser != null) {
                if (id != null) {
                    addToPreferenceTabId(id)
                }//save to local preference such that u never call again and again/
                // / todo check after installation to check the same phoneid in firebase

                tablePhoneUser?.add(phoneUser)
                    ?.addOnFailureListener(OnFailureListener { e ->
                        {
                            Log.e(
                                TAG,
                                "addPhoneUser OnFailureListener documentReference " + e.message
                            )
                        }
                    })
                    ?.addOnSuccessListener(OnSuccessListener<DocumentReference?> { documentReference -> //this gets triggered when I run
                        Log.i(
                            TAG,
                            "addPhoneUser OnSuccessListener  documentReference " + documentReference
                        )
                    })
                    ?.addOnCompleteListener(OnCompleteListener<DocumentReference?> { task -> //this also gets triggered when I run
                        Log.i(
                            TAG,
                            "  addPhoneUser OnCompleteListener documentReference " + task.result
                        )
                    })
            }
        }
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

    ///get calls

    fun getCurrentPhoneUser(): PhoneUser {
        return PhoneUser(AppHelper.getInstance().uniqueUserID)

    }

    fun getAllCustomers(): Task<QuerySnapshot>? {
        val query = tableCustomer?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)
        return query?.get()
    }

    fun getByNameAndDate(
        customerName: String,
        convertedTimestampDate: String
    ): Task<QuerySnapshot>? {
        val query = tableCollectionEntry
            ?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)
            ?.whereEqualTo("customerName", customerName)
            ?.whereEqualTo("convertedTimestampDate", convertedTimestampDate)
        return query?.get()
    }


    fun getByName(customerName: String): Task<QuerySnapshot>? {
        val query = tableCollectionEntry
            ?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)
            ?.whereEqualTo("customerName", customerName)
            ?.orderBy("timestamp", Query.Direction.ASCENDING)
        return query?.get()
    }
}