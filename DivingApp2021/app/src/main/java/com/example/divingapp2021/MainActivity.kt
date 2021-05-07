package com.example.divingapp2021
import com.example.mylibrary.ProjectActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.example.divingapp2021.databinding.ActivityMainBinding

class MainActivity : ProjectActivity<ActivityMainBinding>(), FragmentManager.OnBackStackChangedListener{

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

        binding.toolbar.title="MAIN"

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
            "MAIN"
            // deviceService.getEquipment().fullName
        }
        if (fragment is NavigationFragment<*>)
            supportActionBar?.setDisplayHomeAsUpEnabled(fragment.getShowBackButton())
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