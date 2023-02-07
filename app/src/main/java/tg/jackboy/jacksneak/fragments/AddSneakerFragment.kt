package tg.jackboy.jacksneak.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import tg.jackboy.jacksneak.MainActivity
import tg.jackboy.jacksneak.R
import tg.jackboy.jacksneak.SneakerModel
import tg.jackboy.jacksneak.SneakerRepository
import tg.jackboy.jacksneak.SneakerRepository.Singleton.downloadUri
import java.security.AccessControlContext
import java.util.*

class AddSneakerFragment(
    private val context: MainActivity
): Fragment() {

    private var file: Uri? = null
    private var uploadedImage:ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_sneaker, container, false)

        // recuperer uploading
        uploadedImage = view.findViewById(R.id.preview_image)

        // recuperer le button charger image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // lorsqu'on clique ca ouvre les images du phones
        pickupImageButton.setOnClickListener{ pickupImage()}

        // recuperer le button confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener{ sendForm(view)}

        return view
    }

    private fun sendForm(view: View) {
        val repo = SneakerRepository()
        repo.uploadImage(file!!){
            val sneakerName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val sneakerDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val commercialisation = view.findViewById<Spinner>(R.id.tendance_spinner).selectedItem.toString()
            val colories = view.findViewById<Spinner>(R.id.colories_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri

            // creer un nouvel object
            val sneaker = SneakerModel(
                UUID.randomUUID().toString(),
                sneakerName,
                sneakerDescription,
                downloadImageUrl.toString(),
                commercialisation,
                colories
            )

            //envoyer en bd
            repo.insertSneaker(sneaker)
        }


    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requesCode: Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if (requesCode == 47 && resultCode == Activity.RESULT_OK){

            // verifier si les donnes sont nulles
            if (data == null|| data.data == null) return

            // recuperer l'image
            file = data.data

            // mttre a jour l'appercu de l'image
            uploadedImage?.setImageURI(file)




    }
}}