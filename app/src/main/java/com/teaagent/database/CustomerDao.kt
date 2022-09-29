

package com.teaagent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teaagent.domain.CustomerEntity


@Dao
interface CustomerDao {
  // 1
  @Query("SELECT * FROM customerentity")
  fun getAllTrackingEntities(): LiveData<List<CustomerEntity>>

//  // 2
//  @Query("SELECT SUM(distanceTravelled) FROM customerentity")
//  fun getTotalDistanceTravelled(): LiveData<Float?>

  // 3
  @Query("SELECT * FROM customerentity ORDER BY timestamp DESC LIMIT 1")
  fun getLastTrackingEntity(): LiveData<CustomerEntity?>

  // 4
  @Query("SELECT * FROM customerentity WHERE customerentity.customerName=:customerName AND customerentity.timestamp / (1000 * 60 * 60 * 24)=:startDate /(1000 * 60 * 60 * 24) " )
  suspend fun newAllExpensesFromTo(customerName: String,startDate: Long?/*, endDate: Long?*/): List<CustomerEntity?>

  // 5
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(trackingEntity: CustomerEntity): Long

  // 6
  @Query("DELETE FROM customerentity")
  suspend fun deleteAll()

  // 7
  @Query("SELECT * FROM customerentity where customerentity.customerName =:customerName")
  suspend fun getAllEntitiesByCustomerName( customerName : String ): List<CustomerEntity>

  @Query("SELECT customerentity.customerName FROM customerentity")
  suspend fun getAllCustomerName(  ): List<String>


  @Query("SELECT * FROM customerentity WHERE customerentity.customerName=:customerName AND" +
          " customerentity.timestamp / (1000 * 60 * 60 * 24)=:startDate /(1000 * 60 * 60 * 24) " )
  suspend fun getByNameAndDate(customerName: String,startDate: Long?): List<CustomerEntity?>


  @Update
  suspend fun update(trackingEntity: CustomerEntity)

}