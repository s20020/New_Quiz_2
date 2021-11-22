package jp.ac.it_college.std.s20020.new_quiz_2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, ) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VIRSION
){
    companion object{
        //データベースファイル名の定義
        private const val DATABASE_NAME = "Quiz.db"
        //バージョン情報の定数
        private const val DATABASE_VIRSION = 1
    }

    //初期テーブルをつくる
    override fun onCreate(db: SQLiteDatabase?) {
        //テーブル作成用SQL文字列の作成
        val createTable_main = """
            CREATE TABLE Quiz (
                _id INTEGER PRIMARY KEY,
                question TEXT,
                answer INTEGER,
                choices_1 TEXT,
                choices_2 TEXT,
                choices_3 TEXT,
                choices_4 TEXT,
                choices_5 TEXT,
                choices_6 TEXT
                    );
        """.trimIndent()
        //SQLの実行
        db?.execSQL(createTable_main)




    }

    //DATABASE_VIRSIONが大きくなったときに実行される
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}