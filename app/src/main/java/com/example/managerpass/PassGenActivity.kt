package com.example.managerpass

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.managerpass.databinding.ActivityPassGenBinding
import kotlin.random.Random

class PassGenActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPassGenBinding
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityPassGenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toGenerateBtn = findViewById<Button>(R.id.toMainActivity)
        toGenerateBtn.setOnClickListener {
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }

        //initViews
        binding.apply {
            generateButton.setOnClickListener {
                val selectedOptions = mutableListOf<Char>()
                if (checkboxLowercase.isChecked){
                    selectedOptions.addAll(('a'..'z'))
                }
                if (checkboxNumbers.isChecked){
                    selectedOptions.addAll(('0'..'9'))
                }
                if (checkboxUppercase.isChecked){
                    selectedOptions.addAll(('A'..'Z'))
                }
                if (checkboxSymbol.isChecked){
                    selectedOptions.addAll("!@#$%^&*()_-[]{}|:;,.<>?".toList())
                }
                if (selectedOptions.isEmpty())
                {

                    Toast.makeText(this@PassGenActivity , "please select at least one option." , Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val passwordLength = 12
                val randomPassword = buildString {
                    repeat(passwordLength){
                        val randomIndex = Random.nextInt(0,selectedOptions.size)
                        append(selectedOptions[randomIndex])
                    }
                }
                tvPassword.text = randomPassword
            }
            tvPassword.setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("TextViewText",tvPassword.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this@PassGenActivity , "copied." , Toast.LENGTH_SHORT).show()
            }
        }
    }
}