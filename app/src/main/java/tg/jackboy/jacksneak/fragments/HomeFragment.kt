package tg.jackboy.jacksneak.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tg.jackboy.jacksneak.MainActivity
import tg.jackboy.jacksneak.R
import tg.jackboy.jacksneak.SneakerModel
import tg.jackboy.jacksneak.SneakerRepository.Singleton.sneakerList
import tg.jackboy.jacksneak.adapter.SneakerAdapter
import tg.jackboy.jacksneak.adapter.SneakerItemDecoration

class HomeFragment(
    private val context: MainActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)


        // recuperer le recyclerview
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = SneakerAdapter(context,sneakerList.filter{!it.liked},R.layout.item_horizontal_sneaker)

        // recuperer le second recyclerview
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = SneakerAdapter(context,sneakerList,R.layout.item_vertical_sneaker)
        verticalRecyclerView.addItemDecoration(SneakerItemDecoration())

        return view

    }
}