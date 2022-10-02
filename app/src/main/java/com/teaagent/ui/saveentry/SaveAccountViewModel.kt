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
import kotlinx.coroutines.*

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
        FirebaseUtil.addAccountDEtail(customer)
    }

    fun addTeaTransactionRecord(collectionEntry: BalanceTx?) {
        val col = collectionEntry?.let {

            /*
 data class BalanceTx(
     var accountType: String,
     var accountNo: String,
     var balanceAmount: Long,
     var timestamp: Long,

     var phoneUserName: String?,
     var customerName: String
 )*/

            BalanceTx(
                it.accountType,
                it.accountNo,
                it.balanceAmount,
                it.timestamp,

                it?.phoneUserName,
                it?.customerName
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
                        Log.d(FirebaseUtil.TAG, "toObject" + " => " + c.name)
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
//        delay(3000)
//        return customers
    }

    override fun onCustomerAddedSuccessfully(id: String) {
        Log.d(TAG, "onCustomerAddedSuccessfully id " + id)

    }

    /* suspend fun getUIAllCustomerFirebaseDb(): ArrayList<Customer> {
         var customers: ArrayList<Customer> = ArrayList()

         val value = GlobalScope.async { // creates worker thread
             withContext(Dispatchers.Default) {
                 customers = getAllCustomerFirebaseDb()
             }
         }
         value.await() //waits for workerthread to finish
         //runs on ui thread as calling function is on Dispatchers.main
         customersLiveData.postValue(customers)
         Log.d(FirebaseUtil.TAG, "***************** ********************* customers $customers")
         return customers
     }*/
}

