package ru.example.simbirsoft

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQ = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provides =
                listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provides)
                .build(), SIGN_IN_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val isResultOk = resultCode == Activity.RESULT_OK
        when(requestCode) {
            SIGN_IN_REQ -> {
                if (isResultOk) {
                    FirebaseAuth.getInstance().currentUser?.let {
                        showToast("Hello ${it.displayName}")
                    }
                } else {
                    IdpResponse.fromResultIntent(data)?.let {
                        showToast("Error ${it.errorCode} for login user ${it.email}")
                    }
                }
            }
        }
    }

    private fun showToast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
