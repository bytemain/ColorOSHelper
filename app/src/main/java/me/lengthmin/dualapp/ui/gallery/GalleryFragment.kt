package me.lengthmin.dualapp.ui.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.lengthmin.dualapp.R
import me.lengthmin.dualapp.getRefreshRateDesc
import me.lengthmin.dualapp.setRefreshRate

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
        val textView: TextView = root.findViewById(R.id.text_gallery)
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val set90: Button = root.findViewById(R.id.set90)
        val goSetting: Button = root.findViewById(R.id.goSetting)
        val goDev: Button = root.findViewById(R.id.goDev)
        val cbBoot: CheckBox = root.findViewById(R.id.cbBoot)
        val hzValue: EditText = root.findViewById(R.id.hz_value)
        hzValue.setText("30")
        textView.text = this.context?.let { it1 -> getRefreshRateDesc(it1) }

        set90.setOnClickListener {
            this.context?.setRefreshRate(hzValue.text.toString());
            textView.text = this.context?.let { it1 -> getRefreshRateDesc(it1) }
        }
        goSetting.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setClassName(
                        "com.android.settings",
                        "com.android.settings.Settings\$DisplaySettingsActivity"
                )
                flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            }
            startActivity(intent)

        }
        goDev.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            }
            startActivity(intent)
        }
        val sharedPref = this.context?.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            cbBoot.isChecked = sharedPref.getBoolean(getString(R.string.settings_boot), false)
            cbBoot.setOnClickListener {
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.settings_boot), cbBoot.isChecked)
                    apply()
                }
            }
        }

        return root
    }
}