package com.esmaeel.addminus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esmaeel.addMinusLibrary.QuantityView
import com.esmaeel.addminus.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binder: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.quantityView.setListeners(object : QuantityView.QuantityViewContract {
            override fun onAdd(oldValue: Int, newValue: Int) {
                binder.txt1.text = "add ::-->> old is : $oldValue -- new is : $newValue"
            }

            override fun onMinus(oldValue: Int, newValue: Int) {
                binder.txt1.text = "minus ::-->> old is : $oldValue -- new is : $newValue"
            }

            override fun onReset() {
                binder.txt1.text = "Reset"
            }

        })

        binder.quantityView2.setListeners(object : QuantityView.QuantityViewContract {
            override fun onAdd(oldValue: Int, newValue: Int) {
                binder.txt2.text = "add ::-->> old is : $oldValue -- new is : $newValue"
            }

            override fun onMinus(oldValue: Int, newValue: Int) {
                binder.txt2.text = "minus ::-->> old is : $oldValue -- new is : $newValue"
            }

            override fun onReset() {
                binder.txt2.text = "Reset"
            }

        })

        binder.disableAdd.setOnClickListener {
            quantityView?.addIsDisabled = !(quantityView?.addIsDisabled)!!
        }

        binder.disableMinus.setOnClickListener {
            quantityView?.minusIsDisabled = !(quantityView?.minusIsDisabled)!!
        }

        binder.quantityView.bindSumWith(binder.quantityView2,20)

    }
}