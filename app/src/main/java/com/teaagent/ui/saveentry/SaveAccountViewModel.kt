package com.teaagent.ui.saveentry

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.data.FirebaseUtil
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.uimappingentities.SaveAccountInfo
import com.teaagent.repo.FirebaseEntryAddedCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import util.StringEncryption

// 1
class SaveAccountViewModel() : ViewModel(),
    FirebaseEntryAddedCallback {
    val TAG: String = "MapsActivityViewModel"
    val accountsLiveData = MutableLiveData<List<SaveAccountInfo>>()


    // 2

    //  val totalDistanceTravelled: LiveData<Float?> = trackingRepository.totalDistanceTravelled
    val currentNumberOfStepCount = MutableLiveData(0)
    var initialStepCount = 0

    fun addAccountDEtail(customer: SaveAccountInfo?) {
        FirebaseUtil.setFirebaseEntryAddedCallback(this)

        val accountInfo: SaveAccountInfo = convertCustomerToEncrypted(customer)
        FirebaseUtil.addAccountDEtail(accountInfo)
    }

    private fun convertCustomerToEncrypted(accountInfo: SaveAccountInfo?): SaveAccountInfo {
        val type =
            accountInfo?.type.toString()
//        val type =
//            StringEncryption.encryptMsg(accountInfo?.type.toString()).toString()
        val bankName =
            StringEncryption.encryptMsg(accountInfo?.bankName.toString()).toString()
//        val phoneUserName =
//            StringEncryption.encryptMsg(accountInfo?.phoneUserName.toString()).toString()
        val phoneUserName =
            accountInfo?.phoneUserName.toString()
        val institutionCode =
            StringEncryption.encryptMsg(accountInfo?.institutionCode.toString()).toString()
        val address =
            StringEncryption.encryptMsg(accountInfo?.address.toString()).toString()

        val accountNo =
            StringEncryption.encryptMsg(accountInfo?.acNo.toString()).toString()
        val netBankingUserName =
            StringEncryption.encryptMsg(accountInfo?.netBankingUserName.toString()).toString()
        val password =
            StringEncryption.encryptMsg(accountInfo?.password.toString()).toString()
        val atmNo =
            StringEncryption.encryptMsg(accountInfo?.atmNo.toString()).toString()
        val atmPin =
            StringEncryption.encryptMsg(accountInfo?.atmPin.toString()).toString()

        return SaveAccountInfo(
            "",
            type,
            bankName,

            phoneUserName,
            institutionCode,
            address,

            accountNo,
            netBankingUserName,
            password,
            atmNo,
            atmPin
        )


    }

    fun addTeaTransactionRecord(collectionEntry: BalanceTx?) {

//        val at = StringEncryption.encryptMsg(collectionEntry?.accountType.toString()).toString()
        val at = collectionEntry?.accountType.toString()
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

        val col = collectionEntry?.let {
            BalanceTx(
                "",
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
        var customers: ArrayList<SaveAccountInfo> = ArrayList()
        var task: Task<QuerySnapshot>? = FirebaseUtil.getAllAccountDEtail()

        val job = GlobalScope.async {
            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        var c: SaveAccountInfo = document.toObject(SaveAccountInfo::class.java)
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
            accountsLiveData.postValue(customers)
        }

        job.await()
    }

    override fun onCustomerAddedSuccessfully(id: String) {
        Log.d(TAG, "onCustomerAddedSuccessfully id " + id)
    }

}

