package ru.example.simbirsoft

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQ = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provides = listOf<AuthUI.IdpConfig>()

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provides)
                .build(), SIGN_IN_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            SIGN_IN_REQ -> {
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }
            }
        }
    }
}
