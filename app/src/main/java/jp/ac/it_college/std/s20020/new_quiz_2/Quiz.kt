package jp.ac.it_college.std.s20020.new_quiz_2

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import jp.ac.it_college.std.s20020.new_quiz_2.databinding.ActivityQuizBinding
import kotlin.concurrent.timer

class Quiz : AppCompatActivity() {

    private lateinit var _helper: DatabaseHelper
    private var q_list = mutableListOf<String>()
    private var shuffl_list = mutableListOf<Int>()
    private var on_list = mutableListOf<Int>()
    private var answer_num = 0
    private var answer_list = mutableListOf<Int>()
    private var total_time : Long = 0
    private var total_score = 0
    private var index = 1

//    private lateinit var timer_start:String


    private lateinit var binding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //タイマーインスタンスを生成
        val timer = object : CountDownTimer(11000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val second = millisUntilFinished / 1000L % 60L
                binding.watchTime.text = second.toString()
            }
            override fun onFinish() {
//                binding.okButton.performClick()
            }
         }

        binding.okButton.setOnClickListener {
            timer.cancel()
            colection()
        }

        //自身のインテントを受け取る
//        var index = intent.getIntExtra("INDEX",1)
//        var total_score = intent.getIntExtra("TOTAL_SCORE",0)
//        var total_time = intent.getLongExtra("TOTAL_TIME", 0)
    }

    override fun onResume() {
        super.onResume()

        //タイマーを起動
        val timer_start = System.currentTimeMillis()

        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(this)

        //データベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        //ランダムな数字を生成
        var ram = (1001..1075).random()
        println(ram)


        //ランダムに問題を取得
        val select = """
            SELECT _id, question, answers, choices_1, choices_2, choices_3, choices_4, choices_5, choices_6 FROM Quiz
            WHERE _id = ${ram}
        """.trimIndent()


        //データベースのそれぞれの値を変数に格納。
        var c = db.rawQuery(select, null)
        c.moveToNext()

        val c_id = c.getColumnIndex("_id")
        val id = c.getString(c_id)
        val c_question = c.getColumnIndex("question")
        val question = c.getString(c_question)
        val c_answers = c.getColumnIndex("answers")
        answer_num = c.getInt(c_answers)
        val c_choices_1 = c.getColumnIndex("choices_1")
        val choices_1 = c.getString(c_choices_1)
        val c_choices_2 = c.getColumnIndex("choices_2")
        val choices_2 = c.getString(c_choices_2)
        val c_choices_3 = c.getColumnIndex("choices_3")
        val choices_3 = c.getString(c_choices_3)
        val c_choices_4 = c.getColumnIndex("choices_4")
        val choices_4 = c.getString(c_choices_4)
        val c_choices_5 = c.getColumnIndex("choices_5")
        val choices_5 = c.getString(c_choices_5)
        val c_choices_6 = c.getColumnIndex("choices_6")
        val choices_6 = c.getString(c_choices_6)


        //選択肢の文字列リスト -> q_list
        q_list.run {
            this.add(choices_1)
            this.add(choices_2)
            this.add(choices_3)
            this.add(choices_4)
            //テキストのないものはリストからはずす。
            if (choices_5 != "") this.add(choices_5)
            if (choices_6 != "") this.add(choices_6)
        }
        println(q_list)

        //q_listのインデックスのshuffl_listを作成。(Int)  ＜ー　シャッフルするため。
        var i_0 = 0
        while (i_0 < q_list.size) {
            shuffl_list.add(i_0)
            i_0++
        }

        //anser_listに正解を保存。
        var i_1 = 0
        while (i_1 < answer_num) {
            answer_list.add(shuffl_list[i_1])
            i_1++
        }

        //shuffl_listをシャッフルしたshuffled_listを作成。
        val shuffled_list = shuffl_list.shuffled()



        //各ボタンに値をセット
        binding.questionText.text = question
        binding.button1.text = q_list[shuffled_list[0]]
        binding.button2.text = q_list[shuffled_list[1]]
        binding.button3.text = q_list[shuffled_list[2]]
        binding.button4.text = q_list[shuffled_list[3]]
        //テキストのない選択肢はボタンごと消す。
        if (shuffled_list.size > 4) binding.button5.text =
            q_list[shuffled_list[4]] else binding.button5.visibility = View.INVISIBLE
        if (shuffled_list.size > 5) binding.button6.text =
            q_list[shuffled_list[5]] else binding.button6.visibility = View.INVISIBLE





        //ボタンを押したときのonClickを発動
        binding.button1.setOnClickListener { onClick(shuffled_list[0], 1) }
        binding.button2.setOnClickListener { onClick(shuffled_list[1], 2) }
        binding.button3.setOnClickListener { onClick(shuffled_list[2], 3) }
        binding.button4.setOnClickListener { onClick(shuffled_list[3], 4) }
        binding.button5.setOnClickListener { onClick(shuffled_list[4], 5) }
        binding.button6.setOnClickListener { onClick(shuffled_list[5], 6) }

    }







    //選択されたら色をかえる。on_listに選択されたもののindexを格納。
    fun onClick (answer: Int, i: Int) {

        //ボタンが選択されていないされている場合
        if (answer in on_list) {
            on_list.remove(answer)
            offColor(i)
        }

        //すでにanswer個に達している場合
        else if (on_list.size  > answer_num - 1) {
            return
        }
        //ボタンが選択されていない場合
        else {
            on_list.add(answer)
            onColor(i)
        }

    }

    //ボタンが解除されたときの色設定
    fun offColor(i: Int) {
        val color = Color.rgb(200, 200, 200)
        when(i) {
            1 -> binding.button1.setBackgroundColor(color)
            2 -> binding.button2.setBackgroundColor(color)
            3 -> binding.button3.setBackgroundColor(color)
            4 -> binding.button4.setBackgroundColor(color)
            5 -> binding.button5.setBackgroundColor(color)
            6 -> binding.button6.setBackgroundColor(color)
        }

    }

    //ボタンが選択されたときの色設定
    fun onColor(i: Int) {
        val color = Color.rgb(0,0, 255)
        when(i) {
            1 -> binding.button1.setBackgroundColor(color)
            2 -> binding.button2.setBackgroundColor(color)
            3 -> binding.button3.setBackgroundColor(color)
            4 -> binding.button4.setBackgroundColor(color)
            5 -> binding.button5.setBackgroundColor(color)
            6 -> binding.button6.setBackgroundColor(color)
        }

    }

    //決定ボタンが押されたときの処理。
    fun colection() {
        //正解かどうか確かめ。
        var i = 0
        var total = 0
        //on_listとanswer_listの照合数.
        while(i < answer_num) {
            if(on_list[i] in answer_list)
                total++
            i++
        }

        //全部正解。
        if(total == answer_num){
            binding.marubatu.text = "⭕"
        }
        //不正解
        else{
            binding.marubatu.text = "❌"
        }

        //1秒開ける処理。
        val handler = Handler(Looper.getMainLooper())
        binding.okButton.setOnClickListener {
            handler.postDelayed( {
                binding.okButton.performClick()
            }, 1000)

        }
        which()
    }

    //次の問題画面へ遷移
//    fun onChange(index: Int, total_score: Int, totaltime: Long ) {
//        println(index)
//        val intent = Intent(this, Quiz::class.java)
//            intent.putExtra("INDEX", index)
//            intent.putExtra("TOTAL_SCORE", total_score)
//            intent.putExtra("TOTAL_TIME", totaltime)
//        startActivity(intent)
//    }

    //１０回ループを超えたとき、結果発表画面へ遷移
    fun ResultChange( score: Int, time:Long) {
        val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("TOTAL_SCORE",total_score.toString())
            intent.putExtra("TOTAL_tIME", total_time.toString())
        startActivity(intent)
    }

    fun which() {
        index += 1
        if(index >= 11){
            ResultChange(total_score, total_time)
        }
        else {
            onChange()
        }
    }

    fun onChange() {

        //クリア操作
        val color = Color.rgb(200, 200, 200)
        binding.run{
            marubatu.text = ""
            button1.setBackgroundColor(color)
            button2.setBackgroundColor(color)
            button3.setBackgroundColor(color)
            button4.setBackgroundColor(color)
            button5.setBackgroundColor(color)
            button6.setBackgroundColor(color)
        }
        answer_list.clear()
        q_list.clear()
        shuffl_list.clear()
        on_list.clear()






        //データベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        //ランダムな数字を生成
        var ram = (1001..1075).random()
        println(ram)


        //ランダムに問題を取得
        val select = """
            SELECT _id, question, answers, choices_1, choices_2, choices_3, choices_4, choices_5, choices_6 FROM Quiz
            WHERE _id = ${ram}
        """.trimIndent()


        //データベースのそれぞれの値を変数に格納。
        var c = db.rawQuery(select, null)
        c.moveToNext()

//        val c_id = c.getColumnIndex("_id")
//        val id = c.getString(c_id)
        val c_question = c.getColumnIndex("question")
        val question = c.getString(c_question)
        val c_answers = c.getColumnIndex("answers")
        answer_num = c.getInt(c_answers)
        val c_choices_1 = c.getColumnIndex("choices_1")
        val choices_1 = c.getString(c_choices_1)
        val c_choices_2 = c.getColumnIndex("choices_2")
        val choices_2 = c.getString(c_choices_2)
        val c_choices_3 = c.getColumnIndex("choices_3")
        val choices_3 = c.getString(c_choices_3)
        val c_choices_4 = c.getColumnIndex("choices_4")
        val choices_4 = c.getString(c_choices_4)
        val c_choices_5 = c.getColumnIndex("choices_5")
        val choices_5 = c.getString(c_choices_5)
        val c_choices_6 = c.getColumnIndex("choices_6")
        val choices_6 = c.getString(c_choices_6)



        //q_listをすべてクリア。
        q_list.clear()

        //選択肢の文字列リスト -> q_list

        q_list.run {
            this.add(choices_1)
            this.add(choices_2)
            this.add(choices_3)
            this.add(choices_4)
            //テキストのないものはリストからはずす。
            if (choices_5 != "") this.add(choices_5)
            if (choices_6 != "") this.add(choices_6)
        }
        println(q_list)

        //q_listのインデックスのshuffl_listを作成。(Int)  ＜ー　シャッフルするため。
        var i_0 = 0
        while (i_0 < q_list.size) {
            shuffl_list.add(i_0)
            i_0++
        }

        //anser_listに正解を保存。
        var i_1 = 0
        while (i_1 < answer_num) {
            answer_list.add(shuffl_list[i_1])
            i_1++
        }

        //shuffl_listをシャッフルしたshuffled_listを作成。
        val shuffled_list = shuffl_list.shuffled()

        //各ボタンに値をセット
        binding.questionText.text = question
        binding.button1.text = q_list[shuffled_list[0]]
        binding.button2.text = q_list[shuffled_list[1]]
        binding.button3.text = q_list[shuffled_list[2]]
        binding.button4.text = q_list[shuffled_list[3]]
        //テキストのない選択肢はボタンごと消す。
        if (shuffled_list.size > 4) binding.button5.text =
            q_list[shuffled_list[4]] else binding.button5.visibility = View.INVISIBLE
        if (shuffled_list.size > 5) binding.button6.text =
            q_list[shuffled_list[5]] else binding.button6.visibility = View.INVISIBLE

        binding.okButton.setOnClickListener{ colection() }


    }







}



