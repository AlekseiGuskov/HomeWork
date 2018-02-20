package ru.example.simbirsoft.presenters

import android.text.TextUtils
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.example.simbirsoft.Empty
import ru.example.simbirsoft.R
import ru.example.simbirsoft.Util

/**
* Created by ag
* on 16.02.18.
*/

interface ILoginCallback {
    fun returnToPreviousFragment(): Observable<Empty>
    fun emailValue(): Observable<String>
    fun passwordValue(): Observable<String>
    fun sendButtonState(): Observable<Boolean>
    fun showMessage(): Observable<String>
    fun clearFields(): Observable<Empty>
}

class LoginPresenter : BasePresenter(), ILoginCallback {

    val callback = this as ILoginCallback

    private val mClearAllFieldsObservable = PublishSubject.create<Empty>()
    private val mShowMessageObservable = PublishSubject.create<String>()
    private val mReturnToPreviousFragmentObservable = PublishSubject.create<Empty>()
    private val mEmailValueObservable = BehaviorSubject.createDefault("")
    private val mPasswordValueObservable = BehaviorSubject.createDefault("")
    private val mSendButtonStateObservable = Observable.combineLatest(mEmailValueObservable,
            mPasswordValueObservable, BiFunction<String, String, Boolean> { email, password ->
        return@BiFunction isValidEmail(email) && password.isNotEmpty()
    })

    override fun init() {
        super.init()
    }

    override fun destroy() {
        super.destroy()
    }

    fun loginWasChanged(login: String) {
        mEmailValueObservable.onNext(login)
    }

    fun passwordWasChanged(password: String) {
        mPasswordValueObservable.onNext(password)
    }

    fun sendButtonClicked() {
        if (Util.isConnected()) {
            login()
        } else {
            mShowMessageObservable.onNext(getString(R.string.not_connected))
        }
    }

    fun returnToPreviousFragmentButtonWasClicked() {
        mReturnToPreviousFragmentObservable.onNext(Empty.INSTANCE)
    }

    private fun login() {
        val mAuth = FirebaseAuth.getInstance()
        mAuth?.signInWithEmailAndPassword(mEmailValueObservable.value,
                mPasswordValueObservable.value)?.addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val user = result.result.user
                val email = user?.email ?: ""
                mShowMessageObservable.onNext("$email login")
                clearDataFields()
                mClearAllFieldsObservable.onNext(Empty.INSTANCE)
            } else {
                val ex = result.exception?.localizedMessage ?: ""
                mShowMessageObservable.onNext("Error: $ex")
                result.exception?.cause
            }
        }
    }

    private fun clearDataFields() {
        mEmailValueObservable.onNext("")
        mPasswordValueObservable.onNext("")
    }

    private fun isValidEmail(email: String?): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    //Callback methods

    override fun returnToPreviousFragment(): Observable<Empty> = mReturnToPreviousFragmentObservable

    override fun emailValue(): Observable<String> = mEmailValueObservable

    override fun passwordValue(): Observable<String> = mPasswordValueObservable

    override fun sendButtonState(): Observable<Boolean> = mSendButtonStateObservable

    override fun showMessage(): Observable<String> = mShowMessageObservable

    override fun clearFields(): Observable<Empty> = mClearAllFieldsObservable
}