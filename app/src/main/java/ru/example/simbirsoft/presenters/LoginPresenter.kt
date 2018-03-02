package ru.example.simbirsoft.presenters

import android.text.TextUtils
import android.util.Patterns
import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseAuth
import ru.example.simbirsoft.R
import ru.example.simbirsoft.common.Util
import ru.example.simbirsoft.views.LoginView

/**
* Created by ag
* on 16.02.18.
*/

@InjectViewState
class LoginPresenter : BasePresenter<LoginView>() {

    private var mEmail = ""
    private var mPassword = ""

    override fun init() {
        super.init()
        viewState.emailValue(mEmail)
        viewState.emailIsValid(isValidEmail(mEmail))
        viewState.passwordValue(mPassword)
        viewState.passwordIsValid(mPassword.isNotEmpty())
    }

    override fun destroy() {
        super.destroy()
    }

    fun emailWasChanged(email: String) {
        mEmail = email
        viewState.emailIsValid(isValidEmail(email))
        changeSendButtonState()
    }

    fun passwordWasChanged(password: String) {
        mPassword = password
        viewState.passwordIsValid(!password.isEmpty())
        changeSendButtonState()
    }

    fun sendButtonClicked() {
        if (Util.isConnected()) {
            login()
        } else {
            viewState.showMessage(getString(R.string.not_connected))
        }
    }

    fun returnToPreviousFragmentButtonWasClicked() {
        viewState.returnToPreviousFragment()
    }

    private fun changeSendButtonState() {
        viewState.sendButtonState(isValidEmail(mEmail) && mPassword.isNotEmpty())
    }

    private fun login() {
        val mAuth = FirebaseAuth.getInstance()
        mAuth?.signInWithEmailAndPassword(mEmail,
                mPassword)?.addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val user = result.result.user
                val email = user?.email ?: ""
                viewState.showMessage("$email login")
                clearDataFields()
                viewState.clearFields()
            } else {
                val ex = result.exception?.localizedMessage ?: ""
                viewState.showMessage("Error: $ex")
                result.exception?.cause
            }
        }
    }

    private fun clearDataFields() {
        mEmail = ""
        mPassword = ""
        viewState.emailValue("")
        viewState.passwordValue("")
        viewState.emailIsValid(false)
        viewState.passwordIsValid(false)
    }

    private fun isValidEmail(email: String?): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}