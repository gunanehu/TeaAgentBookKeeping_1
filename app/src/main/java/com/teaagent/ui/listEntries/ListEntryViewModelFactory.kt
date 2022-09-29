
package com.teaagent.ui.listEntries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teaagent.repo.CustomerRepository

class ListEntryViewModelFactory(private val trackingRepository: CustomerRepository):
    ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {

    if (modelClass.isAssignableFrom(ListEntryViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return ListEntryViewModel(trackingRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}