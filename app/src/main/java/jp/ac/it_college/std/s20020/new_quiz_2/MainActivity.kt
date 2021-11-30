package jp.ac.it_college.std.s20020.new_quiz_2

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.lifecycleScope
import jp.ac.it_college.std.s20020.new_quiz_2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import kotlin.time.DurationUnit

class MainActivity : AppCompatActivity() {


    private lateinit var _helper: DatabaseHelper

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startbutton.setOnClickListener(){
            next()
        }

    }

    //画面生成後、よばれる。
    override fun onResume() {
        super.onResume()
        getVersion("https://script.google.com/macros/s/AKfycbznWpk2m8q6lbLWSS6qaz3uS6j3L4zPwv7CqDEiC433YOgAdaFekGJmjoAO60quMg6l/exec?f=version")

    }

    override fun onDestroy() {
        //DBヘルパーオブジェクトの解放
        _helper.close()
        super.onDestroy()
    }

    //versionを取得
    //自分でつくる
    @UiThread
    private fun getVersion(url: String) {
        //インポートしたリサイクルを使う
        lifecycleScope.launch {
            val result = getJsonBack(url)
            getVersionPost(result)
        }
    }

    //dateを取得
    //自分でつくる
    @UiThread
    private fun getDate(url: String) {
        lifecycleScope.launch {
            val result = getJsonBack(url)

            getDatePost(result)
        }
    }


    //自分でつくる
    //jsonをとってくるおもに働くもの
    @WorkerThread
    private suspend fun getJsonBack(url:String): String {
        val res = withContext(Dispatchers.IO) {
            var result = ""
            val url = URL(url)
            val con = url.openConnection() as? HttpURLConnection
            con?.let {
                try{
                    it.connectTimeout = 15000
                    it.readTimeout = 15000
                    it.requestMethod = "GET"
                    it.connect()

                    val stream = it.inputStream
                    result = extedString(stream)
                    stream.close()
                } catch(ex: SocketTimeoutException) {
                    print("通信タイムアウト")
                }
                it.disconnect()
            }
            result
        }
        return res
    }


    //自分でつくる
    @UiThread
    private fun getVersionPost(result: String) {
        var oldVersion = "20211130"
        println(oldVersion)
        val newVersion = JSONObject(result).getString("version")
        println(newVersion)
        if (newVersion != oldVersion){
            _helper = DatabaseHelper(this)
            val db = _helper.writableDatabase
            val delete = """DELETE FROM Quiz;
        """.trimIndent()
            val stmt = db.compileStatement(delete)
            stmt.executeUpdateDelete()

            oldVersion = newVersion


            getDate("https://script.google.com/macros/s/AKfycbznWpk2m8q6lbLWSS6qaz3uS6j3L4zPwv7CqDEiC433YOgAdaFekGJmjoAO60quMg6l/exec?f=data")
        }
        binding.textView.text = oldVersion

    }

    //自分でつくる
    @UiThread
    private fun getDatePost(result: String) {

        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(this)






        val rootJSON = JSONArray(result)
        println(rootJSON)

        var i = 0

        while(i < rootJSON.length()){


            val id = rootJSON.getJSONObject(i).getLong("id")
            val question = rootJSON.getJSONObject(i).getString("question")
            val answers = rootJSON.getJSONObject(i).getLong("answers")
            val choices = rootJSON.getJSONObject(i).getJSONArray("choices")
            val choices_1 = choices[0].toString()
            val choices_2 = choices[1].toString()
            val choices_3 = choices[2].toString()
            val choices_4 = choices[3].toString()
            val choices_5 = choices[4].toString()
            val choices_6 = choices[5].toString()


            //データベース接続オブジェクトを取得
            val db = _helper.writableDatabase




            val insert = """INSERT INTO Quiz
            (_id, question, answers, choices_1, choices_2, choices_3,choices_4, choices_5, choices_6)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

            //SQL文字列をもとにプリペアドステートメントを取得
            val stmt = db.compileStatement(insert)

            //変数のバイド。
            stmt.bindLong(1, id)
            stmt.bindString(2, question)
            stmt.bindLong(3, answers)
            stmt.bindString(4, choices_1)
            stmt.bindString(5, choices_2)
            stmt.bindString(6, choices_3)
            stmt.bindString(7, choices_4)
            stmt.bindString(8, choices_5)
            stmt.bindString(9, choices_6)

            stmt.executeInsert()

            i = i + 1

        }





    }

    private fun extedString(stream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        return reader.readText()
    }

    private fun next() {
        var time = 0
        var score = 0

        val intent = Intent(this, Quiz::class.java)
        startActivity(intent)
    }




}