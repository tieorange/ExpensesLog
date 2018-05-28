package com.wojtekmalek.expenseslog.ui.paragon

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.frosquivel.magicalcamera.MagicalCamera
import com.frosquivel.magicalcamera.MagicalPermissions
import com.mcxiaoke.koi.ext.toast
import com.wojtekmalek.expenseslog.R
import kotlinx.android.synthetic.main.activity_paragons.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*


@RuntimePermissions
class ParagonsActivity : AppCompatActivity() {

    lateinit var camera: MagicalCamera
    val paragonsAdapter = ParagonsAdapter(ParagonItem.getDummy())
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
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun takePicture() {
        currentPictureId = UUID.randomUUID().toString()

        val RESIZE_PHOTO_PIXELS_PERCENTAGE = 75
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        val magicalPermissions = MagicalPermissions(this, permissions)
        camera = MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions)

        // Call the camera takePicture method to open the existing camera
        try {
            camera.takePhoto()
        } catch (e: Exception) {
            toast("Can't take a picture")
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK) {
            //you should to call the method ever, for obtain the bitmap photo (= magicalCamera.getPhoto())
            camera.resultPhoto(requestCode, resultCode, data);
            //if you need save your bitmap in device use this method and return the path if you need this
            //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
            val path = camera.savePhotoInMemoryDevice(camera.getPhoto(), currentPictureId, "paragons", MagicalCamera.JPEG, true)
            paragonsAdapter.addNew(camera.photo, path)

            if (path != null) {
                Toast.makeText(this, "The photo is save in device, please check this path: $path", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show()
            }
        }
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
