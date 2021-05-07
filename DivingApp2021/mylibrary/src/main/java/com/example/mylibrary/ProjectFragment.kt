package com.example.mylibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.io.Closeable
import java.io.IOException

abstract class ProjectFragment <B : ViewBinding> : Fragment() {
    protected lateinit var binding: B
    private val tagsBag: MutableMap<String, Any> = mutableMapOf()

    @Volatile
    private var tagsCleared = false

    protected abstract fun buildBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): B

    private fun closeWithRuntimeException(obj: Closeable) {
        try {
            obj.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this.binding = this.buildBinding(inflater, container, savedInstanceState)
        return this.binding.root
    }

    override fun onDestroy() {
        this.tagsCleared = true
        super.onDestroy()
        synchronized(this.tagsBag) {
            this.tagsBag.forEach { (_, v) -> if (v is Closeable) v.close() }
            this.tagsBag.clear()
        }
    }

    fun <T> getTag(key: String): T? {
        return synchronized(this.tagsBag) { this.tagsBag[key] as T? }
    }

    fun <T> setTagIfAbsent(key: String, newValue: T): T {
        val previous: T? = synchronized(this.tagsBag) {
            val value = this.tagsBag[key] as T?
            if (value == null) {
                this.tagsBag[key] = newValue as Any
            }
            value
        }
        //
        val result = previous ?: newValue
        //
        if (tagsCleared && result != null && result is Closeable) {
            closeWithRuntimeException(result)
        }
        return result
    }
}