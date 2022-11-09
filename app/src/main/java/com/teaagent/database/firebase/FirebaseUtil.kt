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
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.PhoneUser
import com.teaagent.domain.firemasedbEntities.TimerLog
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import com.teaagent.repo.FirebaseEntryAddedCallback


object FirebaseUtil {
    var mDatabase =
        FirebaseDatabase.getInstance(/*"https://teaagentbookkeeping-default-rtdb.asia-southeast1.firebasedatabase.app"*/)
            .getReference();


    var firestoreDb = FirebaseFirestore.getInstance()
    var TAG = "FirebaseUtil"
//    val phoneUser: PhoneUser = getCurrentPhoneUser()
    private var firebaseEntryAddedCallback: FirebaseEntryAddedCallback? = null


    //firebeCloudStorage
    private var tablePhoneUser: CollectionReference? = null
    private var tableTradeAnalysisEntry: CollectionReference? = null
    private var tableCollectionEntry: CollectionReference? = null
    private var tableTimerLogEntry: CollectionReference? = null

    init {
        tableTradeAnalysisEntry = firestoreDb.collection("TradeAnalysisEntry")
        tablePhoneUser = firestoreDb.collection("PhoneUser")
        tableCollectionEntry = firestoreDb.collection("BalanceTx")
        tableTimerLogEntry = firestoreDb.collection("TimerLog")
        //TODO use only one in lifetime for that user
//        addPhoneUser(phoneUser)
    }


    fun setFirebaseEntryAddedCallback(
        f: FirebaseEntryAddedCallback
    ) {
        firebaseEntryAddedCallback = f
    }


    fun updateAccountDEtail(customer: TradeAnalysis?) {
        if (customer != null) {
            val id = customer?.id.toString()//.replace("/", "_")

            tableTradeAnalysisEntry?.
            document(id)?.
            set(customer)?.addOnSuccessListener {
                Log.i(TAG, "SaveAccountInfo addOnSuccessListener documentReference ")
                firebaseEntryAddedCallback?.onCustomerAddedSuccessfully(id)
            }

            var doc = id// this is the id
            if (doc != null) {
                customer?.id = doc
            }


        }
    }

    fun addTimerLog(timerLog: TimerLog?) {
        var doc = tableTimerLogEntry?.document()?.id// this is the id
        if (doc != null) {
            timerLog?.id = doc
        }

        if (timerLog != null) {
            tableTimerLogEntry?.document(doc!!)?.set(timerLog)
                ?.addOnSuccessListener {
                    Log.i(TAG, "tableTimerLogEntry addOnSuccessListener tableTimerLogEntry " + doc)
TeaAgentsharedPreferenceUtil.addToPreferenceCurrentStartTime(System.currentTimeMillis())                }
        }
    }

    fun addAccountDEtail(customer: TradeAnalysis?) {
        var doc = tableTradeAnalysisEntry?.document()?.id// this is the id
        if (doc != null) {
            customer?.id = doc
        }

        if (customer != null) {
            tableTradeAnalysisEntry?.document(doc!!)?.set(customer)
                ?.addOnSuccessListener {
                    Log.i(TAG, "SaveAccountInfo addOnSuccessListener documentReference " + doc)
                    firebaseEntryAddedCallback?.onCustomerAddedSuccessfully(doc)
                }
        }
    }

    fun addPhoneUser(phoneUser: PhoneUser?) {
        val phoneId: String? = TeaAgentsharedPreferenceUtil.getAppId()

        if (phoneId.equals("")) {
            if (phoneUser != null) {
                phoneUser.name?.let { addToPreferenceTabId(it) }//save to local preference such that u never call again and again/
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

    fun addCollectionEntry(collectionEntry: BalanceTx?) {
        var doc = tableCollectionEntry?.document()?.id// this is the id
        if (doc != null) {
            collectionEntry?.id = doc
        }

        if (collectionEntry != null) {
            tableCollectionEntry?.document(doc!!)?.set(collectionEntry)
                ?.addOnSuccessListener {
                    Log.i(TAG, " BalanceTx addOnSuccessListener documentReference " + doc)
                }
        }
    }
    /*{
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
    }*/

    ///get calls

    fun getCurrentPhoneUser(): PhoneUser {
        return PhoneUser(AppHelper.getInstance().uniqueUserID)

    }

    fun getAllAccountDEtail(): Task<QuerySnapshot>? {
        val query = tableTradeAnalysisEntry?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)//getCurrentPhoneUser(TeaAgentsharedPreferenceUtil.getAppId().toString()).name)
        return query?.get()
    }


    fun getAllTradeDetails(): Task<QuerySnapshot>? {
        val query = tableTradeAnalysisEntry?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)//getCurrentPhoneUser(TeaAgentsharedPreferenceUtil.getAppId().toString()).name)
        return query?.get()
    }
    fun getByNameAndDate(
        customerName: String,
        convertedTimestampDate: String
    ): Task<QuerySnapshot>? {
        val query = tableCollectionEntry
            ?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)//getCurrentPhoneUser(TeaAgentsharedPreferenceUtil.getAppId().toString()).name)
            ?.whereEqualTo("bankName", customerName)
//            ?.whereEqualTo("convertedTimestampDate", convertedTimestampDate)
        return query?.get()
    }


    fun getNetAssetsByName(): Task<QuerySnapshot>? {
        val query = tableCollectionEntry
            ?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)//getCurrentPhoneUser(TeaAgentsharedPreferenceUtil.getAppId().toString()).name)
//            ?.orderBy("timestamp", Query.Direction.ASCENDING)
        return query?.get()
    }

    fun getByAccountType(customerName: String): Task<QuerySnapshot>? {
        val query = tableCollectionEntry
            ?.whereEqualTo("phoneUserName", getCurrentPhoneUser().name)//getCurrentPhoneUser(TeaAgentsharedPreferenceUtil.getAppId().toString()).name)
            ?.whereEqualTo("accountType", customerName)
        return query?.get()
    }

    fun getTxById(id: String): Task<DocumentSnapshot>? {
        return tableCollectionEntry?.document(id)?.get()
    }

    fun getTradeDetailsById(id: String): Task<DocumentSnapshot>? {
        return tableTradeAnalysisEntry?.document(id)?.get()
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

}