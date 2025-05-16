package com.example.josemerchanejemplo1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity4 : AppCompatActivity() {

    private lateinit var buttonInfoButton: Button
    private lateinit var buttonAdoptButton: Button
    private lateinit var buttonBackButton: Button
    private lateinit var textViewMainTittle: TextView
    private lateinit var imageViewCapibara: ImageView
    private lateinit var textViewCapibaraInfo : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUI()
        buttonOnClick()
    }

    private fun initUI() {
        textViewCapibaraInfo = findViewById(R.id.textViewCapibaraInfo)
        textViewMainTittle = findViewById(R.id.textViewMainTittle)
        buttonInfoButton = findViewById(R.id.buttonInfoButton)
        buttonAdoptButton = findViewById(R.id.buttonAdoptButton)
        buttonBackButton = findViewById(R.id.buttonBackButton)
        imageViewCapibara = findViewById(R.id.imageViewCapibara)
    }

    fun buttonOnClick() {
        buttonInfoButton.setOnClickListener {
            Toast.makeText(this, R.string.buttonInfoMessage , Toast.LENGTH_SHORT).show()
            textViewCapibaraInfo.visibility = TextView.VISIBLE
        }
        buttonAdoptButton.setOnClickListener {
            Toast.makeText(this, R.string.buttonAdoptMessage , Toast.LENGTH_SHORT).show()
            textViewMainTittle.text = getString(R.string.capibaraHappyTittle)
            textViewCapibaraInfo.text = "Ya está feliz :D"
            imageViewCapibara.setImageResource(R.drawable.capibara_feliz)
        }
        buttonBackButton.setOnClickListener {
            Toast.makeText(this, R.string.buttonBackMessage , Toast.LENGTH_SHORT).show()
            textViewMainTittle.text = getString(R.string.capibaraTittle)
            textViewCapibaraInfo.text = "Está triste :("
            imageViewCapibara.setImageResource(R.drawable.capibara_triste)
        }
    }

}