package com.example.calcex

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.calcex.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_calc.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity()
{
    private var error: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    fun input(view: View)
    {
        if (error)
        {
            clear(view)
        }

        findViewById<TextView>(R.id.exprDisplay).append((view as Button).text)
    }

    fun clear(view: View)
    {
        findViewById<TextView>(R.id.exprDisplay).text = ""
        error = false
    }

    fun compute(view: View)
    {
        if (error)
        {
            return
        }

        val txt = findViewById<TextView>(R.id.exprDisplay).text.toString()

        try {
            val expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                findViewById<TextView>(R.id.exprDisplay).text = result.toString()
            } catch (ex: ArithmeticException) {
                error = true
                findViewById<TextView>(R.id.exprDisplay).text = "Error!"
            }
        } catch (ex: Exception) {
            error = true
            findViewById<TextView>(R.id.exprDisplay).text = "Error!"
        }
    }

    fun calculateBMI(v: View?)
    {
        val heightStr = findViewById<EditText>(R.id.height).text.toString()
        val weightStr = findViewById<EditText>(R.id.weight).text.toString()

        if (heightStr.isNotEmpty() && weightStr.isNotEmpty())
        {
            val height = heightStr.toFloat() / 100.0f
            val weight = weightStr.toFloat()
            val bmi = weight / (height * height)

            displayBMI(bmi)
        }
    }

    private fun displayBMI(bmi: Float)
    {
        // https://www.euro.who.int/en/health-topics/disease-prevention/nutrition/a-healthy-lifestyle/body-mass-index-bmi

        var bmiLabel =
            if (bmi.compareTo(18.5f) <= 0)
            {
                "${getString(R.string.underweight)} (Eat more)"
            }
            else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0)
            {
                getString(R.string.normal)
            }
            else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0)
            {
                "${getString(R.string.overweight)} (Try to eat less)"
            }
            else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0)
            {
                "${getString(R.string.obese_class_i)} (Eat less)"
            }
            else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0)
            {
                "${getString(R.string.obese_class_ii)} (Eat less!)"
            }
            else
            {
                "${getString(R.string.obese_class_iii)} (Eat way less!)"
            }

        bmiLabel = "${bmi.roundToInt()}\n$bmiLabel"

        findViewById<TextView>(R.id.result).text = bmiLabel
    }

    fun computeHBe(v: View?)
    {
        // https://en.wikipedia.org/wiki/Harris%E2%80%93Benedict_equation

        val heightStr = findViewById<EditText>(R.id.height).text.toString()
        val weightStr = findViewById<EditText>(R.id.weight).text.toString()
        val ageStr = findViewById<EditText>(R.id.age).text.toString()
        val isWoman = findViewById<Switch>(R.id.isWoman).isChecked

        if (heightStr.isNotEmpty() && weightStr.isNotEmpty() && ageStr.isNotEmpty())
        {
            val height = heightStr.toFloat()
            val weight = weightStr.toFloat()
            val age = ageStr.toInt()
            val hbe =
                if (isWoman)
                {
                    (655.1f + (1.85 * height) + (9.567f * weight) - (4.68f * age)).toFloat()
                }
                else
                {
                    66.47f + (5.0f * height) + (13.7f * weight) - (6.76f * age)
                }

            displayHBe(hbe)
        }
    }

    private fun displayHBe(hbe: Float)
    {
        val hbeLabel = "$hbe kcal"

        findViewById<TextView>(R.id.result).text = hbeLabel
    }
}
