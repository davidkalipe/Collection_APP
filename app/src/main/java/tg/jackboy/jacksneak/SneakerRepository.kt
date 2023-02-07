package tg.jackboy.jacksneak

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import tg.jackboy.jacksneak.SneakerRepository.Singleton.databaseRef
import tg.jackboy.jacksneak.SneakerRepository.Singleton.downloadUri
import tg.jackboy.jacksneak.SneakerRepository.Singleton.sneakerList
import tg.jackboy.jacksneak.SneakerRepository.Singleton.storageReference
import java.net.URI
import java.util.*
import javax.security.auth.callback.Callback

class SneakerRepository {

    object Singleton{
        // donner le lien bucket
        private val BUCKET_URL: String = "gs://jacksneak-ff91b.appspot.com"
        // se connecter a notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        // se connecter a la reference plante
        val databaseRef = FirebaseDatabase.getInstance()
            .getReference("sneaker")

        // creer une liste qui va contenir nos paires
        val sneakerList = arrayListOf<SneakerModel>()

        // contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit){
        // absorber les donnees recuperer dans la databaseRef -> liste de paire
        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes
                sneakerList.clear()
                // recolter la liste
                for (ds in snapshot.children){
                    // construire une paire
                    val sneaker = ds.getValue(SneakerModel::class.java)

                    // verifier que la paire n'est pas null
                    if (sneaker!=null){
                        // ajouter la paire a la liste
                        sneakerList.add(sneaker)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    // creer une fun pour envoyer les fichier sur les storages
    fun uploadImage(file: Uri, callback: () -> Unit){
        // verifier que ce fichier n'est pas null
        if (file!=null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                // verifier s'il y a un blem lors de l'envoi
                if (task.isSuccessful){
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                // verifier si tout a fonctionne
                if (task.isSuccessful){
                    // recuperer l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    // mettre a jour une paire en bd
    fun updateSneaker(sneaker: SneakerModel) = databaseRef.child(sneaker.id).setValue(sneaker)

    // inserer une nouvelle
    fun insertSneaker (sneaker: SneakerModel) =databaseRef.child(sneaker.id).setValue(sneaker)

    // supprimer une paire de la base
    fun deleteSneaker(sneaker: SneakerModel) = databaseRef.child(sneaker.id).removeValue()



}