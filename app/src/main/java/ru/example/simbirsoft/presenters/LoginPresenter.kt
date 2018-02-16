package ru.example.simbirsoft.presenters

import android.text.TextUtils
import android.util.Patterns
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.example.simbirsoft.Empty

/**
* Created by ag on 16.02.18.
*/

interface ILoginCallback {
    fun returnToPreviousFragment(): Observable<Empty>
    fun emailValue(): Observable<String>
    fun passwordValue(): Observable<String>
    fun sendButtonState(): Observable<Boolean>
}

class LoginPresenter : BasePresenter(), ILoginCallback {

    val callback = this as ILoginCallback

    private val mReturnToPreviousFragmentObservable = PublishSubject.create<Empty>()
    private val mEmailValueObservable = BehaviorSubject.createDefault("")
    private val mPasswordValueObservable = BehaviorSubject.createDefault("")
    private val mSendButtonStateObservable = Observable.combineLatest(mEmailValueObservable,
            mPasswordValueObservable, BiFunction<String, String, Boolean> { email, password ->
                return@BiFunction isValidEmail(email) && password.isNotEmpty()
            })

    override fun viewCreated() {
        super.viewCreated()
    }

    override fun viewDestroy() {
        super.viewDestroy()
    }

    fun loginWasChanged(login: String) {
        mEmailValueObservable.onNext(login)
    }

    fun passwordWasChanged(password: String) {
        mPasswordValueObservable.onNext(password)
    }

    fun sendButtonClicked() {
        login()
    }

    fun returnToPreviousFragmentButtonWasClicked() {
        mReturnToPreviousFragmentObservable.onNext(Empty.INSTANCE)
    }

    private fun login() {}

    private fun isValidEmail(email: String?): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    //Callback methods

    override fun returnToPreviousFragment(): Observable<Empty> = mReturnToPreviousFragmentObservable

    override fun emailValue(): Observable<String> = mEmailValueObservable

    override fun passwordValue(): Observable<String> = mPasswordValueObservable

    override fun sendButtonState(): Observable<Boolean> = mSendButtonStateObservable
}