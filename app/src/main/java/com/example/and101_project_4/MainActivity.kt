package com.example.and101_project_4

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.whenCreated
import com.example.and101_project_4.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText

private const val INITIAL_PERCENTAGE = 15

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tipBar: SeekBar
    private lateinit var tipPercentageValue: TextView
    private lateinit var billAmountInput: TextInputEditText
    private lateinit var totalTip: TextView
    private lateinit var totalBill: TextView
    private lateinit var review: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tipBar = binding.TipBar
        tipPercentageValue = binding.percentageValue
        billAmountInput = binding.enterAmount
        totalTip = binding.tipAmount
        totalBill = binding.finalBill
        review = binding.tipReview

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tipBar.progress = INITIAL_PERCENTAGE
        tipPercentageValue.text = "$INITIAL_PERCENTAGE%"
        tipDescription(INITIAL_PERCENTAGE)
        tipBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipPercentageValue.text = "$progress%"
                computeTipAndTotal()
                tipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        billAmountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }
        })
    }

    private fun tipDescription(tipPercentage: Int) {
        val description : String
        when (tipPercentage){
            in 0..9 -> description = "Poor"
            in 10..14 -> description = "Good"
            in 15..19 -> description = "Great"
            else -> description = "Amazing"
        }
        review.text = description
        val color = ArgbEvaluator().evaluate(tipPercentage.toFloat()/ tipBar.max,
                        ContextCompat.getColor(this,R.color.worst),
                        ContextCompat.getColor(this,R.color.best)) as Int
        review.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        val baseAmount = billAmountInput.text.toString().toDoubleOrNull() ?: return

        val tipPercent = tipBar.progress
        val tipValue = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipValue

        totalTip.text = "%.2f".format(tipValue)
        totalBill.text = "%.2f".format(totalAmount)
    }
}
