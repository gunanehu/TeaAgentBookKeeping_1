package com.teaagent.ui.saveentry

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.data.FirebaseUtil
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import com.teaagent.repo.FirebaseEntryAddedCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

// 1
class SaveAccountViewModel() : ViewModel(),
    FirebaseEntryAddedCallback {
    val TAG: String = "MapsActivityViewModel"
    val tradeDetailsLiveData = MutableLiveData<List<TradeAnalysis>>()


    // 2

    //  val totalDistanceTravelled: LiveData<Float?> = trackingRepository.totalDistanceTravelled
    val currentNumberOfStepCount = MutableLiveData(0)
    var initialStepCount = 0

    fun addAccountDEtail(customer: TradeAnalysis?) {
        FirebaseUtil.setFirebaseEntryAddedCallback(this)
//        val accountInfo: TradeAnalysis = convertCustomerToEncrypted(customer)
        FirebaseUtil.addAccountDEtail(customer)
    }

    fun updateAccountDEtail(customer: TradeAnalysis?) {
        FirebaseUtil.setFirebaseEntryAddedCallback(this)
//        val accountInfo: TradeAnalysis = convertCustomerToEncrypted(customer)
        FirebaseUtil.updateAccountDEtail(customer)
    }

/*
    private fun convertCustomerToEncrypted(accountInfo: TradeAnalysis?): TradeAnalysis {
        val type =
            accountInfo?.tradeIncomeType.toString()

        val id =
            accountInfo?.id.toString()
        val bankName =
            accountInfo?.stockName.toString()
//        val phoneUserName =
//            StringEncryption.encryptMsg(accountInfo?.phoneUserName.toString()).toString()
        val phoneUserName =
            accountInfo?.phoneUserName.toString()
        val institutionCode =
            accountInfo?.EntryPrice.toString()
        val address =
            accountInfo?.SLPrice.toString()

        val accountNo =
            accountInfo?.ExitPrice.toString()
        val netBankingUserName =
           accountInfo?.HTFLocation.toString()
        val password =
           accountInfo?.HTFTrend.toString()
        val atmNo =
           accountInfo?.timestampTradePlanned.toString()
        val atmPin =
           accountInfo?.note.toString()


        return TradeAnalysis(
            id,
            type,
            bankName,

            phoneUserName,
            institutionCode,
            address,

            accountNo,
            netBankingUserName,
            password,
            atmNo,
            atmPin,
            "",""//TODO with actual value
        )


    }
*/

    fun addTeaTransactionRecord(collectionEntry: BalanceTx?) {

// only for encryption/decryption
//val at = StringEncryption.encryptMsg(collectionEntry?.accountType.toString()).toString()
        /*val at = collectionEntry?.accountType.toString()
        val accountNo =
            StringEncryption.encryptMsg(collectionEntry?.accountNo.toString()).toString()
        val balanceAmount =
            StringEncryption.encryptMsg(collectionEntry?.balanceAmount.toString()).toString()
        val timestamp =
            StringEncryption.encryptMsg(collectionEntry?.timestamp.toString()).toString()
//        val phoneUserName =
//            StringEncryption.encryptMsg(collectionEntry?.phoneUserName.toString()).toString()
        val phoneUserName =
            collectionEntry?.phoneUserName.toString()
        val bankName = StringEncryption.encryptMsg(collectionEntry?.bankName.toString()).toString()
*/

        val at = collectionEntry?.accountType.toString()
        val accountId = collectionEntry?.accountId.toString()

        val accountNo =
            collectionEntry?.accountNo.toString()
        val balanceAmount =
            collectionEntry?.balanceAmount.toString()
        val timestamp =
            collectionEntry?.timestamp.toString()
//        val phoneUserName =
//            StringEncryption.encryptMsg(collectionEntry?.phoneUserName.toString()).toString()
        val phoneUserName =
            collectionEntry?.phoneUserName.toString()
        val bankName = collectionEntry?.bankName.toString()

        val col = collectionEntry?.let {
            BalanceTx(
                "",
                accountId,
                at,
                accountNo,
                balanceAmount,
                timestamp,

                phoneUserName,
                bankName
            )
        }
        FirebaseUtil.addCollectionEntry(col)

    }

    suspend fun getAllAccountDetailsFirebaseDb()/*: ArrayList<Customer>*/ {
        var customers: ArrayList<TradeAnalysis> = ArrayList()
        var task: Task<QuerySnapshot>? = FirebaseUtil.getAllTradeDetails()

        val job = GlobalScope.async {
            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        var c: TradeAnalysis = document.toObject(TradeAnalysis::class.java)
                        customers.add(c)
                        Log.d(FirebaseUtil.TAG, document.id + " => " + document.data)
                        Log.d(FirebaseUtil.TAG, "=======================")
                    }

                } else {
                    Log.e(FirebaseUtil.TAG, "Error getting documents: ", task.exception)
                }
            })
        }
        task?.addOnSuccessListener { it ->
            Log.d(
                FirebaseUtil.TAG,
                "*****************addOnSuccessListener ********************* customers $customers"
            )
            tradeDetailsLiveData.postValue(customers)
        }

        job.await()
    }

    override fun onCustomerAddedSuccessfully(id: String) {
        Log.d(TAG, "onCustomerAddedSuccessfully id " + id)
    }

}

