
package com.teaagent.ui.saveentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teaagent.repo.CustomerRepository

class SaveEntryViewModelFactory(private val trackingRepository: CustomerRepository):
    ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {

    if (modelClass.isAssignableFrom(SaveEntryViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return SaveEntryViewModel(trackingRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}