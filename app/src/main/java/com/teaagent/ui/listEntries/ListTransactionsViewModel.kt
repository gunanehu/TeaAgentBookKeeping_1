package com.teaagent.ui.listEntries

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.data.FirebaseUtil
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.repo.CustomerRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay


// 1
class ListTransactionsViewModel(private val trackingRepository: CustomerRepository) : ViewModel() {
    val TAG: String = "ListEntryViewModel"


    suspend fun getByNameAndDateFromFirebaseDb(
        customerName: String,
        startDate: Long
    ): ArrayList<String> {

        var customers: ArrayList<String> = ArrayList()

        var entryTimestampDate = startDate?.div((1000 * 60 * 60 * 24))
        var task: Task<QuerySnapshot>? =
            FirebaseUtil.getByNameAndDate(customerName, entryTimestampDate)

        val job = GlobalScope.async {
            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(
                            FirebaseUtil.TAG,
                            document.toString() + "===document===================="
                        )

                        var c: CollectionEntry = document.toObject(CollectionEntry::class.java)

                        customers.add(c.toString())
//                    Log.d(FirebaseUtil.TAG, document.id + " => " + document.data)
                        Log.d(FirebaseUtil.TAG, "toObject" + " => " + c.toString())
                        Log.d(FirebaseUtil.TAG, "netTotal" + " => " + c.netTotal)

                        Log.d(FirebaseUtil.TAG, "=======================")

                    }

                } else {
                    Log.e(FirebaseUtil.TAG, "Error getting documents: ", task.exception)
                }
            })
        }

        job.await()
        delay(3000)
        Log.d(FirebaseUtil.TAG, "***************** ********************* customers $customers")
        return customers

    }

}
