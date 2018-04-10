package ru.example.simbirsoft.presenters

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.edit_profile_fragment.view.*
import ru.example.simbirsoft.models.User
import ru.example.simbirsoft.views.MapView

/**
 * Created by ag on 10.04.18.
 */
class MapPresenter: BasePresenter<MapView>() {

    private var mIsFirstAttach = true

    private var mAuthorizeTask: Task<AuthResult>? = null
    private var mUserList = mutableListOf<User>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        mIsFirstAttach = false
        getData()
    }

    override fun init() {
        super.init()
        if (!mIsFirstAttach) {
            viewState.dataLoaded(mUserList)
        }
    }

    override fun destroy() {
        super.destroy()
        mAuthorizeTask = null
    }

    private fun getData() {
        val database = FirebaseDatabase.getInstance().reference.child("users")
        database.addChildEventListener(object :ChildEventListener {
            override fun onCancelled(error: DatabaseError?) {
                val exception = error?.toException()
                Log.w(this::class.java.simpleName,
                        "Failed to read value.", exception)
                viewState.showMessage("Failed to read value ${exception.toString()}")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(data: DataSnapshot, p1: String?) {
                val user = data.getValue(User::class.java)
                user?.let { mUserList.add(it) }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        })
        viewState.dataLoaded(mUserList)
    }
}
