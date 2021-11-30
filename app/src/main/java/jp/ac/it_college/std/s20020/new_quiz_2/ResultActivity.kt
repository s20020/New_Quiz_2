package jp.ac.it_college.std.s20020.new_quiz_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.new_quiz_2.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultScore = intent.getStringExtra("TOTAL_SCORE")
        val resultTime = intent.getStringExtra("TOTAL_TIME")

        binding.resultScore.text = resultScore
        binding.resultTime.text = resultTime


    }
}