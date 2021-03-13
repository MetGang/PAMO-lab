package com.example.calc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class MainActivity : AppCompatActivity()
{
    private lateinit var exprDisplay: TextView

    private var error: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exprDisplay = findViewById(R.id.exprDisplay)
    }

    fun onInput(view: View)
    {
        if (error)
        {
            clear(view)
        }

        exprDisplay.append((view as Button).text)
    }

    fun clear(view: View)
    {
        exprDisplay.text = ""
        error = false
    }

    fun compute(view: View)
    {
        if (error)
        {
            return
        }

        val txt = exprDisplay.text.toString()

        try {
            val expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                exprDisplay.text = result.toString()
            } catch (ex: ArithmeticException) {
                error = true
                exprDisplay.text = "Error!"
            }
        } catch (ex: Exception) {
            error = true
            exprDisplay.text = "Error!"
        }
    }
}
