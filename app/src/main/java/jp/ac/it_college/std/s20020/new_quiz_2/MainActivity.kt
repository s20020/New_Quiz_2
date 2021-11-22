package jp.ac.it_college.std.s20020.new_quiz_2

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

    //private val  URL = "https://script.google.com/macros/s/AKfycbznWpk2m8q6lbLWSS6qaz3uS6j3L4zPwv7CqDEiC433YOgAdaFekGJmjoAO60quMg6l/exec?f=data"

    private lateinit var _helper: DatabaseHelper

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //データベース接続オブジェクトを取得
        binding.button.setOnClickListener() {
//            val db = _helper.writableDatabase

            binding.test.text = "taiga"
        }

//       println(db)

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
        val oldVersion = ""
        val newVersion = JSONObject(result).getString("version")
        if (newVersion != oldVersion){
            getDate("https://script.google.com/macros/s/AKfycbznWpk2m8q6lbLWSS6qaz3uS6j3L4zPwv7CqDEiC433YOgAdaFekGJmjoAO60quMg6l/exec?f=data")
        }
        binding.textView.text = newVersion

    }

    //自分でつくる
    @UiThread
    private fun getDatePost(result: String) {
        val rootDate = JSONArray(result)

        //ここのデータがすべてある。now
        println(rootDate)
    }

    private fun extedString(stream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        return reader.readText()
    }







}