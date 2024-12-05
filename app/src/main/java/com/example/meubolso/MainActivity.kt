package com.example.meubolso

import DatabaseHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.ComponentActivity
import com.example.meubolso.ui.theme.MeuBolsoTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etDescricao: EditText
    private lateinit var etValor: EditText
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        etDescricao = findViewById(R.id.etDescricao)
        etValor = findViewById(R.id.etValor)
        listView = findViewById(R.id.listView)

        val btnAdicionar: Button = findViewById(R.id.btnAdicionar)
        btnAdicionar.setOnClickListener { adicionarTransacao() }

        listarTransacoes()
    }

    private fun adicionarTransacao() {
        val descricao = etDescricao.text.toString()
        val valor = etValor.text.toString().toDoubleOrNull()
        val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (descricao.isNotEmpty() && valor != null) {
            dbHelper.addTransacao(descricao, valor, data)
            etDescricao.text.clear()
            etValor.text.clear()
            listarTransacoes()
        }
    }

    private fun listarTransacoes() {
        val transacoes = dbHelper.getTransacoes()
        val adapter = SimpleAdapter(
            this,
            transacoes,
            android.R.layout.simple_list_item_2,
            arrayOf("descricao", "valor"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        listView.adapter = adapter
    }
}
