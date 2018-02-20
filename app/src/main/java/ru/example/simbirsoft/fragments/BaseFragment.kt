package ru.example.simbirsoft.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject
import ru.example.simbirsoft.Empty
import ru.example.simbirsoft.presenters.BasePresenter

/**
* Created by ag on 16.02.18.
*/
abstract class BaseFragment<P : BasePresenter> : Fragment() {

    protected var unbinder: Unbinder? = null

    var presenter: P? = null

    private val lifecycleSubject = BehaviorSubject.create<Empty>()

    abstract fun createPresenter(): P

    protected open fun layoutResourceId() = View.NO_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutResourceId = layoutResourceId()
        if (layoutResourceId == View.NO_ID) {
            return null
        }
        return inflater?.inflate(layoutResourceId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)
        presenter?.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleSubject.onComplete()
        presenter?.destroy()
        unbinder?.unbind()
    }

    protected fun showToast(text: CharSequence, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, length).show()
    }

    protected fun getColor(resId: Int) = ContextCompat.getColor(context, resId)

    fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycle.bind(lifecycleSubject)
}