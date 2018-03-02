package ru.example.simbirsoft.views

import android.net.Uri
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
* Created by ag on 02.03.18.
*/
interface EditProfileView : MvpView {
    fun returnToPreviousFragment()
    fun avatarValue(uri: Uri)
    fun nameIsValid(isValid: Boolean)
    fun nameValue(value: String)
    fun phoneIsValid(isValid: Boolean)
    fun phoneValue(value: String)
    fun emailIsValid(isValid: Boolean)
    fun emailValue(value: String)
    fun oldPasswordIsValid(isValid: Boolean)
    fun oldPasswordValue(value: String)
    fun newPasswordIsValid(isValid: Boolean)
    fun newPasswordValue(value: String)
    fun repeatNewPasswordIsValid(isValid: Boolean)
    fun repeatNewPasswordValue(value: String)
    fun sendButtonState(state: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(text: String)
    fun clearFields()
}