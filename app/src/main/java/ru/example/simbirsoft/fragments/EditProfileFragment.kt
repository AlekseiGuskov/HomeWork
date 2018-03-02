package ru.example.simbirsoft.fragments

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import butterknife.BindView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.login_fragment.view.*
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.EditProfilePresenter
import ru.example.simbirsoft.views.EditProfileView

/**
* Created by ag on 02.03.18.
*/
class EditProfileFragment : MvpBaseFragment(), EditProfileView {

    companion object {
        fun getInstance(): EditProfileFragment = EditProfileFragment()
    }

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.name_text_input_layout) lateinit var mNameTextInputLayout: TextInputLayout
    @BindView(R.id.phone_text_input_layout) lateinit var mPhoneTextInputLayout: TextInputLayout
    @BindView(R.id.email_text_input_layout) lateinit var mEmailTextInputLayout: TextInputLayout
    @BindView(R.id.old_password_text_input_layout) lateinit var mOldPasswordTextInputLayout: TextInputLayout
    @BindView(R.id.new_password_text_input_layout) lateinit var mNewPasswordTextInputLayout: TextInputLayout
    @BindView(R.id.repeat_password_text_input_layout) lateinit var mRepeatPasswordTextInputLayout: TextInputLayout
    @BindView(R.id.avatar) lateinit var mAvatarImageView: CircleImageView
    @BindView(R.id.send_button) lateinit var mSendButton: Button


    @InjectPresenter(type = PresenterType.WEAK)
    lateinit var presenter: EditProfilePresenter

    override fun layoutResourceId(): Int = R.layout.edit_profile_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun returnToPreviousFragment() {
        fragmentManager.popBackStack()
    }

    override fun avatarValue(uri: Uri) {
        mAvatarImageView.imageView.setImageURI(uri)
    }

    override fun nameIsValid(isValid: Boolean) {
        if (isValid) {
            mNameTextInputLayout.isErrorEnabled = false
        } else {
            mNameTextInputLayout.error = "Invalid name"
        }
    }

    override fun nameValue(value: String) {
        mNameTextInputLayout.editText?.setText(value)
    }

    override fun phoneIsValid(isValid: Boolean) {
        if (isValid) {
            mPhoneTextInputLayout.isErrorEnabled = false
        } else {
            mPhoneTextInputLayout.error = "Invalid phone"
        }
    }

    override fun phoneValue(value: String) {
        mPhoneTextInputLayout.editText?.setText(value)
    }

    override fun emailIsValid(isValid: Boolean) {
        if (isValid) {
            mEmailTextInputLayout.isErrorEnabled = false
        } else {
            mEmailTextInputLayout.error = getString(R.string.invalid_email)
        }
    }

    override fun emailValue(value: String) {
        mEmailTextInputLayout.editText?.setText(value)
    }

    override fun oldPasswordIsValid(isValid: Boolean) {
        if (isValid) {
            mOldPasswordTextInputLayout.isErrorEnabled = false
        } else {
            mOldPasswordTextInputLayout.error = "Invalid old password"
        }
    }

    override fun oldPasswordValue(value: String) {
        mOldPasswordTextInputLayout.editText?.setText(value)
    }

    override fun newPasswordIsValid(isValid: Boolean) {
        if (isValid) {
            mNameTextInputLayout.isErrorEnabled = false
        } else {
            mNewPasswordTextInputLayout.error = "Invalid new password"
        }
    }

    override fun newPasswordValue(value: String) {
        mNewPasswordTextInputLayout.editText?.setText(value)
    }

    override fun repeatNewPasswordIsValid(isValid: Boolean) {
        if (isValid) {
            mRepeatPasswordTextInputLayout.isErrorEnabled = false
        } else {
            mRepeatPasswordTextInputLayout.error = "Invalid new password"
        }
    }

    override fun repeatNewPasswordValue(value: String) {
        mRepeatPasswordTextInputLayout.editText?.setText(value)
    }

    override fun sendButtonState(state: Boolean) {
        mSendButton.isEnabled = state
        if (state) {
            mSendButton.setBackgroundColor(getColor(R.color.colorGreenLight))
        } else {
            mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
        }
    }

    override fun showMessage(text: String) {
        showToast(text)
    }

    override fun clearFields() {
        mNameTextInputLayout.editText?.setText("")
        mNameTextInputLayout.isErrorEnabled = false
        mPhoneTextInputLayout.editText?.setText("")
        mPhoneTextInputLayout.isErrorEnabled = false
        mOldPasswordTextInputLayout.editText?.setText("")
        mOldPasswordTextInputLayout.isErrorEnabled = false
        mNewPasswordTextInputLayout.editText?.setText("")
        mNewPasswordTextInputLayout.isErrorEnabled = false
        mRepeatPasswordTextInputLayout.editText?.setText("")
        mRepeatPasswordTextInputLayout.isErrorEnabled = false
        mSendButton.isEnabled = false
        mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
    }

    private fun initToolbar() {
        mToolbar.title = "Редактировать"
        mToolbar.setNavigationOnClickListener {
            presenter.returnToPreviousFragmentButtonWasClicked()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}