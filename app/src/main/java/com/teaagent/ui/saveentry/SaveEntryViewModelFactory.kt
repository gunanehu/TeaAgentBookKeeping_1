
package com.teaagent.ui.saveentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaveEntryViewModelFactory():
    ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {

    if (modelClass.isAssignableFrom(SaveAccountViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return SaveAccountViewModel() as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}