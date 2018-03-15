package ru.example.simbirsoft.fragments

import android.app.Activity
import android.content.Intent
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
import kotlinx.android.synthetic.main.login_fragment.view.*
import ru.example.simbirsoft.R
import ru.example.simbirsoft.presenters.EditProfilePresenter
import ru.example.simbirsoft.views.EditProfileView
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
* Created by ag on 02.03.18.
*/
class EditProfileFragment : MvpBaseFragment(), EditProfileView {

    companion object {
        private const val LOAD_IMAGE = 1
        private const val LOAD_PHOTO = 2

        fun getInstance(): EditProfileFragment = EditProfileFragment()
    }

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.progress_bar_container) lateinit var mProgressBarContainer: FrameLayout
    @BindView(R.id.title_name) lateinit var mTitleNameTextView: TextView
    @BindView(R.id.change_photo_text_view) lateinit var mChangePhotoTextView: TextView
    @BindView(R.id.name_text_input_layout) lateinit var mNameTextInputLayout: TextInputLayout
    @BindView(R.id.phone_text_input_layout) lateinit var mPhoneTextInputLayout: TextInputLayout
    @BindView(R.id.email_text_input_layout) lateinit var mEmailTextInputLayout: TextInputLayout
    @BindView(R.id.old_password_text_input_layout) lateinit var mOldPasswordTextInputLayout: TextInputLayout
    @BindView(R.id.new_password_text_input_layout) lateinit var mNewPasswordTextInputLayout: TextInputLayout
    @BindView(R.id.repeat_password_text_input_layout) lateinit var mRepeatPasswordTextInputLayout: TextInputLayout
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

        RxTextView.textChanges(mOldPasswordTextInputLayout.editText!!).compose(bindToLifecycle())
                .skip(1).subscribe {
                    presenter.oldPasswordWasChanged(it.toString())
                }

        RxTextView.textChanges(mNewPasswordTextInputLayout.editText!!).compose(bindToLifecycle())
                .skip(1).subscribe {
                    presenter.newPasswordWasChanged(it.toString())
                }

        RxTextView.textChanges(mRepeatPasswordTextInputLayout.editText!!).compose(bindToLifecycle())
                .skip(1).subscribe {
                    presenter.repeatPasswordWasChanged(it.toString())
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    override fun returnToPreviousFragment() {
        fragmentManager.popBackStack()
    }

    override fun avatarValue(uri: Uri) {
        mAvatarImageView.imageView.setImageURI(uri)
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

    override fun oldPasswordIsValid(isValid: Boolean) {
        if (isValid) {
            mOldPasswordTextInputLayout.isErrorEnabled = false
        } else {
            mOldPasswordTextInputLayout.error = getString(R.string.invalid_old_password)
        }
    }

    override fun oldPasswordValue(value: String) {
        mOldPasswordTextInputLayout.editText?.setText(value)
    }

    override fun newPasswordIsValid(isValid: Boolean) {
        if (isValid) {
            mNameTextInputLayout.isErrorEnabled = false
        } else {
            mNewPasswordTextInputLayout.error = getString(R.string.invalid_new_password)
        }
    }

    override fun newPasswordValue(value: String) {
        mNewPasswordTextInputLayout.editText?.setText(value)
    }

    override fun repeatNewPasswordIsValid(isValid: Boolean) {
        if (isValid) {
            mRepeatPasswordTextInputLayout.isErrorEnabled = false
        } else {
            mRepeatPasswordTextInputLayout.error = getString(R.string.invalid_new_password)
        }
    }

    override fun repeatNewPasswordValue(value: String) {
        mRepeatPasswordTextInputLayout.editText?.setText(value)
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

    override fun clearFields() {
        mNameTextInputLayout.editText?.setText("")
        mNameTextInputLayout.isErrorEnabled = false
        mPhoneTextInputLayout.editText?.setText("")
        mPhoneTextInputLayout.isErrorEnabled = false
        mOldPasswordTextInputLayout.editText?.setText("")
        mOldPasswordTextInputLayout.isErrorEnabled = false
        mNewPasswordTextInputLayout.editText?.setText("")
        mNewPasswordTextInputLayout.isErrorEnabled = false
        mRepeatPasswordTextInputLayout.editText?.setText("")
        mRepeatPasswordTextInputLayout.isErrorEnabled = false
        mSendButton.isEnabled = false
        mSendButton.setBackgroundColor(getColor(R.color.colorAccent))
    }

    override fun progressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            mProgressBarContainer.visibility = View.VISIBLE
        } else {
            mProgressBarContainer.visibility = View.GONE
        }
    }

    override fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, LOAD_IMAGE)
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
                startActivityForResult(takePictureIntent, LOAD_PHOTO)
            }
        }
    }

    var mCurrentPhotoPath = ""

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ) {
            when (requestCode) {
                LOAD_IMAGE -> {
                    if (data?.data != null) {
                        showMessage("Image")
                    }
                }
                LOAD_PHOTO -> {
                    showMessage("Photo")
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,
                            Uri.parse(mCurrentPhotoPath))
                    bitmap?.let {
                        mAvatarImageView.setImageBitmap(it)
                    }
                }
            }
        }
    }

    @OnClick(R.id.change_photo_text_view)
    fun changePhotoTextViewClicked() {
        presenter.changeAvatarForPhotoWasClicked()
    }

    @OnClick(R.id.send_button)
    fun sendButtonClicked() {
        presenter.sendButtonClicked()
    }

    private fun initToolbar() {
        mToolbar.title = getString(R.string.edit)
        mToolbar.setNavigationOnClickListener {
            presenter.returnToPreviousFragmentButtonWasClicked()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}