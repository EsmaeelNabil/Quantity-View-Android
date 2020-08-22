package com.esmaeel.addMinusLibrary

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.esmaeel.addMinusLibrary.databinding.QuantityViewMainBinding

fun Int.doIfProvided(gf: (value: Int) -> Unit) {
    this?.let {
        if (this != 0) {
            gf(this)
        }
    }
}

fun Float.doIfProvided(gf: (value: Float) -> Unit) {
    this?.let {
        if (this != 0f) {
            gf(this)
        }
    }
}

class QuantityView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var contract: QuantityViewContract? = null
    private var binder: QuantityViewMainBinding? = null

    private var numberColor = Color.parseColor("#000000")

    var MIN = 1
        set(value) {
            field = value
        }

    var MAX = 20
        set(value) {
            field = value
        }

    /**
     * updates the current Value for the quantity
     * equals == Min by default
     */
    var currentValue = MIN
        set(value) {
            field = value
            updateNumber()
        }


    /**
     * enable or disable adding
     */
    var addIsDisabled = false
        set(value) {
            field = value
        }

    /**
     * enable or disable removing/Minus
     */
    var minusIsDisabled = false
        set(value) {
            field = value
        }


    init {
        binder = QuantityViewMainBinding.inflate(LayoutInflater.from(context), this, true)
        updateStyle()
        post {
            initClicks()
        }
    }

    private fun updateStyle() {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuantityView)

        typedArray.getColor(R.styleable.QuantityView_numberColor, 0).doIfProvided {
            binder?.number?.setTextColor(it)
            numberColor = it
        }

        typedArray.getResourceId(R.styleable.QuantityView_addDrawable, 0).doIfProvided {
            binder?.addButton?.setImageResource(it)
        }

        typedArray.getResourceId(R.styleable.QuantityView_minusDrawable, 0).doIfProvided {
            binder?.minusButton?.setImageResource(it)
        }

        typedArray.getInt(R.styleable.QuantityView_minQuantity, MIN).doIfProvided {
            MIN = it
        }

        typedArray.getInt(R.styleable.QuantityView_maxQuantity, MAX).doIfProvided {
            MAX = it
        }

        typedArray.getInt(R.styleable.QuantityView_currentQuantity, MIN).doIfProvided {
            currentValue = it
            updateNumber()
        }

        typedArray.getDimension(R.styleable.QuantityView_numberTextSize, 0f).doIfProvided {
            binder?.number?.textSize = it
        }

        typedArray.recycle()
    }


    private fun initClicks() {
        binder?.addButton?.setOnClickListener {
            addNumber()
        }
        binder?.minusButton?.setOnClickListener { minusNumber() }

        /*binder?.number?.setOnLongClickListener {
            reset()
            return@setOnLongClickListener true
        }*/

        // TODO: 7/28/20 make this after typedvalues
        updateNumber()
    }

    fun addNumber() {
        if (currentValue < MAX && !addIsDisabled) {
            // TODO: 8/22/20 enable it

            if (isLinked) {
                if (linkedQuantity!!.currentValue + (currentValue + 1) <= linkedQuantitySum) {
                    contract?.onAdd(currentValue, currentValue + 1)
                    currentValue++
                    updateNumber()
                }
            } else {
                contract?.onAdd(currentValue, currentValue + 1)
                currentValue++
                updateNumber()
            }
        } else {
            // TODO: 8/22/20 Disable it
        }
    }


    fun minusNumber() {
        if (currentValue > MIN && !minusIsDisabled) {
            contract?.onMinus(currentValue, currentValue - 1)
            currentValue--
            updateNumber()
        }
    }

    private fun updateNumber() {
        binder?.number?.text = currentValue.toString()
    }

    /**
     * reset view
     */
    private fun reset() {
        currentValue = MIN
        binder?.number?.text = currentValue.toString()
        binder?.minusButton?.alpha = 1f
        binder?.addButton?.alpha = 1f
        binder?.minusButton?.isEnabled = true
        binder?.addButton?.isEnabled = true
        contract?.onReset()
    }

    fun setListeners(contract: QuantityViewContract) {
        this.contract = contract
    }

    private var linkedQuantity: QuantityView? = null
    private var linkedQuantitySum = 8
    private var isLinked = false

    /**
     *
     * @param linkedQuantity QuantityView another Quantity view that we want to link it to us
     * @param sum Int the sum of both views currentValues should be
     * both views can't get higher than the sum of the selected value
     * example : view1 = 4 , view2 = 3 and the selected sum is : 8 -> then only one of them can get one higher!
     */
    fun bindSumWith(linkedQuantity: QuantityView, sum: Int = 8) {
        this.linkedQuantity = linkedQuantity
        this.linkedQuantitySum = sum
        isLinked = true
    }


    interface QuantityViewContract {
        fun onAdd(oldValue: Int, newValue: Int)
        fun onMinus(oldValue: Int, newValue: Int)
        fun onReset()
    }
}