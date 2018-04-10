package ru.example.simbirsoft.presenters

import android.net.Uri
import android.util.Log
import android.util.Patterns
import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.example.simbirsoft.models.User
import ru.example.simbirsoft.views.EditProfileView
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.FirebaseStorage
import ru.example.simbirsoft.R


/**
* Created by ag on 02.03.18.
*/
@InjectViewState
class EditProfilePresenter : BasePresenter<EditProfileView>() {

    private var mIsFirstAttach = true

    private var mAvatarUri: Uri? = null
    private var mName = ""
    private var mPhone = ""
    private var mEmail = ""
    private var mLat = .0
    private var mLng = .0

    private var mUploadTask: UploadTask? = null
    private var mDownloadAvatarUri: Uri? = null

    private var mUserUid: String = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        mIsFirstAttach = false
        val currentUser = FirebaseAuth.getInstance().currentUser
        mUserUid = currentUser?.uid ?: ""
        if (currentUser == null) {
            viewState.needAuthorize()
        } else {
            takeDataFromDatabaseForUser(currentUser)
        }
    }

    override fun init() {
        super.init()
        if (!mIsFirstAttach) {
            mAvatarUri?.let {
                viewState.avatarValue(it)
            }
            viewState.titleNameValue(mName)
            viewState.nameValue(mName)
            viewState.phoneValue(mPhone)
            viewState.emailValue(mEmail)
            viewState.nameIsValid(mName.isNotEmpty())
            viewState.phoneIsValid(isValidPhone(mPhone))
            viewState.emailIsValid(isValidEmail(mEmail))
            changeSendButtonState()
            viewState.progressBarVisibility(false)
        }
    }

    override fun destroy() {
        super.destroy()
        mUploadTask?.cancel()
    }

    fun nameWasChanged(value: String) {
        mName = value
        viewState.nameIsValid(value.isNotEmpty())
        changeSendButtonState()
    }

    fun phoneWasChanged(value: String) {
        mPhone = value
        viewState.phoneIsValid(isValidPhone(value))
        changeSendButtonState()
    }

    fun emailWasChanged(value: String) {
        mEmail = value
        viewState.emailIsValid(isValidEmail(value))
        changeSendButtonState()
    }

    fun sendButtonClicked() {
        if (!mAvatarUri?.toString().isNullOrEmpty()) {
            saveAvatar(mAvatarUri!!, {
                saveData()
            })
        } else {
            saveData()
        }
    }

    private fun saveData() {
        val database = FirebaseDatabase.getInstance().reference.child("users").child(mUserUid)
        database
                .setValue(User(mDownloadAvatarUri.toString(), mEmail, mName, mPhone, mLat, mLng))
                .addOnCompleteListener {
                    viewState.dataSaved(mName)
                    viewState.showMessage(getString(R.string.completed))
                    viewState.returnToPreviousFragment()
                }
                .addOnFailureListener {
                    viewState.showMessage(it.message.toString())
                    viewState.returnToPreviousFragment()
                }
    }

    fun changeAvatarFieldClicked() {
        viewState.showPopupMenu()
    }

    fun returnToPreviousFragmentButtonWasClicked() {
        viewState.returnToPreviousFragment()
    }

    fun changeAvatarForImageWasClicked() {
        viewState.chooseImage()
    }

    fun changeAvatarForPhotoWasClicked() {
        viewState.makePhoto()
    }

    fun newCroppedAvatar(imageUri: Uri) {
        mAvatarUri = imageUri
        viewState.avatarValue(imageUri)
    }

    private fun saveAvatar(imageBitmap: Uri, successTask: () -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val riversRef = storageRef.child("avatar/$mUserUid/avatar.jpg")

        mUploadTask = riversRef.putFile(imageBitmap)
        mUploadTask?.addOnSuccessListener({ taskSnapshot ->
            // Get a URL to the uploaded content
            mDownloadAvatarUri = taskSnapshot.downloadUrl
            successTask.invoke()
        })
                ?.addOnFailureListener({
                    // Handle unsuccessful uploads
                    successTask.invoke()
                })
    }

    fun takeDataFromDatabaseForUser(currentUser: FirebaseUser) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").child(currentUser.uid)
                .addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError?) {
                        viewState.progressBarVisibility(false)
                        val exception = error?.toException()
                        Log.w(this::class.java.simpleName,
                                "Failed to read value.", exception)
                        viewState.showMessage("Failed to read value ${exception.toString()}")
                    }

                    override fun onDataChange(data: DataSnapshot?) {
                        val user = data?.getValue(User::class.java)
                        mName = user?.name ?: ""
                        mEmail = user?.email ?: ""
                        mPhone = user?.phone ?: ""
                        mLat = user?.lat ?: .0
                        mLng = user?.lng ?: .0
                        user?.avatar?.let {
                            if (it.isNotEmpty()) {
                                mAvatarUri = Uri.parse(it)
                                viewState.avatarValue(mAvatarUri!!)
                            }
                        }
                        viewState.titleNameValue(mName)
                        viewState.nameValue(mName)
                        viewState.phoneValue(mPhone)
                        viewState.emailValue(mEmail)
                        viewState.progressBarVisibility(false)
                    }
                })
    }

    private fun isValidEmail(value: String) = Patterns.EMAIL_ADDRESS.matcher(value).matches()

    private fun isValidPhone(value: String) = Patterns.PHONE.matcher(value).matches()

    private fun changeSendButtonState() {
        viewState.sendButtonState(mName.isNotEmpty() && isValidPhone(mPhone) &&
                isValidEmail(mEmail))
    }
}