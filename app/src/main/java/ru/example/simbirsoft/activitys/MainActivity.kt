package ru.example.simbirsoft.activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ru.example.simbirsoft.R
import ru.example.simbirsoft.fragments.LoginFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onStart() {
        super.onStart()
        beginLoginFragment()
    }

    private fun beginLoginFragment() {
        supportFragmentManager.beginTransaction().addToBackStack(LoginFragment::class.simpleName.toString())
                .add(android.R.id.content, LoginFragment.getInstance()).commit()
    }
}
