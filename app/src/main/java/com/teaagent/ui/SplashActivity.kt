package com.teaagent.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teaagent.databinding.ActivitySplashBinding
import com.teaagent.ui.saveentry.SaveCustomerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    val TAG: String = "SplashActivity"

    val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activityScope.launch {
            delay(1000)

// ...
// Initialize Firebase Auth
//            mAuth = FirebaseAuth.getInstance();

            // signin()

            var intent = Intent(this@SplashActivity, SaveCustomerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
//    lateinit var mAuth:FirebaseAuth;

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser: FirebaseUser? = mAuth.getCurrentUser()
//        updateUI(currentUser)
    }
    /* private fun signin() {
        val signInRequest = BeginSignInRequest.builder()
             .setGoogleIdTokenRequestOptions(
                 BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                 .setSupported(true)
                 // Your server's client ID, not your Android client ID.
                 .setServerClientId(getString(R.string.default_web_client_id))
                 // Only show accounts previously used to sign in.
                 .setFilterByAuthorizedAccounts(true)
                 .build())
             .build();

 //        val googleCredential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(data)
        var idToken =null//= googleCredential.googleIdToken
 //        if (idToken != null) {
             // Got an ID token from Google. Use it to authenticate
             // with Firebase.
             val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
             mAuth.signInWithCredential(firebaseCredential)
                 .addOnCompleteListener(
                     this
                 ) { task ->
                     if (task.isSuccessful) {
                         // Sign in success, update UI with the signed-in user's information
                         Log.d(TAG, "signInWithCredential:success")
                         val user = mAuth.currentUser
                       //  updateUI(user)
                     } else {
                         // If sign in fails, display a message to the user.
                         Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // updateUI(null)
                     }
                 }
         }*/
}


