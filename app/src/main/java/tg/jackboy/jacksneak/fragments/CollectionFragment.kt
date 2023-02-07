package tg.jackboy.jacksneak.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tg.jackboy.jacksneak.MainActivity
import tg.jackboy.jacksneak.R
import tg.jackboy.jacksneak.SneakerRepository.Singleton.sneakerList
import tg.jackboy.jacksneak.adapter.SneakerAdapter
import tg.jackboy.jacksneak.adapter.SneakerItemDecoration

class CollectionFragment(
    private val context: MainActivity
): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        // recuperer ma recyclerView
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView.adapter = SneakerAdapter(context, sneakerList.filter{ it.liked}, R.layout.item_vertical_sneaker)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(SneakerItemDecoration())

        return view
    }
}