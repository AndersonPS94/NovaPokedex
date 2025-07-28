package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hacksprintpokedex.R // Importe R do seu projeto
import com.example.hacksprintpokedex.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val errorMessage = intent.getStringExtra("errorMessage")
        binding.tvErrorMessage.text = errorMessage

        binding.btnBack.setOnClickListener {
            // Simplesmente finalize a ErrorActivity.
            // Isso fará com que a atividade anterior na pilha (sua MainActivity) seja exibida novamente.
            finish()

            // Aplica as animações de transição para o fechamento desta atividade.
            // A animação slide_in_right fará com que a tela ANTERIOR (MainActivity)
            // deslize para a direita ao "entrar" (reaparecer).
            // A animação slide_out_left fará com que a tela ATUAL (ErrorActivity)
            // deslize para a esquerda ao "sair".
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}