package jp.ac.it_college.std.s20020.new_quiz_2


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import jp.ac.it_college.std.s20020.new_quiz_2.databinding.ActivityQuizBinding


class Quiz : AppCompatActivity() {

    private lateinit var _helper: DatabaseHelper
    private var qlist = mutableListOf<String>()
    private var shuffllist = mutableListOf<Int>()
    private var onlist = mutableListOf<Int>()
    private var answernum = 0
    private var answerlist = mutableListOf<Int>()
    private var totaltime = 0L
    private var totalscore = 0
    private var index = 1
    private var start = 0L

    private lateinit var binding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)


        start = System.currentTimeMillis()
        println(start)
        onChange()
    }
    override fun onDestroy() {
        //DBヘルパーオブジェクトの解放
        _helper.close()
        super.onDestroy()
    }

    //タイマーのインスタンスを生成
    private val timer = object : CountDownTimer(10000,
        1000) {
        override fun onTick(millisUntilFinished: Long) {
            val second = millisUntilFinished / 1000L % 60L
            binding.watchTime.text = second.toString()
        }

        //終了したら、次へボタンを明示的にクリック
        override fun onFinish() {
            colection()
        }
    }


    //選択されたら色をかえる。on_listに選択されたもののindexを格納。
    private fun onClick (answer: Int, i: Int) {

        //ボタンが選択されていないされている場合
        if (answer in onlist) {
            onlist.remove(answer)
            offColor(i)
            println(onlist)
        }

        //すでにanswer個に達している場合
        else if (onlist.size  > answernum - 1) {
            return
        }
        //ボタンが選択されていない場合
        else {
            onlist.add(answer)
            onColor(i)
            println(onlist)
        }

    }

    //ボタンが解除されたときの色設定
    private fun offColor(i: Int) {
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
    private fun onColor(i: Int) {
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
    private fun colection() {
        index += 1
        timer.cancel()
        //on_listとanswer_listが等しいか確認。
        var i = 0
        var total = 0
        println(answernum)
        //on_listとanswer_listの照合数.
        while(i < answernum) {
            if(onlist.size < answernum){
                total = 0
            }
            else if(onlist[i] in answerlist)
                total++
            i++
        }

        println(total)
        //正解
        if(total == answernum){
            totalscore+=1
            binding.marubatu.text = "⭕"
        }
        //不正解
        else{
            binding.marubatu.text = "❌"
        }

        //１秒遅らせるためのインスタンス
        val handler = Handler(Looper.getMainLooper())
        //1秒後にwhich()
        handler.postDelayed( {
            which()
        }, 1000)
    }

    //１０回ループを超えたとき、結果発表画面へ遷移
    private fun resultChange() {
        val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("TOTAL_SCORE",totalscore.toString())
            intent.putExtra("TOTAL_TIME", totaltime.toString())
        startActivity(intent)
    }

    private fun which() {
        if(index >= 11){
            val end = System.currentTimeMillis()
            println(start)
            println(end)
            totaltime = (end/1000) - (start/1000)
            resultChange()
        }
        else {
            println(totalscore)
            onChange()
        }
    }

    private fun onChange() {

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
        answerlist.clear()
        qlist.clear()
        shuffllist.clear()
        onlist.clear()
        println(qlist)

        timer.start()

        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(this)


        //データベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        //ランダムな数字を生成
        val ram = (1001..1075).random()
        println(ram)


        //ランダムに問題を取得
        val select = """
            SELECT _id, question, answers, choices_1, choices_2, choices_3, choices_4, choices_5, choices_6 FROM Quiz
            WHERE _id = ${ram}
        """.trimIndent()


        //データベースのそれぞれの値を変数に格納。
        val c = db.rawQuery(select, null)
        c.moveToNext()

//        val c_id = c.getColumnIndex("_id")
//        val id = c.getString(c_id)
        val c1question = c.getColumnIndex("question")
        val question = c.getString(c1question)
        val c1answers = c.getColumnIndex("answers")
        answernum = c.getInt(c1answers)
        val c1choices1 = c.getColumnIndex("choices_1")
        val choices1 = c.getString(c1choices1)
        val c1choices2 = c.getColumnIndex("choices_2")
        val choices2 = c.getString(c1choices2)
        val c1choices3 = c.getColumnIndex("choices_3")
        val choices3 = c.getString(c1choices3)
        val c1choices4 = c.getColumnIndex("choices_4")
        val choices4 = c.getString(c1choices4)
        val c1choices5 = c.getColumnIndex("choices_5")
        val choices5 = c.getString(c1choices5)
        val c1choices6 = c.getColumnIndex("choices_6")
        val choices6 = c.getString(c1choices6)

//        println(answer_num)
//        println(choices_1)
//        println(choices_2)
//        println(choices_3)
//        println(choices_4)
//        println(choices_5)
//        println(choices_6)


        //選択肢の文字列リスト -> q_list
        qlist.run {
            this.add(choices1)
            this.add(choices2)
            this.add(choices3)
            this.add(choices4)
            //テキストのないものはリストからはずす。
            if (choices5 != "") this.add(choices5)
            if (choices6 != "") this.add(choices6)
        }
        println(qlist)

        //q_listのインデックスのshuffl_listを作成。(Int)  ＜ー　シャッフルするため。
        var i_0 = 0
        while (i_0 < qlist.size) {
            shuffllist.add(i_0)
            i_0++
        }
        println(shuffllist)


        //anser_listに正解を保存。
        var i_1 = 0
        while (i_1 < answernum) {
            answerlist.add(shuffllist[i_1])
            i_1++
        }
        println(answerlist)

        //shuffl_listをシャッフルしたshuffled_listを作成。
        val shuffled_list = shuffllist.shuffled()
        println(shuffled_list)

        //各ボタンに値をセット
        binding.questionText.text = question
        binding.button1.text = qlist[shuffled_list[0]]
        binding.button2.text = qlist[shuffled_list[1]]
        binding.button3.text = qlist[shuffled_list[2]]
        binding.button4.text = qlist[shuffled_list[3]]
        //テキストのない選択肢はボタンごと消す。
        if (shuffled_list.size > 4) binding.button5.text =
            qlist[shuffled_list[4]] else binding.button5.visibility = View.INVISIBLE
        if (shuffled_list.size > 5) binding.button6.text =
            qlist[shuffled_list[5]] else binding.button6.visibility = View.INVISIBLE

        //ボタンを押したときのonClickを発動
        binding.button1.setOnClickListener { onClick(shuffled_list[0], 1) }
        binding.button2.setOnClickListener { onClick(shuffled_list[1], 2) }
        binding.button3.setOnClickListener { onClick(shuffled_list[2], 3) }
        binding.button4.setOnClickListener { onClick(shuffled_list[3], 4) }
        binding.button5.setOnClickListener { onClick(shuffled_list[4], 5) }
        binding.button6.setOnClickListener { onClick(shuffled_list[5], 6) }


        binding.okButton.setOnClickListener{ colection() }

    }
}



