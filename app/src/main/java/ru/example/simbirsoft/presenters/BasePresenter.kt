package ru.example.simbirsoft.presenters

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject
import ru.example.simbirsoft.Application
import ru.example.simbirsoft.Empty
import ru.example.simbirsoft.Util

/**
 * Created by ag
 * on 16.02.18.
 */
abstract class BasePresenter {

    open fun init() {}

    open fun destroy() {}

    protected fun getString(resId: Int): String =
            Application.sApplicationContext.getString(resId)
}