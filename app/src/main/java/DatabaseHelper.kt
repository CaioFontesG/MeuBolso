import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "financeiro.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "transacoes"
        const val COL_ID = "id"
        const val COL_DESCRICAO = "descricao"
        const val COL_VALOR = "valor"
        const val COL_DATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_DESCRICAO TEXT, " +
                "$COL_VALOR REAL, " +
                "$COL_DATA TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTransacao(descricao: String, valor: Double, data: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_DESCRICAO, descricao)
            put(COL_VALOR, valor)
            put(COL_DATA, data)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getTransacoes(): ArrayList<HashMap<String, String>> {
        val transacoesList = ArrayList<HashMap<String, String>>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                map["descricao"] = cursor.getString(cursor.getColumnIndex(COL_DESCRICAO))
                map["valor"] = cursor.getDouble(cursor.getColumnIndex(COL_VALOR)).toString()
                transacoesList.add(map)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return transacoesList
    }
}
