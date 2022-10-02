package com.teaagent.repo;

import com.teaagent.domain.firemasedbEntities.uimappingentities.SaveAccountInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FirebaseEntryAddedCallback {

    void onCustomerAddedSuccessfully(@NotNull String id);


}
