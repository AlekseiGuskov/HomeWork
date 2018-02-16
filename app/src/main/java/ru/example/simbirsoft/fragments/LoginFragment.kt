package ru.example.simbirsoft.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import butterknife.BindView
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.LoginPresenter

/**
* Created by ag on 16.02.18.
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


    }

    override fun onDestroy() {
        super.onDestroy()
    }
}