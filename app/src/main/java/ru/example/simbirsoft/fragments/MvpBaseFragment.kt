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
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.MvpDelegate
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject
import ru.example.simbirsoft.common.Empty

/**
* Created by ag on 16.02.18.
*/
abstract class MvpBaseFragment : MvpAppCompatFragment() {

    protected var unbinder: Unbinder? = null

    private val lifecycleSubject = BehaviorSubject.create<Empty>()

    protected open fun layoutResourceId() = View.NO_ID

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleSubject.onComplete()
        unbinder?.unbind()
    }

    protected fun showToast(text: CharSequence, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, length).show()
    }

    protected fun getColor(resId: Int) = ContextCompat.getColor(context, resId)

    fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycle.bind(lifecycleSubject)
}