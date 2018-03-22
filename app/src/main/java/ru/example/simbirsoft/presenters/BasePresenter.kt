package ru.example.simbirsoft.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.example.simbirsoft.Application

/**
 * Created by ag
 * on 16.02.18.
 */
abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    open fun init() {}

    open fun destroy() {}

    protected fun getString(resId: Int): String =
            Application.sApplicationContext.getString(resId)
}