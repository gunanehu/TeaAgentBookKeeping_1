package com.teaagent.repo;

import org.jetbrains.annotations.NotNull;

public interface FirebaseEntryAddedCallback {

    void onCustomerAddedSuccessfully(@NotNull String id);
}
