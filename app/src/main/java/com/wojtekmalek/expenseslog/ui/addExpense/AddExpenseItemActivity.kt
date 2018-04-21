package com.wojtekmalek.expenseslog.ui.addExpense

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mcxiaoke.koi.ext.onClick
import com.mcxiaoke.koi.ext.toast
import com.wojtekmalek.expenseslog.R
import kotlinx.android.synthetic.main.activity_add_expense_item.*
import kotlinx.android.synthetic.main.content_add_expense_item.*
import net.gotev.speech.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
open class AddExpenseItemActivity : AppCompatActivity() {

    private lateinit var categoryId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense_item)
        setSupportActionBar(toolbar)
        categoryId = intent.getStringExtra("extra")

        fab.setOnClickListener { view ->
            addExpenseItem()
        }

        Speech.init(this, packageName)
        microphone.onClick { speechToTextWithPermissionCheck() }
    }

    private fun addExpenseItem() {
        val price = price.text.toString()
        if (price.isEmpty()) return

        RealmHelper.addExpense(price.toFloat(), categoryId)
        toast("Add an item for $price")
        finish()
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun speechToText() {
        try {
            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    Log.i("speech", "speech recognition is now active")
                }

                override fun onSpeechRmsChanged(value: Float) {
                    Log.d("speech", "rms is now: $value")
                }

                override fun onSpeechPartialResults(results: List<String>) {
                    val stringBuilder = StringBuilder()
                    for (res in results) {
                        stringBuilder.append(res).append(" ")
                    }

                    Log.i("speech", "partial result: " + stringBuilder.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    price.text = result
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
            SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Speech.getInstance().shutdown()
    }

    companion object {
        fun startActivity(context: Context, categoryId: String) {
            val intent = Intent(context, AddExpenseItemActivity::class.java).apply {
                putExtra("extra", categoryId)
            }
            context.startActivity(intent)
        }
    }
}
