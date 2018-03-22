package ru.example.simbirsoft.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.google.firebase.auth.FirebaseUser

/**
 * Created by harri
 * on 27.02.2018.
 */
interface LoginView : MvpView {
    fun returnToPreviousFragment()
    fun emailIsValid(isValid: Boolean)
    fun emailValue(value: String)
    fun passwordIsValid(isValid: Boolean)
    fun passwordValue(value: String)
    fun sendButtonState(state: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(text: String)
    fun loginSuccess(user: FirebaseUser)
    fun progressBarVisibility(isVisible: Boolean)
}