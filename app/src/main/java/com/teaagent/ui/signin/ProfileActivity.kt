package com.teaagent.ui.signin

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.os.Bundle
import com.teaagent.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.bumptech.glide.Glide
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.auth.api.Auth
import com.teaagent.ui.saveentry.SaveCustomerActivity
import com.google.android.gms.common.ConnectionResult
import com.teaagent.data.FirebaseUtil
import com.teaagent.databinding.ActivityProfileBinding
import com.teaagent.domain.firemasedbEntities.PhoneUser
import java.lang.NullPointerException

class ProfileActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    var logoutBtn: Button? = null
    var userName: TextView? = null
    var userEmail: TextView? = null
    var userId: TextView? = null
    var profileImage: ImageView? = null
    private var googleApiClient: GoogleApiClient? = null
    private var gso: GoogleSignInOptions? = null
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        logoutBtn = findViewById(R.id.logoutBtn)
        userName = findViewById(R.id.name)
        userEmail = findViewById(R.id.email)
        userId = findViewById(R.id.userId)
        profileImage = findViewById(R.id.profileImage)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
            .build()
        binding.logoutBtn.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            Auth.GoogleSignInApi.signOut(googleApiClient!!).setResultCallback { status ->
                if (status.isSuccess) {
                    // gotoMainActivity();
                } else {
                    Toast.makeText(applicationContext, "Session not close", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val opr = Auth.GoogleSignInApi.silentSignIn(
            googleApiClient!!
        )
        if (opr.isDone) {
            val result = opr.get()
            handleSignInResult(result)
        } else {
            opr.setResultCallback { googleSignInResult -> handleSignInResult(googleSignInResult) }
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount
            userName!!.text = account!!.displayName
            userEmail!!.text = account.email
            userId!!.text = account.id
            Log.i(TAG, "Login Unsuccessful. account.getId() " + account.id)
            try {
                Glide.with(this).load(account.photoUrl).into(profileImage!!)
                val phoneUser: PhoneUser = FirebaseUtil.getCurrentPhoneUser()
                //TODO use only one in lifetime for that user
                FirebaseUtil.addPhoneUser(account.id,phoneUser)

                gotoMainActivity()
            } catch (e: NullPointerException) {
                Toast.makeText(applicationContext, "image not found", Toast.LENGTH_LONG).show()
            }
        } else {
            gotoMainActivity()
        }
    }

    private fun gotoMainActivity() {

        val intent = Intent(this, SaveCustomerActivity::class.java)
        startActivity(intent)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        private const val TAG = "ProfileActivity"
    }
}