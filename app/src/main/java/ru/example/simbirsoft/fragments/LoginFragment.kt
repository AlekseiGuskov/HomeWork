package ru.example.simbirsoft.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import butterknife.BindView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.google.firebase.auth.FirebaseUser
import com.jakewharton.rxbinding2.widget.RxTextView
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.LoginPresenter
import ru.example.simbirsoft.views.LoginView

/**
* Created by ag
* on 16.02.18.
*/
class LoginFragment : MvpBaseFragment(), LoginView {

    interface ILoginSuccessCallback {
        fun loginSuccess(user: FirebaseUser)
    }

    companion object {
        fun getInstance(parentFragment: Fragment?): LoginFragment = LoginFragment().apply {
            parentFragment?.let {
                this.setTargetFragment(it, -1)
            }
        }
    }

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: LoginPresenter

    @BindView(R.id.progress_bar_container) lateinit var mProgressBarContainer: FrameLayout
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

    override fun loginSuccess(user: FirebaseUser) {
        val parent = targetFragment as? ILoginSuccessCallback
        parent?.loginSuccess(user)
        fragmentManager.popBackStack()
    }

    override fun progressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            mProgressBarContainer.visibility = View.VISIBLE
        } else {
            mProgressBarContainer.visibility = View.GONE
        }
    }

    private fun initToolbar() {
        mToolbar.title = getString(R.string.authorisation)
        mToolbar.setNavigationOnClickListener {
            presenter.returnToPreviousFragmentButtonWasClicked()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}