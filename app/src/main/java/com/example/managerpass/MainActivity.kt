package com.example.managerpass

import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.managerpass.adapter.ItemRVVBListAdapter
import com.example.managerpass.databinding.ActivityMainBinding
import com.example.managerpass.models.Item
import com.example.managerpass.utils.Status
import com.example.managerpass.utils.clearEditText
import com.example.managerpass.utils.longToastShow
import com.example.managerpass.utils.setupDialog
import com.example.managerpass.utils.validateEditEmail
import com.example.managerpass.utils.validateEditPassword
import com.example.managerpass.utils.validateEditText
import com.example.managerpass.viewmodels.ItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0

    var key : String="mysecretkey12345"
    var secretKeySpec = SecretKeySpec(key.toByteArray(),"AES")


    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addItemDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_item_dialog)
        }
    }

    private val updateItemDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_item_dialog)
        }
    }

    private val loadingDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        val Intent = Intent(this, PassGenActivity::class.java)


        fun openDatabase(){
            itemViewModel.openDatabase()
        }
        openDatabase()

        val toGenerateBtn = findViewById<Button>(R.id.toGenerateBtn)
        toGenerateBtn.setOnClickListener {
            startActivity(Intent)
        }


        // Add item start
        val addCloseImg = addItemDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener { addItemDialog.dismiss() }


        val addETTitle = addItemDialog.findViewById<TextInputEditText>(R.id.edItemTitle)
        val addETTitleL = addItemDialog.findViewById<TextInputLayout>(R.id.edItemTitleL)

        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })

        val addETEmail = addItemDialog.findViewById<TextInputEditText>(R.id.edItemEmail)
        val addETEmailL = addItemDialog.findViewById<TextInputLayout>(R.id.edItemEmailL)

        addETEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditEmail(addETEmail, addETEmailL)
            }

        })

        val addETPassword = addItemDialog.findViewById<TextInputEditText>(R.id.edItemPassword)
        val addETPasswordL = addItemDialog.findViewById<TextInputLayout>(R.id.edItemPasswordL)

        addETPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditPassword(addETPassword, addETPasswordL)
            }

        })

        val addETDescr = addItemDialog.findViewById<TextInputEditText>(R.id.edItemDescr)
        val addETDescrL = addItemDialog.findViewById<TextInputLayout>(R.id.edItemDescrL)
        addETDescr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDescr, addETDescrL)
            }

        })


        mainBinding.addItemFABtn.setOnClickListener {
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETEmail, addETEmailL)
            clearEditText(addETPassword, addETPasswordL)
            clearEditText(addETDescr, addETDescrL)
            addItemDialog.show()
        }

        val saveItemBtn = addItemDialog.findViewById<Button>(R.id.saveItemBtn)
        saveItemBtn.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL)
                && validateEditEmail(addETEmail, addETEmailL)
                && validateEditPassword(addETPassword, addETPasswordL)
                && validateEditText(addETDescr, addETDescrL)
            ) {
                addItemDialog.dismiss()
                val cipheredPassword = encrypt(addETPassword.text.toString())
                val newItem = Item(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETEmail.text.toString().trim(),
                    cipheredPassword.trim(),
                    addETDescr.text.toString().trim(),
                )
                itemViewModel.insertItem(newItem).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                longToastShow("Task Added Successfully")
                            }

                        }

                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                    }

                }
            }
        }  // Add item end


            // Update item start
            val updateETTitle = updateItemDialog.findViewById<TextInputEditText>(R.id.edItemTitle)
            val updateETTitleL = updateItemDialog.findViewById<TextInputLayout>(R.id.edItemTitleL)

            addETTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    validateEditText(updateETTitle, updateETTitleL)
                }

            })

            val updateETEmail = updateItemDialog.findViewById<TextInputEditText>(R.id.edItemEmail)
            val updateETEmailL = updateItemDialog.findViewById<TextInputLayout>(R.id.edItemEmailL)

            addETEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    validateEditEmail(updateETEmail, updateETEmailL)
                }

            })

            val updateETPassword =
                updateItemDialog.findViewById<TextInputEditText>(R.id.edItemPassword)
            val updateETPasswordL =
                updateItemDialog.findViewById<TextInputLayout>(R.id.edItemPasswordL)

            addETPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    validateEditPassword(updateETPassword, updateETPasswordL)
                }

            })

            val updateETDescr = updateItemDialog.findViewById<TextInputEditText>(R.id.edItemDescr)
            val updateETDescrL = updateItemDialog.findViewById<TextInputLayout>(R.id.edItemDescrL)

            addETDescr.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    validateEditText(updateETDescr, updateETDescrL)
                }

            })

            val updateCloseImg = updateItemDialog.findViewById<ImageView>(R.id.closeImg)
            updateCloseImg.setOnClickListener { updateItemDialog.dismiss() }

            val updateItemBtn = updateItemDialog.findViewById<Button>(R.id.updateItemBtn)

            // Update item end


            val itemRVVBListAdapter = ItemRVVBListAdapter { type, position, item ->
                if (type == "delete") {
                    itemViewModel
                        .deleteItemUsingId(item.id)
                } else if (type == "update") {
                    updateETTitle.setText(item.title)
                    updateETEmail.setText(item.email)
                    updateETPassword.setText(decrypt(item.password))
                    updateETDescr.setText(item.description)
                    updateItemBtn.setOnClickListener {
                        if (validateEditText(updateETTitle, updateETTitleL)
                            && validateEditEmail(updateETEmail, updateETEmailL)
                            && validateEditPassword(updateETPassword, updateETPasswordL)
                            && validateEditText(updateETDescr, updateETDescrL)
                        ) {
                            updateItemDialog.dismiss()
                            val cipheredPassword = encrypt(updateETPassword.text.toString())
                            itemViewModel
                                //.updateItem(updateItem)
                                .updateItemParticularField(
                                    item.id,
                                    updateETTitle.text.toString().trim(),
                                    updateETEmail.text.toString().trim(),
                                    cipheredPassword.trim(),
                                    updateETDescr.text.toString().trim()
                                )
                                .observe(this) {
                                    when (it.status) {
                                        Status.LOADING -> {
                                            loadingDialog.show()
                                        }

                                        Status.SUCCESS -> {
                                            loadingDialog.dismiss()
                                            if (it.data != -1) {
                                                longToastShow("Task Updated Successfully")
                                            }
                                        }

                                        Status.ERROR -> {
                                            loadingDialog.dismiss()
                                            it.message?.let { it1 -> longToastShow(it1) }
                                        }
                                    }
                                }

                        }
                    }
                    updateItemDialog.show()
                }
            }
            mainBinding.vaultRv.adapter = itemRVVBListAdapter
            itemRVVBListAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    mainBinding.vaultRv.smoothScrollToPosition(positionStart)
                }
            })
            callGetItemList(itemRVVBListAdapter)



    }


    private fun callGetItemList(itemRecyclerViewAdapter: ItemRVVBListAdapter) {
        loadingDialog.show()
        CoroutineScope(Dispatchers.Main).launch {
            itemViewModel.getItemList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect { itemList ->
                            loadingDialog.dismiss()
                            itemRecyclerViewAdapter.submitList(itemList)
                        }
                    }

                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                }

            }
        }
    }

    private fun encrypt(string: String) : String{
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") //Specifying which mode of AES is to be used
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec)// Specifying the mode wither encrypt or decrypt
        var encryptBytes =cipher.doFinal(string.toByteArray(Charsets.UTF_8))//Converting the string that will be encrypted to byte array
        return Base64.encodeToString(encryptBytes,Base64.DEFAULT) // returning the encrypted string
    }

    private fun decrypt(string : String) : String{
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec)
        var decryptedBytes = cipher.doFinal(Base64.decode(string, Base64.DEFAULT)) // decoding the entered string
        return String(decryptedBytes,Charsets.UTF_8) // returning the decrypted string
    }


    }

