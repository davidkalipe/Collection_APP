package tg.jackboy.jacksneak

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import tg.jackboy.jacksneak.adapter.SneakerAdapter

class SneakerPopup(
    private val adapter: SneakerAdapter,
    private val currentSneaker: SneakerModel
    ): Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }
    private fun updateStar(button: ImageView){
        if (currentSneaker.liked){
            button.setImageResource(R.drawable.ic_star)
        }
        else{
            button.setImageResource(R.drawable.ic_unstar)

        }
    }

    private fun setupStarButton() {
        // recuperer
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)

        // interaction
        starButton.setOnClickListener {
            currentSneaker.liked = !currentSneaker.liked
            val repo = SneakerRepository()
            repo.updateSneaker(currentSneaker)
            updateStar(starButton)

            if (currentSneaker.liked){
                starButton.setImageResource(R.drawable.ic_star)
            }
            else{
                starButton.setImageResource(R.drawable.ic_unstar)

            }
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            // supprimer la plante de la bd
            val repo = SneakerRepository()
            repo.deleteSneaker(currentSneaker)
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            // fermer la fenetre popup
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser l'image de la paire
        val sneakerImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentSneaker.imageUrl)).into(sneakerImage)

        // actualiser le nom de la paire
        findViewById<TextView>(R.id.popup_sneaker_name).text = currentSneaker.name

        // actualiser la description
        findViewById<TextView>(R.id.popup_sneaker_description_subtitle).text = currentSneaker.description

        // actualiser la commercialisation de la paire
        findViewById<TextView>(R.id.popup_sneaker_commercialisation_subtitle).text = currentSneaker.commercialisation

        // actualiser l'exisatance de colories
        findViewById<TextView>(R.id.popup_sneaker_colorie_subtitle).text = currentSneaker.Existance_de_colories
    }

}