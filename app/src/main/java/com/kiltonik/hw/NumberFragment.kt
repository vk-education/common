package com.kiltonik.hw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.fragment.app.Fragment

class NumberFragment : Fragment() {
    companion object {

        const val NUMBER_KEY = "1"

        const val BIG_TEXT = 60F

        fun newInstance(number: Int): NumberFragment {
            val args = Bundle().apply {
                putInt(NUMBER_KEY, number)
            }

            return NumberFragment().apply { arguments = args }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.number, container, false)

        v.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val number = getNumber()!!

        view.findViewById<TextView>(R.id.big_number).apply {
            text = number.toString()
            textSize = BIG_TEXT
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                setTextColor(
                    view.context.resources.getColor(
                        if (number % 2 == 0) R.color.red else R.color.blue,
                        view.context.theme
                    )
                )
            else
                setTextColor(
                    view.context.resources.getColor(
                        if (number % 2 == 0) R.color.red else R.color.blue
                    )
                )
        }
    }

    private fun getNumber() = arguments?.getInt(NUMBER_KEY)
}
