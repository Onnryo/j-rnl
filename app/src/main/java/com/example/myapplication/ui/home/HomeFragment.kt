package com.example.myapplication.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapters.TimelineAdapter
import com.example.myapplication.models.TimelineItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.*


class HomeFragment : Fragment(), TimelineAdapter.OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel

    private val storageFile: String = "storage.json"
    private var timelineList: ArrayList<TimelineItem> = ArrayList()

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

    override fun onItemClick(v: View, position: Int) {
        //Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()
        //v.backgroundTintList = ColorStateList.valueOf(Color.RED)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFilePresent = isFilePresent(activity!!, storageFile)
        if (isFilePresent) {
            val jsonString = read(activity!!, storageFile)
            Log.d("HomeFragment.onCreate", jsonString!!)
            timelineList = ArrayList(Gson().fromJson(jsonString, Array<TimelineItem>::class.java).toList())
        } else {
            val isFileCreated = create(activity!!, storageFile, Gson().toJson(timelineList))
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineRecycler.adapter = TimelineAdapter(timelineList, this)
        timelineRecycler.layoutManager = LinearLayoutManager(this.context)

        fun save() {
            val item = TimelineItem(entryTitle.text.toString(), entryBody.text.toString()) // TODO: add tag metadata
            timelineList.add(item)
            val isFileCreated = create(activity!!, storageFile, Gson().toJson(timelineList))
            if (!isFileCreated) {
                Log.e("HomeFragment.onCreate", "Failed to create json file")
            }
        }

        fun back() {
            val imm =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(getView()!!.windowToken, 0)
            view.findViewById<ConstraintLayout>(R.id.main).visibility = View.VISIBLE
            view.rootView.findViewById<FloatingActionButton>(R.id.fab).show()
            view.rootView.findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.entry).visibility = View.GONE
            entryBack.visibility = View.VISIBLE
            entrySave.visibility = View.GONE
            entryTitle.setText("")
            entryBody.setText("")
        }

        entryTitle.setOnFocusChangeListener { _, b ->
            if(b) {
                entryBack.visibility = View.GONE
                entrySave.visibility = View.VISIBLE
            }
        }
        entryBody.setOnFocusChangeListener { _, b ->
            if(b) {
                entryBack.visibility = View.GONE
                entrySave.visibility = View.VISIBLE
            }
        }

        entrySave.setOnClickListener {
            save()
            back()
        }
        entryBack.setOnClickListener {
            back()
        }
    }
}