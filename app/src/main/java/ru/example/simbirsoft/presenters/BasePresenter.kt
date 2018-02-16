package ru.example.simbirsoft.presenters

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject
import ru.example.simbirsoft.Empty

/**
* Created by ag on 16.02.18.
*/
abstract class BasePresenter {

    private val lifecycleSubject = BehaviorSubject.create<Empty>()

    open fun viewCreated() {}

    open fun viewDestroy() {
        lifecycleSubject.onComplete()
    }

    fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycle.bind(lifecycleSubject)
}