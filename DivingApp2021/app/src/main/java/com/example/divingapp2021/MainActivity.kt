package com.example.divingapp2021
import com.example.mylibrary.ProjectActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.beust.klaxon.Klaxon
import com.example.divingapp2021.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : ProjectActivity<ActivityMainBinding>(), FragmentManager.OnBackStackChangedListener{
    private val client = OkHttpClient()

    override fun buildBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.addOnBackStackChangedListener(this)

        FragmentHelper.replaceFragment(this, HomeFragment(), R.id.mainFrameLayout)

        setSupportActionBar(this.binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.title="DivingApp 2021"

    }

    override fun onBackPressed() {
         FragmentHelper.hideKeyboard(this)
      /*   if (tripUnitFragment.isInTabView() && !tripUnitFragment.isInFirstTab()) {
             tripUnitFragment.selectTab(EkipDeviceTabMenu.DASHBOARD)
         } else {
             super.onBackPressed()
         }*/
        super.onBackPressed()
    }

    override fun onBackStackChanged() {
        FragmentHelper.hideKeyboard(this)
        val fragment = FragmentHelper.getCurrentFragment(this)
        binding.toolbar.title=if (fragment is NavigationFragment<*> && fragment.getTitle().isNotEmpty()) {
            fragment.getTitle()
        } else {
            "DivingApp 2021"
            // deviceService.getEquipment().fullName
        }
        if(FragmentHelper.getCurrentFragment(this)?.tag == "HomeFragment"){
            disconnect()
        }
        if (fragment is NavigationFragment<*>)
            supportActionBar?.setDisplayHomeAsUpEnabled(fragment.getShowBackButton())
    }

    private fun disconnect(){
        val jsonBody = Klaxon().toJsonString(MyProperties.instance?.userLogged)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toRequestBody(mediaType)
        val request = Request.Builder()
                .url("http://10.0.2.2:8080/users/logout/")
                .post(body)
                .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
                //binding.noUserFound.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body!!.string()

                println("Successfully connected to webserver")
                println(json)

                if(response.code == 200){
                    val dialog = MessageDialog(MessageDialog.DIALOG_MODE.LOGOUT)
                    dialog.show(supportFragmentManager, "Logout")
                }
            }
        })
    }

    // : - Toolbar methods

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}