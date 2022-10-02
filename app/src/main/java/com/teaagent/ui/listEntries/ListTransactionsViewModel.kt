package com.teaagent.ui.listEntries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.data.FirebaseUtil
import com.teaagent.domain.firemasedbEntities.BalanceTx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


// 1
class ListTransactionsViewModel() : ViewModel() {
    val TAG: String = "ListEntryViewModel"
    val customerNames = MutableLiveData<List<String>>()
    val blanceTxMutableLiveData = MutableLiveData<List<BalanceTx>>()
    val reportEntities = MutableLiveData<List<BalanceTx>>()


    suspend fun getTxByTypeFromFirebaseDb(type: String) {
        var balanceTxs: ArrayList<BalanceTx> = ArrayList()

        val job = GlobalScope.async {
            var task: Task<QuerySnapshot>? =
                FirebaseUtil.getByAccountType(
                    type
                )

            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        var c: BalanceTx = document.toObject(BalanceTx::class.java)

//                        customers.add(c.toString())
                        balanceTxs.add(c)

                    Log.d(FirebaseUtil.TAG, document.id + " => " + document.data)
                        Log.d(FirebaseUtil.TAG, "toObject" + " => " + c.toString())
//                        Log.d(FirebaseUtil.TAG, "netTotal" + " => " + c.netTotal)
                    }

                } else {
                    Log.e(FirebaseUtil.TAG, "Error getting documents: ", task.exception)
                }
            })
            task?.addOnSuccessListener { it ->
                Log.d(
                    FirebaseUtil.TAG,
                    "*****************addOnSuccessListener ********************* customers $balanceTxs"
                )
//                customerNames.postValue(customers)
                blanceTxMutableLiveData.postValue(balanceTxs)
            }
        }
    }


    suspend fun getByNameFirebaseDb(
        customerName: String
    ) {
        var customers: ArrayList<BalanceTx> = ArrayList()
        GlobalScope.async {
            var task: Task<QuerySnapshot>? =
                FirebaseUtil.getByName(customerName)
            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        var c: BalanceTx = document.toObject(BalanceTx::class.java)
                        customers.add(c)
                        Log.d(FirebaseUtil.TAG, "toObject" + " => " + c.toString())
                    }

                } else {
                    Log.e(FirebaseUtil.TAG, "Error getting documents: ", task.exception)
                }
            })
            task?.addOnSuccessListener { it ->
                Log.d(
                    FirebaseUtil.TAG,
                    "***************** reportEntities addOnSuccessListener ********************* customers $customers"
                )
                reportEntities.postValue(customers)
            }
        }
    }
}
