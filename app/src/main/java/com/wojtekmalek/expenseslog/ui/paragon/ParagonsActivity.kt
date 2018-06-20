package com.wojtekmalek.expenseslog.ui.paragon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import kotlinx.android.synthetic.main.activity_paragons.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@RuntimePermissions
class ParagonsActivity : AppCompatActivity() {

    lateinit var paragonsAdapter: ParagonsAdapter

    lateinit var currentPictureId: String
    private val REQUEST_CAPTURE_IMAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paragons)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            takePictureWithPermissionCheck()
        }

        initParagonsList()

        /*EasyImage.configuration(this)
                .saveInAppExternalFilesDir()
                .setImagesFolderName("Paragons")
                .setCopyExistingPicturesToPublicLocation(true)*/

        if (intent.getBooleanExtra(SHOULD_START_PHOTO_PICKER, false)) {
            takePictureWithPermissionCheck()
        }
    }

    private fun initParagonsList() {
        paragonsAdapter = ParagonsAdapter(
                RealmHelper.getAllParagons(),
                { initParagonsList() }
        )
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = paragonsAdapter
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(applicationContext, "$packageName.share", photoFile)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI)
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE)
            }
        }
    }

    private var imagePath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        imagePath = image.absolutePath
        return image
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun takePicture() {
        //        EasyImage.openChooserWithGallery(this, "Choose paragon", 0)
        //        EasyImage.openCamera(this, 0)
        openCameraIntent()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //        if (data == null) return

        /*EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                paragonsAdapter.addNew(imageFile?.absolutePath ?: "")
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                toast("error")
            }

        })*/

        //        if (requestCode === REQUEST_CAPTURE_IMAGE && resultCode === Activity.RESULT_OK) {
        imagePath?.let { paragonsAdapter.addNew(it) }
        if (data != null && data.extras != null) {
            val imageBitmap = data.extras.get("data") as Bitmap
        }
        //        }
    }


    override fun onResume() {
        super.onResume()
        paragonsAdapter.apply {
            items.clear()
            items.addAll(RealmHelper.getAllParagons())
        }

    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    companion object {
        val SHOULD_START_PHOTO_PICKER = "start"
        fun startActivity(context: Context, startPhotoPicker: Boolean) {
            context.startActivity(Intent(
                    context,
                    ParagonsActivity::class.java
            ).apply {
                putExtra(SHOULD_START_PHOTO_PICKER, startPhotoPicker)
            }
            )
        }
    }
}
