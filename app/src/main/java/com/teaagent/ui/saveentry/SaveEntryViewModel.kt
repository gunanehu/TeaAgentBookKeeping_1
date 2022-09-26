package com.teaagent.ui.saveentry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teaagent.repo.CustomerRepository
import com.teaagent.domain.CustomerEntity
import kotlinx.coroutines.launch

// 1
class SaveEntryViewModel(private val trackingRepository: CustomerRepository) : ViewModel() {
    val TAG: String = "MapsActivityViewModel"

    // 2
    val allTrackingEntities: LiveData<List<CustomerEntity>> = trackingRepository.allTrackingEntities

    //  val totalDistanceTravelled: LiveData<Float?> = trackingRepository.totalDistanceTravelled
    val currentNumberOfStepCount = MutableLiveData(0)
    var initialStepCount = 0


    // 3
    suspend fun insert(trackingEntity: CustomerEntity) :Long {
       val index:Long= trackingRepository.insert(trackingEntity)
        return index
    }

    fun deleteAllTrackingEntity() = viewModelScope.launch {
        currentNumberOfStepCount.value = 0
        initialStepCount = 0
        trackingRepository.deleteAll()
    }

    suspend fun getAllCustomerName(): List<String> {
        var customers = trackingRepository.getAllCustomerName()
        return customers
    }

}
