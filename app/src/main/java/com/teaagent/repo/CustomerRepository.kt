
package com.teaagent.repo

import androidx.annotation.WorkerThread
import com.teaagent.database.CustomerDao
import com.teaagent.domain.CustomerEntity

// 1
class CustomerRepository(private val trackingDao: CustomerDao) {

  // 2
  val allTrackingEntities = trackingDao.getAllTrackingEntities()
  val lastTrackingEntity = trackingDao.getLastTrackingEntity()
//  val totalDistanceTravelled = trackingDao.getTotalDistanceTravelled()

  // 3
  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun newAllExpensesFromTo(customerName: String,startDate: Long?) : List<CustomerEntity?> {
   return trackingDao.newAllExpensesFromTo(customerName,startDate/*,endDate*/)
  }
  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun insert(trackingEntity: CustomerEntity):Long {
    return trackingDao.insert(trackingEntity)
  }

  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun deleteAll() {
    trackingDao.deleteAll()
  }

  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun getAllEntitiesByCustomerName(customerName : String) : List<CustomerEntity>{
    return trackingDao.getAllEntitiesByCustomerName(customerName)
  }

  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun getByNameAndDate(customerName : String, startDate: Long?) : List<CustomerEntity?> {
    return trackingDao.getByNameAndDate(customerName,startDate)
  }

  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun getAllCustomerName() : List<String>{
    return trackingDao.getAllCustomerName()
  }

}
