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
import com.arellomobile.mvp.MvpDelegate
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject
import ru.example.simbirsoft.common.Empty

/**
* Created by ag on 16.02.18.
*/
abstract class MvpBaseFragment : Fragment() {

    private var mIsStateSaved: Boolean = false
    private var mMvpDelegate: MvpDelegate<MvpBaseFragment>? = null

    protected var unbinder: Unbinder? = null

    private val lifecycleSubject = BehaviorSubject.create<Empty>()

    protected open fun layoutResourceId() = View.NO_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        mIsStateSaved = false

        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()

        mIsStateSaved = false

        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        mIsStateSaved = true

        getMvpDelegate().onSaveInstanceState(outState!!)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()

        getMvpDelegate().onDetach()
        getMvpDelegate().onDestroyView()

        lifecycleSubject.onComplete()
        unbinder?.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity.isFinishing) {
            getMvpDelegate().onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent: Fragment? = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            getMvpDelegate().onDestroy()
        }
    }

    protected fun showToast(text: CharSequence, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, length).show()
    }

    protected fun getColor(resId: Int) = ContextCompat.getColor(context, resId)

    fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycle.bind(lifecycleSubject)

    protected fun getMvpDelegate(): MvpDelegate<*> {
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate(this)
        }

        return mMvpDelegate as MvpDelegate<MvpBaseFragment>
    }
}