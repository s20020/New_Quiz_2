package jp.ac.it_college.std.s20020.new_quiz_2

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import jp.ac.it_college.std.s20020.new_quiz_2.databinding.ActivityQuizBinding

class Quiz : AppCompatActivity() {

    private lateinit var _helper: DatabaseHelper
    private var q_list = mutableListOf<String>()
    private var shuffl_list = mutableListOf<Int>()
    private var on_list = mutableListOf<Int>()
    private var answer_num = 0
    private var answer_list = mutableListOf<Int>()
    private var shuffled_list = mutableListOf<Int>()
    private var total_time : Long = 0
    private var total_score = 0

    private lateinit var binding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            WHERE _id = 1041
        """.trimIndent()

//        var stmt = db.compileStatement(select)
//
//        stmt.bindLong(1, ram.toLong())


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


        println(id)
        println(question)
        println(answer_num)
        println(choices_1)
        println(choices_2)
        println(choices_3)
        println(choices_4)
        println(choices_5)
        println(choices_6)



        //選択するリスト(String)
        q_list.run{
            this.add(choices_1)
            this.add(choices_2)
            this.add(choices_3)
            this.add(choices_4)
            //テキストのないものはリストからはずす。
            if(choices_5 != "") this.add(choices_5)
            if(choices_6 != "") this.add(choices_6)
        }

        println(q_list)

        //q_listのインデックスのリストを作成。(Int)  ＜ー　シャッフルするため。
        var i_0 = 0
        while(i_0 < q_list.size){
            shuffl_list.add(i_0)
            i_0++
        }
        println(shuffl_list)

        //正解のインデックスを保管するリスト.(Int)
        var i_1 = 0
        while(i_1 < answer_num) {
            answer_list.add(shuffl_list[i_1])
            i_1++
        }
        println(answer_list)




        //選択肢をシャッフル
        val shuffled_list = shuffl_list.shuffled()
        println(shuffled_list.size)
        println(shuffled_list)


        //各ボタンに値をセット
        binding.questionText.text = question
        binding.button1.text = q_list[shuffled_list[0]]
        binding.button2.text = q_list[shuffled_list[1]]
        binding.button3.text = q_list[shuffled_list[2]]
        binding.button4.text = q_list[shuffled_list[3]]
        //テキストのない選択肢はボタンごと消す。
        if(shuffled_list.size > 4) binding.button5.text = q_list[shuffled_list[4]] else binding.button5.visibility = View.INVISIBLE
        if(shuffled_list.size > 5) binding.button6.text = q_list[shuffled_list[5]] else binding.button6.visibility = View.INVISIBLE

        //選択肢にテキストが入っていないものは削除する





        //ボタンが押されたときの処理
        binding.okButton.setOnClickListener {
            colection()

        }

        //ボタンを押したときのonClickを発動
        binding.button1.setOnClickListener { onClick(shuffled_list[0], 1)}
        binding.button2.setOnClickListener { onClick(shuffled_list[1], 2)}
        binding.button3.setOnClickListener { onClick(shuffled_list[2],3)}
        binding.button4.setOnClickListener { onClick(shuffled_list[3],4)}
        binding.button5.setOnClickListener { onClick(shuffled_list[4],5)}
        binding.button6.setOnClickListener { onClick(shuffled_list[5],6)}




    }



    //それぞれのボタンのリスト設定
    //選択リストの場所、ボタンの位置を受け取る。
    fun onClick (answer: Int, i: Int) {

        //ボタンが選択されていないされている場合
        if (answer in on_list) {
            on_list.remove(answer)

            offColor(i)
            println(on_list)
            println(on_list.size)

        }

        //すでにanswer個に達している場合
        else if (on_list.size  > answer_num - 1) {
            println(on_list.size)
            println(answer_num)
            return

        }
        //ボタンが選択されていない場合
        else {

            on_list.add(answer)
            onColor(i)
            println(on_list)
            println(on_list.size)
        }

    }

    //ボタンが選択されたときの色設定
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


    //ボタンが解除されたときの色設定
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
        var i = 0
        var total = 0
        while(i < answer_num) {
            if(on_list[i] in answer_list)
                total++
            i++
        }
        println(total)
        println(answer_num)
        if(total == answer_num)
            binding.marubatu.text = "⭕"
        else
            binding.marubatu.text = "❌"

    }



}



