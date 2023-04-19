package com.example.fragmentpractice3

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmentpractice3.adapters.MainAdapter
import com.example.fragmentpractice3.R
import com.example.fragmentpractice3.fragments.FirstFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_first.*


class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter : MainAdapter
    private var nfcAdapter: NfcAdapter? = null
    private var dbHelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentLayoutId = findViewById<View>(android.R.id.content).rootView.id
        Log.v("currentLayout", "$currentLayoutId")

        mAdapter = MainAdapter(supportFragmentManager)
        mainViewPager.adapter = mAdapter
        mainTabLayout.setupWithViewPager(mainViewPager)

        // NFC 어댑터 가져오기
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // NFC 기능이 꺼져있는 경우 Toast 메시지로 안내
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC 기능이 꺼져 있습니다. NFC를 켜주세요.", Toast.LENGTH_LONG).show()
        }
        dbHelper = DBHelper(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // NFC 태그를 읽었을 때 호출되는 메소드
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {

            val id = bytesToHex(tag.id)
            Log.v("READ", "READED ID = $id")
            val isInserted: Boolean = dbHelper!!.insert_check(id)
            if (!isInserted) {
                insertID(id)
            } else
                deleteId(id)
        }
    }

    override fun onResume() {
        super.onResume()

        // Foreground Dispatch 설정
        if (nfcAdapter != null) {
            val intent = Intent(this, javaClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            // PendingIntent 생성
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

            // Foreground Dispatch 설정
            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
        }
    }

    override fun onPause() {
        super.onPause()

        // Foreground Dispatch 해제
        if (nfcAdapter != null) {
            nfcAdapter!!.disableForegroundDispatch(this)
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }

    private fun resetText() {
        val textView: TextView = findViewById(R.id.text1_1)
        textView.text = ""

        val datalist = dbHelper!!.getAllData()

        for (each in datalist) {
            val data = Data(each.id, each.activity)
            textView.text = "${textView.text}${data.id} : ${data.activity}\n"
        }
    }
    @SuppressLint("ResourceType")
    private fun insertID(id: String) {
        val textView:TextView = findViewById(R.id.text1_1)

        Log.v("INSERT", "ID : $id")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("값 입력")
        val input = EditText(this@MainActivity)
        builder.setView(input)
        builder.setPositiveButton("등록") { dialog, _ ->
            val textValue = input.text.toString()
            val db: SQLiteDatabase = dbHelper!!.writableDatabase
            val values = ContentValues()
            values.put("ID", id)
            values.put("activity", textValue)
            val newRowId = db.insert("activity", null, values)
            if (newRowId != -1L) {
                Toast.makeText(this@MainActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            db.close()
            dialog.dismiss()

            textView.text = "${textView.text}$id : $textValue \n"
        }
        builder.setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun deleteId(id: String) {
        try {
            val data = Data(id, null)
            dbHelper!!.deleteData(data)
            resetText()
            Toast.makeText(this@MainActivity, "$id 제거에 성공했습니다.", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(this@MainActivity, "$id 제거에 실패했습니다.\n$e", Toast.LENGTH_SHORT).show()
        }
    }
}