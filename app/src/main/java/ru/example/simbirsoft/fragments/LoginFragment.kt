package ru.example.simbirsoft.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import com.jakewharton.rxbinding2.widget.RxTextView
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.LoginPresenter

/**
* Created by ag
* on 16.02.18.
*/
class LoginFragment : BaseFragment<LoginPresenter>() {

    companion object {
        fun getInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.email_text_input_layout) lateinit var mEmailTextInputLayout: TextInputLayout
    @BindView(R.id.password_text_input_layout) lateinit var mPasswordInputLayout: TextInputLayout
    @BindView(R.id.send_button) lateinit var mSendButton: Button

    override fun layoutResourceId(): Int = R.layout.login_fragment

    override fun createPresenter(): LoginPresenter = LoginPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        mSendButton.setOnClickListener {
            presenter?.sendButtonClicked()
        }

        presenter?.callback?.emailValue()?.compose(bindToLifecycle())?.take(1)?.subscribe {
            mEmailTextInputLayout.editText?.setText(it)
        }

        presenter?.callback?.passwordValue()?.compose(bindToLifecycle())?.take(1)?.subscribe {
            mPasswordInputLayout.editText?.setText(it)
        }

        presenter?.callback?.sendButtonState()?.compose(bindToLifecycle())?.subscribe {
            mSendButton.isEnabled = it
            if (it) {
                mSendButton.text = getString(R.string.enter)
                mSendButton.setBackgroundColor(getColor(R.color.colorGreenLight))
            } else {
                mSendButton.text = getString(R.string.not_all_fields_are_filled)
                mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
            }
        }

        presenter?.callback?.showMessage()?.compose(bindToLifecycle())?.subscribe {
            showToast(it, length = Toast.LENGTH_LONG)
        }

        presenter?.callback?.clearFields()?.compose(bindToLifecycle())?.subscribe {
            mEmailTextInputLayout.editText?.setText("")
            mPasswordInputLayout.editText?.setText("")
        }

        RxTextView.textChanges(mEmailTextInputLayout.editText!!)
                .compose(bindToLifecycle()).skip(1).subscribe {
            presenter?.loginWasChanged(it.toString())
        }

        RxTextView.textChanges(mPasswordInputLayout.editText!!)
                .compose(bindToLifecycle()).skip(1).subscribe {
            presenter?.passwordWasChanged(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initToolbar() {
        mToolbar.title = getString(R.string.authorisation)
        mToolbar.setNavigationOnClickListener {
            presenter?.returnToPreviousFragmentButtonWasClicked()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}