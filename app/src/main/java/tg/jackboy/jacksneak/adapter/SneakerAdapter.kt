package tg.jackboy.jacksneak.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tg.jackboy.jacksneak.*

class SneakerAdapter(
    val context: MainActivity,
    private val sneakerList: List<SneakerModel>,
    private val layoutId: Int
    ): RecyclerView.Adapter<SneakerAdapter.ViewHolder>() {

    // boite pour ranger tous les composants a controler
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val sneakerImage = view.findViewById<ImageView>(R.id.image_item)
        val sneakerName:TextView? = view.findViewById(R.id.name_item)
        val sneakerDescription:TextView? = view.findViewById(R.id.description_item)
        val  starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les infos de la paire
        val currentSneaker = sneakerList[position]

        // recuperer le repo
        val repo = SneakerRepository()

        // utiliser glide pour recuperer l'image à partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentSneaker.imageUrl)).into(holder.sneakerImage)

        // mettre à jour le nom de la plante
        holder.sneakerName?.text = currentSneaker.name

        // mettre a jour la description
        holder.sneakerDescription?.text = currentSneaker.description

        // verifier si la paire a été liker ou non
        if(currentSneaker.liked){
            holder.starIcon.setImageResource(R.drawable.ic_star)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        // rajouter une interaction sur cette etoile
        holder.starIcon.setOnClickListener {
            // inverser si le button est like ou non
            currentSneaker.liked = !currentSneaker.liked
            // mettre a jour l'objet paire
            repo.updateSneaker(currentSneaker)
        }
        // interaction lors du click
        holder.itemView.setOnClickListener{
            // afficher la popup
            SneakerPopup(this, currentSneaker).show()
        }

    }

    override fun getItemCount(): Int = sneakerList.size

}