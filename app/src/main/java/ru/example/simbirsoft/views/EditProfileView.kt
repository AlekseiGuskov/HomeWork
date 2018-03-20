package ru.example.simbirsoft.views

import android.graphics.Bitmap
import android.net.Uri
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
* Created by ag on 02.03.18.
*/
interface EditProfileView : MvpView {
    fun returnToPreviousFragment()
    fun avatarValue(imageUri: Uri)
    fun nameIsValid(isValid: Boolean)
    fun nameValue(value: String)
    fun phoneIsValid(isValid: Boolean)
    fun phoneValue(value: String)
    fun emailIsValid(isValid: Boolean)
    fun emailValue(value: String)
    @StateStrategyType(SkipStrategy::class)
    fun showPopupMenu()
    fun sendButtonState(state: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(text: String)
    fun progressBarVisibility(isVisible: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun chooseImage()
    @StateStrategyType(SkipStrategy::class)
    fun makePhoto()
    @StateStrategyType(SkipStrategy::class)
    fun dataSaved(name: String)
    @StateStrategyType(SkipStrategy::class)
    fun needAuthorize()
}