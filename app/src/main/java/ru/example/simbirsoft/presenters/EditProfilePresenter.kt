package ru.example.simbirsoft.presenters

import android.net.Uri
import android.util.Patterns
import ru.example.simbirsoft.views.EditProfileView

/**
* Created by ag on 02.03.18.
*/

class EditProfilePresenter : BasePresenter<EditProfileView>() {

    private var mAvatar: Uri? = null
    private var mName = ""
    private var mPhone = ""
    private var mEmail = ""
    private var mOldPassword = ""
    private var mNewPassword = ""
    private var mRepeatNewPassword = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        //TODO load data
    }

    override fun init() {
        super.init()
        if (mAvatar != null) {
            viewState.avatarValue(mAvatar!!)
        }
        viewState.nameValue(mName)
        viewState.phoneValue(mPhone)
        viewState.emailValue(mEmail)
        viewState.oldPasswordValue(mOldPassword)
        viewState.newPasswordValue(mNewPassword)
        viewState.repeatNewPasswordValue(mRepeatNewPassword)
        viewState.nameIsValid(mName.isNotEmpty())
        //TODO
        viewState.phoneIsValid(isValidPhone(mPhone))
        viewState.emailIsValid(isValidEmail(mEmail))
        viewState.oldPasswordIsValid(mOldPassword.isNotEmpty())
        viewState.newPasswordIsValid(mNewPassword.isNotEmpty())
        viewState.repeatNewPasswordIsValid(mNewPassword == mOldPassword)
        changeSendButtonState()
    }

    override fun destroy() {
        super.destroy()
    }

    fun nameWasChanged(value: String) {
        mName = value
        viewState.nameIsValid(value.isNotEmpty())
        changeSendButtonState()
    }

    fun phoneWasChanged(value: String) {
        mPhone = value
        //TODO
        viewState.phoneIsValid(isValidPhone(value))
        changeSendButtonState()
    }

    fun emailWasChanged(value: String) {
        mEmail = value
        viewState.emailIsValid(isValidEmail(value))
        changeSendButtonState()
    }

    fun oldPasswordWasChanged(value: String) {
        mOldPassword = value
        //TODO
        viewState?.oldPasswordIsValid(value.isNotEmpty())
        changeSendButtonState()
    }

    fun newPasswordWasChanged(value: String) {
        mNewPassword = value
        //TODO
        viewState?.newPasswordIsValid(value.isNotEmpty())
        changeSendButtonState()
    }

    fun repeatPasswordWasChanged(value: String) {
        mRepeatNewPassword = value
        //TODO
        viewState?.repeatNewPasswordIsValid(value.isNotEmpty())
        changeSendButtonState()
    }

    fun sendButtonClicked() {
        //TODO save
    }

    fun returnToPreviousFragmentButtonWasClicked() {
        viewState.returnToPreviousFragment()
    }

    private fun isValidEmail(value: String) = Patterns.EMAIL_ADDRESS.matcher(value).matches()

    private fun isValidPhone(value: String) = Patterns.PHONE.matcher(value).matches()

    private fun changeSendButtonState() {
        //TODO
        viewState.sendButtonState(mName.isNotEmpty() && isValidPhone(mPhone) &&
                isValidEmail(mEmail) && mOldPassword.isNotEmpty() && mNewPassword.isNotEmpty() &&
        mOldPassword == mNewPassword)
    }
}