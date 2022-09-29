package com.teaagent.ui.saveentry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.data.FirebaseUtil
import com.teaagent.domain.CustomerEntity
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.domain.firemasedbEntities.Customer
import com.teaagent.repo.CustomerRepository
import com.teaagent.repo.FirebaseEntryAddedCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

// 1
class SaveEntryViewModel(private val trackingRepository: CustomerRepository) : ViewModel(),
    FirebaseEntryAddedCallback {
    val TAG: String = "MapsActivityViewModel"

    // 2
    val allTrackingEntities: LiveData<List<CustomerEntity>> = trackingRepository.allTrackingEntities

    //  val totalDistanceTravelled: LiveData<Float?> = trackingRepository.totalDistanceTravelled
    val currentNumberOfStepCount = MutableLiveData(0)
    var initialStepCount = 0

    fun addCustomer(customer: Customer?) {
        FirebaseUtil.setFirebaseEntryAddedCallback(this)
        FirebaseUtil.addCustomer(customer)
    }

    fun addTeaTransactionRecord(collectionEntry: CollectionEntry?) {
        val col = collectionEntry?.let {
            CollectionEntry(
     /*           it.collectionEntryId,*/
                it.quantity,
                it.amount,
                it.labourAmount,
                it.netTotal,
                it.timestamp,
                it?.phoneUserName,
                it?.customerName
            )
        }
        FirebaseUtil.addCollectionEntry(col)

    }

    suspend fun getAllCustomerFirebaseDb(): ArrayList<Customer> {
        var customers: ArrayList<Customer> = ArrayList()
        var task: Task<QuerySnapshot>? = FirebaseUtil.getAllCustomers()

        val job = GlobalScope.async {
            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        var c: Customer = document.toObject(Customer::class.java)
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

        job.await()
        delay(3000)
        Log.d(FirebaseUtil.TAG, "***************** ********************* customers $customers")
        return customers
    }

    override fun onCustomerAddedSuccessfully(id: String) {
        Log.d(TAG, "onCustomerAddedSuccessfully id " + id)

    }
}
