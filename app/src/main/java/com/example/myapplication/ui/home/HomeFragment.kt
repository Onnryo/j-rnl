package com.example.myapplication.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.models.Entry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.io.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    val FILENAME: String = "storage.json"
    private var entries: ArrayList<Entry> = ArrayList<Entry>()

    private fun read(context: Context, fileName: String): String? {
        return try {
            val fis: FileInputStream = context.openFileInput(fileName)
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            sb.toString()
        } catch (fileNotFound: FileNotFoundException) {
            null
        } catch (ioException: IOException) {
            null
        }
    }

    private fun create(context: Context, fileName: String, jsonString: String?): Boolean {
        return try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            if (jsonString != null) {
                fos.write(jsonString.toByteArray())
            }
            fos.close()
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }
    }

    private fun isFilePresent(context: Context, fileName: String): Boolean {
        val path: String =
            context.filesDir.absolutePath.toString() + "/" + fileName
        val file = File(path)
        return file.exists()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFilePresent = isFilePresent(activity!!, FILENAME)
        if (isFilePresent) {
            val jsonString = read(activity!!, FILENAME)
            entries = Gson().fromJson(jsonString, Array<Entry>::class.java).toList() as ArrayList<Entry>
        } else {
            val isFileCreated = create(activity!!, FILENAME, "[]")
            if (!isFileCreated) {
                Log.e("HomeFragment.onCreate", "Failed to create json file")
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back: ImageButton = view.findViewById(R.id.entryBack)
        val save: ImageButton = view.findViewById(R.id.entrySave)
        val title: TextInputEditText = view.findViewById(R.id.entryTitle)
        val body: TextInputEditText = view.findViewById(R.id.entryBody)

        fun reload() {
            for (i in 0 until entries.size) {
                val textView = TextView(this.context)
                textView.text = entries[i].toString()
                view.findViewById<LinearLayout>(R.id.homeTimeline).addView(textView)
            }
        }

        fun save() {
            val e: Entry = Entry(title.text.toString(), body.text.toString()) // TODO: add metadata
            entries.add(e)
            val jsonString = Gson().toJson(entries)
            Log.d("HomeFragment.save", jsonString)
            val isFileCreated = create(activity!!, FILENAME, jsonString)
            if (!isFileCreated) {
                Log.e("HomeFragment.onCreate", "Failed to create json file")
            }
            reload()
        }

        fun back() {
            val imm =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(getView()!!.windowToken, 0)
            view.findViewById<ConstraintLayout>(R.id.main).visibility = View.VISIBLE
            view.rootView.findViewById<FloatingActionButton>(R.id.fab).show()
            view.rootView.findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.entry).visibility = View.GONE
            back.visibility = View.VISIBLE
            save.visibility = View.GONE
            title.setText("")
            body.setText("")
        }

        title.setOnFocusChangeListener { _, b ->
            if(b) {
                back.visibility = View.GONE
                save.visibility = View.VISIBLE
            }
        }
        body.setOnFocusChangeListener { _, b ->
            if(b) {
                back.visibility = View.GONE
                save.visibility = View.VISIBLE
            }
        }

        save.setOnClickListener {
            save()
            back()
        }
        back.setOnClickListener {
            back()
        }
        reload()
    }
}