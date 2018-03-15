package ru.example.simbirsoft.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import butterknife.BindView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.jakewharton.rxbinding2.widget.RxTextView
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.LoginPresenter
import ru.example.simbirsoft.views.LoginView

/**
* Created by ag
* on 16.02.18.
*/
class LoginFragment : MvpBaseFragment(), LoginView {

    companion object {
        fun getInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: LoginPresenter

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.email_text_input_layout) lateinit var mEmailTextInputLayout: TextInputLayout
    @BindView(R.id.password_text_input_layout) lateinit var mPasswordInputLayout: TextInputLayout
    @BindView(R.id.send_button) lateinit var mSendButton: Button

    override fun layoutResourceId(): Int = R.layout.login_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init()
        initToolbar()

        mSendButton.setOnClickListener {
            presenter.sendButtonClicked()
        }

        RxTextView.textChanges(mEmailTextInputLayout.editText!!)
                .compose(bindToLifecycle()).skip(1).subscribe {
            presenter.emailWasChanged(it.toString())
        }

        RxTextView.textChanges(mPasswordInputLayout.editText!!)
                .compose(bindToLifecycle()).skip(1).subscribe {
            presenter.passwordWasChanged(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun returnToPreviousFragment() {
        fragmentManager.popBackStack()
    }

    override fun emailIsValid(isValid: Boolean) {
        if (isValid) {
            mEmailTextInputLayout.isErrorEnabled = false
        } else {
            mEmailTextInputLayout.error = getString(R.string.invalid_email)
        }
    }

    override fun passwordIsValid(isValid: Boolean) {
        if (isValid) {
            mPasswordInputLayout.isErrorEnabled = false
        } else {
            mPasswordInputLayout.error = getString(R.string.invalid_password)
        }
    }

    override fun emailValue(value: String) {
        mEmailTextInputLayout.editText?.setText(value)
    }

    override fun passwordValue(value: String) {
        mPasswordInputLayout.editText?.setText(value)
    }

    override fun sendButtonState(state: Boolean) {
        mSendButton.isEnabled = state
        if (state) {
            mSendButton.text = getString(R.string.enter)
            mSendButton.setBackgroundColor(getColor(R.color.colorGreenLight))
        } else {
            mSendButton.text = getString(R.string.not_all_fields_are_filled)
            mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
        }
    }

    override fun showMessage(text: String) {
        showToast(text)
    }

    override fun clearFields() {
        mEmailTextInputLayout.editText?.setText("")
        mEmailTextInputLayout.isErrorEnabled = false
        mPasswordInputLayout.editText?.setText("")
        mPasswordInputLayout.isErrorEnabled = false
        mSendButton.isEnabled = false
        mSendButton.text = getString(R.string.not_all_fields_are_filled)
        mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
    }

    private fun initToolbar() {
        mToolbar.title = getString(R.string.authorisation)
        mToolbar.setNavigationOnClickListener {
            presenter.returnToPreviousFragmentButtonWasClicked()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}