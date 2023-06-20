package com.example.fragmentpractice3

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentpractice3.adapters.Adapters
import com.example.fragmentpractice3.database.AppDatabase
import com.example.fragmentpractice3.databinding.ActivityMainBinding
import com.example.fragmentpractice3.databinding.ItemViewBinding
import com.example.fragmentpractice3.datas.ReData
import com.example.fragmentpractice3.fragments.FirstFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.DateFormat
import java.util.*


class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, Adapters.ItemClickListener {

    private var nfcAdapter: NfcAdapter? = null
    private var dbHelper: DBHelper? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemBinding : ItemViewBinding

    // 어댑터 선언
    private lateinit var wordAdapter : Adapters
    // 리스트뷰의 한 아이템을 클릭했을 떄의 변수
    private var selectedWord: ReData? = null

    // 현재 주기상태는 resume상태일것이다.
    // 값을 추가하러 추가하는 액티비티에 할 때마다 DB검사 후 UI에 뿌려주면 효과적이지 못함
    // 그래서 값을 추가하는 액티비티로 넘어간 후 추가 버튼을 클릭했을 때 DB를 읽고 UI에 새롭게 뿌려줄거다
    private val updateAddWordResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        // 만약 DB의 데이터가 추가되었다면 가장 나중에 추가된 값을 가져오자
        val isUpdated = result.data?.getBooleanExtra("isUpdate",false) ?: false

        // 데이터를 추가하고 추가버튼을 눌렀는지 확인 후 데이터가 추가되었다면
        if(result.resultCode == RESULT_OK && isUpdated){
            updateAddWord()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        itemBinding = ItemViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인이 안되있으면 로그인 액티비티로 이동
        //startMainActivity()





        // NFC 어댑터 가져오기
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        // NFC 기능이 꺼져있는 경우 Toast 메시지로 안내
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC 기능이 꺼져 있습니다. NFC를 켜주세요.", Toast.LENGTH_LONG).show()
        }
        dbHelper = DBHelper(this)


        initRecylerView()
        binding.addButton.setOnClickListener {

            Intent(this, MainPageEditActivity::class.java).let{
                // startActivity대신 사용한다.
                updateAddWordResult.launch(it)
            }
        }
        
        // 지우기
        binding.mainPageUserInfo.setOnClickListener {
            delete()
        }



    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // NFC 태그를 읽었을 때 호출되는 메소드
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            val id = bytesToHex(tag.id)
            Log.v("READ", "READED ID = $id")
            val isInserted: Boolean = dbHelper!!.insertCheck(id)
            if (!isInserted) {
               // insertID(id)
                startActivity(Intent(this,MainPageEditActivity::class.java))
            }
//            else {
//                if (FirstFragment.isDelete)
//                    deleteId(id)
//                else {
//                    Log.v("INSERTED", "INSERTED")
//                    val data = dbHelper!!.getData(id)
//                    Log.v(
//                        "DATA",
//                        "id : ${data.id} || act : ${data.activity} || status : ${data.status}"
//                    )
//                    if (data.status == 1) {
//                        Toast.makeText(
//                            this,
//                            "${data.id}의 동작 ${data.activity}을/를 성공적으로 마쳤습니다.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        data.status = 0
//                        dbHelper!!.updateData(data)
//                    } else
//                        Toast.makeText(this, "${data.id}는 이미 동작을 끝냈습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
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
            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null) // nfc태그시 뭐 되는데 뭐지

            updateAddWord()//시간 설정 후  UI업데이트 시킴
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
            textView.text = "${textView.text}${each.id} : ${each.activity}\n"
        }
    }
    @SuppressLint("ResourceType")
//    private fun insertID(id: String) {
//        val textView:TextView = findViewById(R.id.text1_1)
//        var timePicker = TimePickerFragment()
//        Log.v("INSERT", "ID : $id")
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
//        builder.setTitle("값 입력")
//        val input = EditText(this@MainActivity)
//        builder.setView(input)
//        builder.setPositiveButton("등록") { dialog, _ ->
//            val textValue = input.text.toString()
//            val db: SQLiteDatabase = dbHelper!!.writableDatabase
//            val values = ContentValues()
//            values.put("ID", id)
//            values.put("activity", textValue)
//            values.put("status", 0)
//            val newRowId = db.insert("activity", null, values)
//            if (newRowId != -1L) {
//                Toast.makeText(this@MainActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
//                timePicker.show(supportFragmentManager, "Time Picker")
//            } else {
//                Toast.makeText(this@MainActivity, "등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
//            }
//            db.close()
//            dialog.dismiss()
//            textView.text = "${textView.text}$id : $textValue \n"
//        }
//        builder.setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
//        builder.show()
//    }
//    private fun deleteId(id: String) {
//        try {
//            val data = Data(id, null, 0)
//            dbHelper!!.deleteData(data)
//            resetText()
//            Toast.makeText(this@MainActivity, "$id 제거에 성공했습니다.", Toast.LENGTH_SHORT).show()
//        }catch (e: Exception){
//            Toast.makeText(this@MainActivity, "$id 제거에 실패했습니다.\n$e", Toast.LENGTH_SHORT).show()
//        }
//    }
    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, min: Int) {
        var c = Calendar.getInstance()
        Log.v("TIMESET", "HOUR : $hourOfDay || MIN : $min")
        //시간설정
        c.set(Calendar.HOUR_OF_DAY, hourOfDay) //시
        c.set(Calendar.MINUTE, min) // 분
        c.set(Calendar.SECOND,0)    //초
        // 알람설정
        startAlarm(c)
    }
    private fun startAlarm(c: Calendar) {
        Log.v("STARTALARM", "STARTALARM")
        // 알람매니저 선언
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        //사용자가 선택한 알람 시간 데이터 담기
        val curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        intent.putExtra("time", curTime)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE)
        //설정 시간이 현재시간 이후라면 설정
        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    // 앱 시작시 첫 메인 화면 동작에 관하여
    fun startMainActivity(){
        val currentUser = Firebase.auth.currentUser

        // 만약 로그인이 안되있으면
        // 메인액티비티가 꺼지고 로그인 액티비티로 간다.
        if(currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    // -------------------------------------------------------리사이클로뷰 시작----------------------------------------------------------------------------------------

    private fun initRecylerView(){

        wordAdapter = Adapters(mutableListOf(),this)


        // 리사이클러뷰랑 어댑터 연결
        binding.wordRecyclerView.apply {
            //리사이클러뷰에 어댑터 연결
            adapter = wordAdapter

            // 레이아웃 메니저 설정
            layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL,false)

        }

        Thread{
            // db에 있는 데이터 들고오기
            // 리사이클러뷰에 뿌릴거임
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()

            //어댑터에 db에서 들고온 데이터 연결
            wordAdapter.list.addAll(list)

            // notifyDataSetChanged()를 사용할 땐 어댑터의 새로운 내룡이 그려져 UI가 변경된다.
            // 그러므로 runOnUiThread를 사용한다.
            runOnUiThread {
                // 어댑터의 데이터의 변화가 있는지 확인 후 화면에 뿌려준다.
                wordAdapter.notifyDataSetChanged()
            }

        }.start()


    }

    // db에 데이터가 추가 되었다면
    private fun updateAddWord(){

        // db에 접근하려면 쓰레드 활용
        Thread{
            // db에서 값 들고오고
            AppDatabase.getInstance(this)?.wordDao()?.getLateStWord()?.let { word ->
                // 어댑터에 리스트형태로 add하는데 가장 최신의 데이터를 들고올거라서 인덱스값을 0으로 준다.
                wordAdapter.list.add(0,word)

                // 데이터의 변화가 있으니 어댑터를 다시 ui에 로드한다.
                runOnUiThread { wordAdapter.notifyDataSetChanged() }
            }

        }.start()
    }

    // 제거버튼 클릭 시
    private fun delete(){
        // 만약 아이템을 선택하지 않고 사젝버튼을 눌렀을 때
        // 아무일도 일어나지 않는다.
        if(selectedWord == null) return

        Thread{
            selectedWord?.let{word ->
                //DB에 접근하여 selectedWord가 있는 아이템을??
                AppDatabase.getInstance(this)?.wordDao()?.delete(word)
                // 데이터의 변화가 있으니 어댑터를 다시 ui에 로드한다.
                runOnUiThread {
                    // 어댑터에서도 해당 데이터를 삭제시킨다.
                    wordAdapter.list.remove(word)
                    wordAdapter.notifyDataSetChanged()
                    // 단순 목록 초기화

                    Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
                }


            }

        }.start()
    }


    // 어댑터 클래스의 클릭을 위한 인터페이스를 활용
    override fun onClick(word: ReData) {
        // 클릭된 한 아이템의 정보를 받아와
        selectedWord = word

    }


}