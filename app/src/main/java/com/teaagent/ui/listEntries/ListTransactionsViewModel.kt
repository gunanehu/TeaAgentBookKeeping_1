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
import util.StringEncryption


// 1
class ListTransactionsViewModel() : ViewModel() {
    val TAG: String = "ListTransactionsViewModel"
    val customerNames = MutableLiveData<List<String>>()
    val blanceTxMutableLiveData = MutableLiveData<List<BalanceTx>>()
    val reportEntities = MutableLiveData<List<BalanceTx>>()


    fun getTxByTypeFromFirebaseDb(type: String) {
        var balanceTxs: ArrayList<BalanceTx> = ArrayList()

        GlobalScope.async {

            /*val encryptedType =
                StringEncryption.encryptMsg(type).toString()
            Log.i(TAG, "encryptedType " + encryptedType)
            var task: Task<QuerySnapshot>? =
                FirebaseUtil.getByAccountType(encryptedType)*/


            var task: Task<QuerySnapshot>? =
                FirebaseUtil.getByAccountType(type)
            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        var c: BalanceTx = document.toObject(BalanceTx::class.java)
//                        customers.add(c.toString())
                        balanceTxs.add(c)

                        Log.d(TAG, document.id + " => " + document.data)
                        Log.d(TAG, "toObject" + " => " + c.toString())
//                        Log.d(FirebaseUtil.TAG, "netTotal" + " => " + c.netTotal)
                    }

                } else {
                    Log.e(TAG, "Error getting documents: ", task.exception)
                }
            })
            task?.addOnSuccessListener { it ->
                Log.d(
                    TAG,
                    "*****************addOnSuccessListener ********************* balanceTxs $balanceTxs"
                )
                blanceTxMutableLiveData.postValue(balanceTxs)
            }
        }
    }


    suspend fun getNetAssetsByName() {
        var balanceTxs: ArrayList<BalanceTx> = ArrayList()

        GlobalScope.async {
            var task: Task<QuerySnapshot>? =
                FirebaseUtil.getNetAssetsByName()

            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        var c: BalanceTx = document.toObject(BalanceTx::class.java)
//                        customers.add(c.toString())
                        balanceTxs.add(c)

                        Log.d(TAG, document.id + " => " + document.data)
                        Log.d(TAG, "toObject" + " => " + c.toString())
//                        Log.d(FirebaseUtil.TAG, "netTotal" + " => " + c.netTotal)
                    }

                } else {
                    Log.e(TAG, "Error getting documents: ", task.exception)
                }
            })
            task?.addOnSuccessListener { it ->
                Log.d(
                    TAG,
                    "*****************addOnSuccessListener ********************* balanceTxs $balanceTxs"
                )
                blanceTxMutableLiveData.postValue(balanceTxs)
            }
        }
    }
}
