package ru.example.simbirsoft.activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import ru.example.simbirsoft.R
import ru.example.simbirsoft.fragments.EditProfileFragment
import ru.example.simbirsoft.fragments.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (savedInstanceState == null) {
            beginEditProfileFragment()
        }
    }

    private fun beginLoginFragment() {
        supportFragmentManager.beginTransaction()
                .addToBackStack(LoginFragment::class.simpleName.toString())
                .add(android.R.id.content, LoginFragment.getInstance(null)).commit()
    }

    private fun beginEditProfileFragment() {
        supportFragmentManager.beginTransaction()
                .addToBackStack(EditProfileFragment::class.simpleName.toString())
                .add(android.R.id.content, EditProfileFragment.getInstance()).commit()
    }
}
