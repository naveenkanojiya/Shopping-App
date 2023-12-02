package com.example.shoppingappadmin

import android.app.Activity
import android.app.Instrumentation
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shoppingappadmin.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var productModel = ProductModel()
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                binding.rootLayout.setBackgroundColor(Color.LTGRAY)
                binding.mainlayout.visibility = View.GONE
                binding.spinKit.visibility = View.VISIBLE

                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                Firebase.storage.reference.child("ProductImg/${UUID.randomUUID()}").putFile(fileUri)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            it.result.storage.downloadUrl.addOnSuccessListener {
                                productModel.imageUrl = it.toString()
                                binding.mainlayout.visibility = View.VISIBLE
                                binding.spinKit.visibility = View.GONE
                                binding.productImage.setImageURI(fileUri)
                                binding.rootLayout.setBackgroundColor(Color.WHITE)
                            }

                        }
                    }


            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.productImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.addProduct.setOnClickListener {
            if (binding.productName.text.toString().isEmpty()) {
                binding.productName.setError("Bhai Name to Dal Le😒😒")
            } else if (binding.productPrice.text.toString().isEmpty()) {
                binding.productPrice.setError("Bhai Price to Dal Le😒😒")
            } else if (binding.disp.text.toString().isEmpty()) {
                binding.disp.setError("Bhai Description to Dal Le😒😒")

            } else if (productModel.imageUrl.isEmpty()) {
                Toast.makeText(this@MainActivity, "bhai Image to Dal", Toast.LENGTH_SHORT).show()
            } else if (binding.disp.text.toString().trim().length < 20) {
                binding.disp.setError("Bhai Description 80 char se kam hai 🤔")
            } else {
                productModel.name = binding.productName.text.toString()
                productModel.disp = binding.disp.text.toString()
                productModel.price = binding.productPrice.text.toString().toDouble()


                Firebase.firestore.collection("Product").document(UUID.randomUUID().toString())
                    .set(productModel).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this@MainActivity, "Product is  Added 👍👍", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@MainActivity, "Product is not  Added 😕😕", Toast.LENGTH_SHORT).show()
                        }
                    }


            }
        }
    }
}







