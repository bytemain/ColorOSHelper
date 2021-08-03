package me.lengthmin.coshelper.ui.refresh_rate

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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.lengthmin.coshelper.R
import me.lengthmin.coshelper.getRefreshRateDesc
import me.lengthmin.coshelper.setRefreshRate

class RefreshRateFragment : Fragment() {

    private lateinit var refreshRateViewModel: RefreshRateViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_refresh_rate, container, false)
        refreshRateViewModel =
                ViewModelProvider(this).get(RefreshRateViewModel::class.java)
        val textView: TextView = root.findViewById(R.id.refresh_rate_info)

        val apply: Button = root.findViewById(R.id.apply)
        val goSetting: Button = root.findViewById(R.id.goSetting)
        val goDev: Button = root.findViewById(R.id.goDev)
        val cbBoot: CheckBox = root.findViewById(R.id.cbBoot)
        val hzValue: EditText = root.findViewById(R.id.hz_value)
        textView.text = this.context?.let { it1 -> getRefreshRateDesc(it1) }

        apply.setOnClickListener {
            val v = hzValue.text.toString()
            this.context?.setRefreshRate(if (v.isEmpty()) "30" else v)
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
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            textView.text = this.context?.let { it1 -> getRefreshRateDesc(it1) }
        }
        return root
    }
}