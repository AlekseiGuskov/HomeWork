package ru.example.simbirsoft.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import ru.example.simbirsoft.presenters.BasePresenter

/**
* Created by ag on 16.02.18.
*/
abstract class BaseFragment<P : BasePresenter> : Fragment() {

    protected var unbinder: Unbinder? = null

    protected var presenter: P? = null

    abstract fun createPresenter(): P

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
        presenter?.viewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.viewDestroy()
        unbinder?.unbind()
    }
}