package com.wojtekmalek.expenseslog.ui.paragon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mcxiaoke.koi.log.logd
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import kotlinx.android.synthetic.main.activity_paragons.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File


@RuntimePermissions
class ParagonsActivity : AppCompatActivity() {

    val paragonsAdapter = ParagonsAdapter()
    lateinit var currentPictureId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paragons)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            takePictureWithPermissionCheck()
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = paragonsAdapter

        EasyImage.configuration(this)
                .setImagesFolderName("Paragons") // images folder name, default is "EasyImage"
                .saveInRootPicturesDirectory(); // if you want to use internal memory for storying images - default

        RealmHelper.getAllParagons().forEach {
            logd { it.uuid }
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun takePicture() {
        EasyImage.openChooserWithGallery(this, "Choose paragon", 0)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                paragonsAdapter.addNew(imageFile?.absolutePath ?: "")
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
            }

        })
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(
                    context,
                    ParagonsActivity::class.java
            ))
        }
    }
}
