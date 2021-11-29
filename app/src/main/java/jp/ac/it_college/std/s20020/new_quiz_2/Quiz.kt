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
    private var off_list = mutableListOf<Int>()
    private var on_list = mutableListOf<Int>()

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


        val select = """
            SELECT _id, question, answers, choices_1, choices_2, choices_3, choices_4, choices_5, choices_6 FROM Quiz
            WHERE _id = ${ram}
        """.trimIndent()

//        var stmt = db.compileStatement(select)
//
//        stmt.bindLong(1, ram.toLong())


        var c = db.rawQuery(select, null)
        c.moveToNext()

        val c_id = c.getColumnIndex("_id")
        val id = c.getString(c_id)
        val c_question = c.getColumnIndex("question")
        val question = c.getString(c_question)
        val c_answers = c.getColumnIndex("answers")
        val answers = c.getInt(c_answers)
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
        println(answers)
        println(choices_1)
        println(choices_2)
        println(choices_3)
        println(choices_4)
        println(choices_5)
        println(choices_6)


        binding.questionText.text = question
        binding.button1.text = choices_1
        binding.button2.text = choices_2
        binding.button3.text = choices_3
        binding.button4.text = choices_4
        binding.button5.text = choices_5
        binding.button6.text = choices_6

        //選択肢にテキストが入っていないものは削除する
        if(choices_5 == "") binding.button5.visibility = View.INVISIBLE
        if(choices_6 == "") binding.button6.visibility = View.INVISIBLE





        //ボタンを押したときの処理
        binding.button1.setOnClickListener { onClick(1)}
        binding.button2.setOnClickListener { onClick(2)}
        binding.button3.setOnClickListener { onClick(3)}
        binding.button4.setOnClickListener { onClick(4)}
        binding.button5.setOnClickListener { onClick(5)}
        binding.button6.setOnClickListener { onClick(6)}




    }



    fun onClick (i:Int) {

        if (i in on_list) {
            on_list.remove(i)
            onColor(i)
            println(on_list)
        }else {
            on_list.add(i)
            offColor(i)
            println(on_list)
        }

    }

    //ボタンが選択されたときの処理
    fun onColor(i: Int) {
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


    //ボタンが解除されたときの処理
    fun offColor(i: Int) {
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


}



