package ru.example.simbirsoft.presenters

import android.net.Uri
import android.util.Log
import android.util.Patterns
import com.arellomobile.mvp.InjectViewState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.example.simbirsoft.models.User
import ru.example.simbirsoft.views.EditProfileView

/**
* Created by ag on 02.03.18.
*/
@InjectViewState
class EditProfilePresenter : BasePresenter<EditProfileView>() {

    private var mIsFirstAttach = true

    private var mAvatar: Uri? = null
    private var mName = ""
    private var mPhone = ""
    private var mEmail = ""
    private var mOldPassword = ""
    private var mNewPassword = ""
    private var mRepeatNewPassword = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        mIsFirstAttach = false
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").child("0")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        viewState.progressBarVisibility(false)
                        val exception = error?.toException()
                        Log.w(this::class.java.simpleName,
                                "Failed to read value.", exception)
                        viewState.showMessage("Failed to read value ${exception.toString()}")
                    }

                    override fun onDataChange(data: DataSnapshot?) {
                        val user = data?.getValue(User::class.java)
                        mName = user?.name ?: ""
                        mEmail = user?.email ?: ""
                        mPhone = user?.phone ?: ""
                        viewState.nameValue(mName)
                        viewState.phoneValue(mPhone)
                        viewState.emailValue(mEmail)
                        viewState.progressBarVisibility(false)
                    }
                })
    }

    override fun init() {
        super.init()
        if (!mIsFirstAttach) {
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
            viewState.phoneIsValid(isValidPhone(mPhone))
            viewState.emailIsValid(isValidEmail(mEmail))
            viewState.oldPasswordIsValid(mOldPassword.isNotEmpty())
            viewState.newPasswordIsValid(mNewPassword.isNotEmpty())
            viewState.repeatNewPasswordIsValid(mNewPassword == mOldPassword)
            changeSendButtonState()
        }
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
        validateNewPasswords()
        changeSendButtonState()
    }

    fun repeatPasswordWasChanged(value: String) {
        mRepeatNewPassword = value
        validateNewPasswords()
        changeSendButtonState()
    }

    fun sendButtonClicked() {
        val database = FirebaseDatabase.getInstance().reference.child("users").child("0")
        database.setValue(User(mEmail, mName, mPhone))
    }

    fun returnToPreviousFragmentButtonWasClicked() {
        viewState.returnToPreviousFragment()
    }

    fun changeAvatarForImageWasClicked() {
        viewState.chooseImage()
    }

    fun changeAvatarForPhotoWasClicked() {
        viewState.makePhoto()
    }

    private fun validateNewPasswords() {
        viewState?.newPasswordIsValid(mNewPassword.isNotEmpty())
        viewState?.repeatNewPasswordIsValid(mRepeatNewPassword.isNotEmpty() &&
                mNewPassword == mRepeatNewPassword)
    }

    private fun isValidEmail(value: String) = Patterns.EMAIL_ADDRESS.matcher(value).matches()

    private fun isValidPhone(value: String) = Patterns.PHONE.matcher(value).matches()

    private fun changeSendButtonState() {
        viewState.sendButtonState(mName.isNotEmpty() && isValidPhone(mPhone) &&
                isValidEmail(mEmail) && mOldPassword.isNotEmpty() && mNewPassword.isNotEmpty() &&
        mOldPassword == mNewPassword)
    }
}