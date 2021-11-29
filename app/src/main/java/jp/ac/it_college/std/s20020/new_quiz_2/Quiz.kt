package jp.ac.it_college.std.s20020.new_quiz_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.new_quiz_2.databinding.ActivityQuizBinding

class Quiz : AppCompatActivity() {

    private lateinit var _helper: DatabaseHelper


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
            WHERE _id = 1001
        """.trimIndent()




        var c = db.rawQuery(select, null)
        c.moveToNext()

        val c_id = c.getColumnIndex("_id")
        val id = c.getString(c_id)
        val c_question = c.getColumnIndex("question")
        val question = c.getString(c_question)
        val c_answers = c.getColumnIndex("answers")
        val answers = c.getString(c_answers)
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





//
//        while(c.moveToNext()) {
//            val note = c.let {
//                val index = it.getColumnIndex("note")
//                it.getString(index)
//            }
//        }


    }
}