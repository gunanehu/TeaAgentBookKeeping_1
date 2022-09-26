
package com.teaagent

import android.app.Application
import com.teaagent.database.CustomerDatabase
import com.teaagent.repo.CustomerRepository

// 1
class TrackingApplication: Application() {
  // 2
  private val trackingDatabase by lazy { CustomerDatabase.getDatabase(this) }
  val trackingRepository by lazy { CustomerRepository(trackingDatabase.getTrackingDao()) }
}
