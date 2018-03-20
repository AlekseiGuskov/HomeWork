package ru.example.simbirsoft.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.jakewharton.rxbinding2.widget.RxTextView
import de.hdodenhof.circleimageview.CircleImageView
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.EditProfilePresenter
import ru.example.simbirsoft.views.EditProfileView
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.PopupMenu
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
* Created by ag on 02.03.18.
*/
class EditProfileFragment : MvpBaseFragment(), EditProfileView, LoginFragment.ILoginSuccessCallback {

    companion object {
        private const val LOAD_IMAGE_REQ = 1
        private const val LOAD_PHOTO_REQ = 2

        fun getInstance(): EditProfileFragment = EditProfileFragment()
    }

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.progress_bar_container) lateinit var mProgressBarContainer: FrameLayout
    @BindView(R.id.title_name) lateinit var mTitleNameTextView: TextView
    @BindView(R.id.name_text_input_layout) lateinit var mNameTextInputLayout: TextInputLayout
    @BindView(R.id.phone_text_input_layout) lateinit var mPhoneTextInputLayout: TextInputLayout
    @BindView(R.id.email_text_input_layout) lateinit var mEmailTextInputLayout: TextInputLayout
    @BindView(R.id.avatar) lateinit var mAvatarImageView: CircleImageView
    @BindView(R.id.send_button) lateinit var mSendButton: Button

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: EditProfilePresenter

    override fun layoutResourceId(): Int = R.layout.edit_profile_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init()
        initToolbar()

        RxTextView.textChanges(mNameTextInputLayout.editText!!).compose(bindToLifecycle())
                .skip(1).subscribe {
                    presenter.nameWasChanged(it.toString())
                }

        RxTextView.textChanges(mEmailTextInputLayout.editText!!).compose(bindToLifecycle())
                .skip(1).subscribe {
                    presenter.emailWasChanged(it.toString())
                }

        RxTextView.textChanges(mPhoneTextInputLayout.editText!!).compose(bindToLifecycle())
                .skip(1).subscribe {
                    presenter.phoneWasChanged(it.toString())
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun returnToPreviousFragment() {
        fragmentManager.popBackStack()
    }

    override fun avatarValue(imageUri: Uri) {
        Picasso.get().load(imageUri).into(mAvatarImageView)
    }

    override fun nameIsValid(isValid: Boolean) {
        if (isValid) {
            mNameTextInputLayout.isErrorEnabled = false
        } else {
            mNameTextInputLayout.error = getString(R.string.invalid_name)
        }
    }

    override fun nameValue(value: String) {
        mNameTextInputLayout.editText?.setText(value)
    }

    override fun phoneIsValid(isValid: Boolean) {
        if (isValid) {
            mPhoneTextInputLayout.isErrorEnabled = false
        } else {
            mPhoneTextInputLayout.error = getString(R.string.invalid_phone)
        }
    }

    override fun phoneValue(value: String) {
        mPhoneTextInputLayout.editText?.setText(value)
    }

    override fun emailIsValid(isValid: Boolean) {
        if (isValid) {
            mEmailTextInputLayout.isErrorEnabled = false
        } else {
            mEmailTextInputLayout.error = getString(R.string.invalid_email)
        }
    }

    override fun emailValue(value: String) {
        mEmailTextInputLayout.editText?.setText(value)
    }

    override fun sendButtonState(state: Boolean) {
        mSendButton.isEnabled = state
        if (state) {
            mSendButton.setBackgroundColor(getColor(R.color.colorGreenLight))
        } else {
            mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
        }
    }

    override fun showMessage(text: String) {
        showToast(text)
    }

    override fun dataSaved(name: String) {
        mTitleNameTextView.text = name
        showToast(getString(R.string.data_saved))
    }

    override fun progressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            mProgressBarContainer.visibility = View.VISIBLE
        } else {
            mProgressBarContainer.visibility = View.GONE
        }
    }

    override fun needAuthorize() {
        fragmentManager.beginTransaction()
                .addToBackStack(LoginFragment::class.simpleName.toString())
                .add(android.R.id.content, LoginFragment.getInstance(this)).commit()
    }

    override fun showPopupMenu() {
        showChangeAvatarPopupMenu()
    }

    override fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, LOAD_IMAGE_REQ)
    }

    override fun makePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            var photo: File? = null
            try {
                photo = createImageFile()
            } catch (e: IOException) {

            }
            photo?.let {
                val uri = FileProvider.getUriForFile(context, "ru.example.simbirsoft.fileprovider", it)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(takePictureIntent, LOAD_PHOTO_REQ)
            }
        }
    }

    private var mCurrentPhotoPath = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }

    private fun showChangeAvatarPopupMenu() {
        val changeAvatarPopupMenu = PopupMenu(context, mAvatarImageView)
        changeAvatarPopupMenu.inflate(R.menu.change_avatar_popup)
        changeAvatarPopupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.photo -> {
                    presenter.changeAvatarForPhotoWasClicked()
                    true
                }
                R.id.gallery -> {
                    presenter.changeAvatarForImageWasClicked()
                    true
                }
                else -> {
                    false
                }
            }
        }
        changeAvatarPopupMenu.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ) {
            when (requestCode) {
                LOAD_IMAGE_REQ -> {
                    if (data?.data != null) {
                        val imageUri = data.data
                        cropImage(imageUri)
                    }
                }
                LOAD_PHOTO_REQ -> {
                    val imageUri = Uri.parse(mCurrentPhotoPath)
                    cropImage(imageUri)
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data);
                    val resultUri = result.uri
                    resultUri?.let {
                        presenter.newCroppedAvatar(it)
                    }
                }
            }
        }
    }

    override fun loginSuccess(user: FirebaseUser) {
        presenter.takeDataFromDatabaseForUser(user)
    }

    @OnClick(R.id.change_photo_text_view)
    fun changePhotoTextViewClicked() {
        presenter.changeAvatarFieldClicked()
    }

    @OnClick(R.id.send_button)
    fun sendButtonClicked() {
        presenter.sendButtonClicked()
    }

    private fun cropImage(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setFixAspectRatio(true)
                .setAutoZoomEnabled(false)
                .start(context, this)
        /*fragmentManager.beginTransaction()
                .addToBackStack(CropImageFragment::class.simpleName.toString())
                .add(android.R.id.content,
                        CropImageFragment.createInstance(imageUri,
                                this)).commit()*/
    }

    private fun initToolbar() {
        mToolbar.title = getString(R.string.edit)
        mToolbar.setNavigationOnClickListener {
            presenter.returnToPreviousFragmentButtonWasClicked()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}