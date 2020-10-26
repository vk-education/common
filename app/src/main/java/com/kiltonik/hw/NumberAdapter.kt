package com.kiltonik.hw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter(
    private val data: MutableList<Int>,
    private val numberClickListener: (Int) -> Unit = {}
) : RecyclerView.Adapter<NumberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder =
        NumberViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.number, parent, false)
        )

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) =
        holder.bind(data[position], numberClickListener)

    override fun getItemCount(): Int = data.size
}

class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val numberText: TextView = itemView.findViewById(R.id.big_number)

    fun bind(number: Int, numberClickListener: (Int) -> Unit) {
        numberText.text = number.toString()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            numberText.setTextColor(
                    itemView.context.resources.getColor(
                            if (number % 2 == 0) R.color.red else R.color.blue,
                            itemView.context.theme
                    )
            )
        else
            numberText.setTextColor(
                    itemView.context.resources.getColor(
                            if (number % 2 == 0) R.color.red else R.color.blue
                    )
            )
        numberText.setOnClickListener { numberClickListener(number) }
    }
}
