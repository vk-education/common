package com.kiltonik.hw

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecycleFragment : Fragment() {

    companion object {
        const val VERTICAL_NUMBER = 3
        const val HORIZONTAL_NUMBER = 4

        fun newInstance(): RecycleFragment {
            return RecycleFragment()
        }
    }

    interface FragmentListener {
        fun onNumberClicked(number: Int)
    }

    private var listener: FragmentListener? = null

    private val repository = NumberRepository.instance

    private val recycleClickListener: (Int) -> Unit =
        { number -> listener?.onNumberClicked(number) }

    private val adapter = NumberAdapter(
        repository.list(),
        recycleClickListener
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = requireActivity() as FragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycle_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recycler).apply {
            this.layoutManager = GridLayoutManager(
                this@RecycleFragment.requireActivity().applicationContext,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    VERTICAL_NUMBER
                else HORIZONTAL_NUMBER
            )
            this.adapter = this@RecycleFragment.adapter
        }

        view.findViewById<Button>(R.id.add_item).setOnClickListener {
            repository.newItem()
            adapter.notifyDataSetChanged()
        }
    }
}
