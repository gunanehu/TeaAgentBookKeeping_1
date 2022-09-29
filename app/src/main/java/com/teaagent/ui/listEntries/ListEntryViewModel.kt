package com.teaagent.ui.listEntries

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.teaagent.data.FirebaseUtil
import com.teaagent.domain.CustomerEntity
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.repo.CustomerRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay


// 1
class ListEntryViewModel(private val trackingRepository: CustomerRepository) : ViewModel() {
    val TAG: String = "ListEntryViewModel"

    // 2
    val allTrackingEntities: LiveData<List<CustomerEntity>> = trackingRepository.allTrackingEntities

    suspend fun getTotalAmountByCustomerName(customerName: String): Long {
        var list = trackingRepository.getAllEntitiesByCustomerName(customerName)
        var amount: Long = 0
        list!!.forEach { customerEntity ->
            run {
                Log.d(
                    TAG,
                    "customerName " + customerName + " customerEntity.amount : " + customerEntity.totalamount
                );

                amount += customerEntity.totalamount
            }
        }
        return amount
    }
//    suspend fun getCollectionByNameAndDateFromFirebaseDb(
//        customerName: String,
//        startDate: Long?
//    ): ArrayList<String> {
//
//        var collectionEntry: ArrayList<CollectionEntry> = ArrayList()
//        var task: Task<QuerySnapshot>? = FirebaseUtil.getByNameAndDate(customerName, startDate)
//
//        val job = GlobalScope.async {
//            task?.addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
//                if (task.isSuccessful) {
//                    for (document in task.result) {
//                        Log.d(
//                            FirebaseUtil.TAG,
//                            document.toString() + "===document===================="
//                        )
//
//                        var c: CollectionEntry = document.toObject(CollectionEntry::class.java)
//
//                        collectionEntry.add(c.toString())
////                    Log.d(FirebaseUtil.TAG, document.id + " => " + document.data)
//                        Log.d(FirebaseUtil.TAG, "toObject" + " => " + c.toString())
//                        Log.d(FirebaseUtil.TAG, "=======================")
//
//                    }
//
//                } else {
//                    Log.e(FirebaseUtil.TAG, "Error getting documents: ", task.exception)
//                }
//            })
//        }
//
//        job.await()
//        delay(3000)
//        Log.d(FirebaseUtil.TAG, "***************** ********************* customers $customers")
//        return customers
//
//    }
    suspend fun getByNameAndDateFromFirebaseDb(
        customerName: String,
        startDate: Long?
    ): ArrayList<String> {

        var customers: ArrayList<String> = ArrayList()
        var task: Task<QuerySnapshot>? = FirebaseUtil.getByNameAndDate(customerName, startDate)

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

    suspend fun getByNameAndDateFromRoomDb(
        customerName: String,
        startDate: Long?
    ): ArrayList<String> {
        var list = trackingRepository.getByNameAndDate(customerName, startDate)
        var customers: ArrayList<String> = ArrayList()
        list!!.forEach { customerEntity ->
            run {
                customers.add(customerEntity.toString())
                Log.d(
                    TAG,
                    customerEntity.toString()
                );
            }
        }
        return customers
    }

    suspend fun getTListByCustomerName(customerName: String): ArrayList<String> {
        var list = trackingRepository.getAllEntitiesByCustomerName(customerName)
        var customers: ArrayList<String> = ArrayList()
        list!!.forEach { customerEntity ->
            run {
                customers.add(customerEntity.toString())
                Log.d(
                    TAG,
                    customerEntity.toString()
                );
            }
        }
        return customers
    }

    suspend public fun getALLByCustomerName(customerName: String): List<CustomerEntity> {
        var customers = trackingRepository.getAllEntitiesByCustomerName(customerName)

        return customers
    }

    suspend fun newAllExpensesFromTo(customerName: String, startDate: Long?): Double {
        Log.d(TAG, "newAllExpensesFromTo before serach   startDate: " + startDate);

        var list = trackingRepository.newAllExpensesFromTo(customerName, startDate)
        var amount: Double = 0.0
        list!!.forEach { customerEntity ->
            if (customerEntity != null) {
                run {
                    Log.d(
                        TAG,
                        "customerName " + customerName + " customerEntity.amount : " + customerEntity.amount
                    );

                    amount += customerEntity.totalamount
                }
            }
        }
        return amount
    }


    suspend fun getAllCustomerName(): List<String> {
        var customers = trackingRepository.getAllCustomerName()
        return customers
    }


    // export all Transactions to csv file
//    fun exportTransactionsToCsv(csvFileUri: Uri) = viewModelScope.launch {
////        _exportCsvState.value = ExportState.Loading
//        transactionRepo
//            .getAllTransactions()
//            .flowOn(Dispatchers.IO)
//            .map { it.toCsv() }
//            .flatMapMerge { exportService.writeToCSV(csvFileUri, it) }
//            .catch { error ->
////                _exportCsvState.value = ExportState.Error(error)
//            }.collect { uriString ->
////                _exportCsvState.value = ExportState.Success(uriString)
//            }
//    }

/*
    fun exportTransactionsToCsv(customerName: String) = viewModelScope.launch(IO) {
        // 👇 state manager for loading | error | success
//        _exportCsvState.value = ViewState.Loading uccess

        // 👇 get all trasnaction detail from repository
        var list: List<CustomerEntity> =
            trackingRepository.getAllEntitiesByCustomerName(customerName)

        // 👇 call export function from Export serivce
        ExportService.export<CustomerEntityToCSV>(
            type = Exports.CSV(CsvConfig()), // 👈 apply config + type of export
            content = list.toCsv() // 👈 send transformed data of exportable type
        ).catch { error ->
            Log.e(TAG,"exportTransactionsToCsv")
            // 👇 handle error here
//            _exportCsvState.value = ViewState.Error(error)
        }
        //        .collect { _ ->
//            // 👇 do anything on success
////            exportCsvState.value = ViewState.Success(emptyList())
//        }
    }*/
}
