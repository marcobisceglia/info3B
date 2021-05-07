package com.example.divingapp2021

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

object FragmentHelper {
    fun addFragmentFromSide(activity: FragmentActivity?, fragment: Fragment, layoutResourceId: Int) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentName = fragment.javaClass.simpleName
        if (fragmentManager?.findFragmentByTag(fragmentName) == null) {
            fragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_from_right, R.animator.enter_from_right, R.animator.exit_from_right)
                ?.add(layoutResourceId, fragment, fragmentName)
                ?.addToBackStack(fragmentName)
                ?.commit()
        }
    }

    fun addFragmentFromSide(activity: FragmentActivity?, fragment: Fragment, layoutResourceId: Int, ignoreDuplicates: Boolean) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentName = fragment.javaClass.simpleName
        if (ignoreDuplicates || fragmentManager != null && fragmentManager.findFragmentByTag(fragmentName) == null) {
            fragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_from_right, R.animator.enter_from_right, R.animator.exit_from_right)
                ?.add(layoutResourceId, fragment, fragmentName)
                ?.addToBackStack(fragmentName)
                ?.commit()
        }
    }

    fun addFragmentFromBottom(activity: FragmentActivity?, fragment: Fragment, layoutResourceId: Int) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentName = fragment.javaClass.simpleName
        if (fragmentManager?.findFragmentByTag(fragmentName) == null) {
            fragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.animator.enter_from_bottom, R.animator.exit_from_bottom, R.animator.enter_from_bottom, R.animator.exit_from_bottom)
                ?.add(layoutResourceId, fragment, fragmentName)
                ?.addToBackStack(fragmentName)
                ?.commit()
        }
    }

    fun addFragmentFromBottom(activity: FragmentActivity?, fragment: Fragment, layoutResourceId: Int, ignoreDuplicates: Boolean) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentName = fragment.javaClass.simpleName
        if (ignoreDuplicates || fragmentManager?.findFragmentByTag(fragmentName) == null) {
            fragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.animator.enter_from_bottom, R.animator.exit_from_bottom, R.animator.enter_from_bottom, R.animator.exit_from_bottom)
                ?.add(layoutResourceId, fragment, fragmentName)
                ?.addToBackStack(fragmentName)
                ?.commit()
        }
    }

    fun addFragmentToContainer(context: Context?, fragmentManager: FragmentManager, container: LinearLayout, fragment: Fragment): LinearLayout {
        val layout = LinearLayout(context)
        layout.id = View.generateViewId()
        layout.orientation = LinearLayout.VERTICAL
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        container.addView(layout)

        fragmentManager
            .beginTransaction()
            .add(layout.id, fragment, fragment.tag)
            .commit()
        return layout
    }

    fun addFragmentToContainer(activity: FragmentActivity?, fragment: Fragment, layoutResourceId: Int) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentName = fragment.javaClass.simpleName
        fragmentManager
            ?.beginTransaction()
            ?.add(layoutResourceId, fragment, fragmentName)
            ?.commit()
    }

    fun replaceFragment(activity: FragmentActivity?, fragment: Fragment, layoutResourceId: Int) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()
            ?.replace(layoutResourceId, fragment, fragment.javaClass.simpleName)
            ?.commit()
    }

    fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment, layoutResourceId: Int) {
        fragmentManager.beginTransaction()
            .replace(layoutResourceId, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    fun getCurrentFragment(activity: FragmentActivity?): Fragment? {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager?.backStackEntryCount ?: 0 > 0) {
            val fragmentTag = fragmentManager?.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)?.name

            return fragmentManager?.findFragmentByTag(fragmentTag)
        }

        return fragmentManager?.findFragmentByTag(HomeFragment::class.java.simpleName)
    }

    fun popToHomeFragment(activity: FragmentActivity?) {
        val fragmentManager = activity?.supportFragmentManager
        if(fragmentManager != null) {
            for (i in 0 until fragmentManager.backStackEntryCount) {
                val backStackId = fragmentManager.getBackStackEntryAt(i).id
                fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    fun popBackFragment(activity: FragmentActivity?) {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager?.backStackEntryCount ?: 0 > 0) {
            fragmentManager?.popBackStackImmediate()
        }
    }

    fun refreshFragment(activity: FragmentActivity?, fragment: Fragment?) {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.detach(fragment!!)?.attach(fragment)?.commit()
    }

    fun refreshFragment(activity: FragmentActivity?, fragmentTag: String?) {
        val fragmentManager = activity?.supportFragmentManager
        val fragment = fragmentManager?.findFragmentByTag(fragmentTag)
        fragment?.let { refreshFragment(activity, it) }
    }

    fun getLastFragment(activity: FragmentActivity?): Fragment? {
        val fragmentManager = activity?.supportFragmentManager
        if(fragmentManager != null) {
            val fragments: List<*> = fragmentManager.fragments
            return if (fragments.isNotEmpty()) fragmentManager.fragments[fragments.size - 1] else null
        }

        return null
    }

    fun hideKeyboard(activity: FragmentActivity?) {
        val input: InputMethodManager? = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (input?.isAcceptingText == true) {
            input.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
    }
}