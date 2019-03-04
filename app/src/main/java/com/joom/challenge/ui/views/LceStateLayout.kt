package com.joom.challenge.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.joom.challenge.R
import java.lang.IllegalStateException

class LceStateLayout(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {

    var onRetryClickedListener: (() -> Unit)? = null

    // TODO move to styled attrs to be able to customize
    private val progressLayoutId = R.layout.progress_view
    private val errorLayoutId = R.layout.error_view

    private lateinit var contentLayout: View
    private lateinit var progressLayout: View
    private lateinit var errorLayout: View
    private lateinit var errorTxt: TextView
    private lateinit var retryBtn: View
    private lateinit var state: State

    private var initialized = false

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (initialized) return
        initialized = true

        if (childCount > 1) throw IllegalStateException("You must specify only one child in LceStateLayout")
        if (childCount == 0) throw IllegalStateException("No child specified")

        val inflater = LayoutInflater.from(context)

        contentLayout = getChildAt(0)
        progressLayout = inflater.inflate(progressLayoutId, this, false)
        errorLayout = inflater.inflate(errorLayoutId, this, false)

        val progressLayoutParams = LayoutParams(progressLayout.layoutParams)
        progressLayoutParams.addRule(CENTER_IN_PARENT)

        val errorLayoutParams = LayoutParams(errorLayout.layoutParams)
        errorLayoutParams.addRule(CENTER_IN_PARENT)

        addView(progressLayout, progressLayoutParams)
        addView(errorLayout, errorLayoutParams)

        val errorTxt = findViewById<TextView>(R.id.lceErrorTxt)
        val retryBtn = findViewById<View>(R.id.lceRetryBtn)

        checkNotNull(errorTxt) { "You must specify TextView with id 'lceErrorTxt' for displaying errors" }
        checkNotNull(retryBtn) { "You must specify View with id 'lceRetryBtn' for handling retries" }

        this.errorTxt = errorTxt
        this.retryBtn = retryBtn

        retryBtn.setOnClickListener {
            onRetryClickedListener?.invoke()
        }

        setState(State.NONE)
    }

    fun showLoading() {
        setState(State.LOADING)
    }

    fun showError(text: String) {
        errorTxt.text = text
        setState(State.ERROR)
    }

    fun showContent() {
        setState(State.CONTENT)
    }

    private fun setState(newState: State) {
        if (::state.isInitialized && state == newState) return

        this.state = newState

        when (state) {
            State.LOADING -> {
                progressLayout.visibility = View.VISIBLE
                contentLayout.visibility = View.INVISIBLE
                errorLayout.visibility = View.INVISIBLE
            }
            State.CONTENT -> {
                progressLayout.visibility = View.INVISIBLE
                contentLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.INVISIBLE
            }
            State.ERROR -> {
                progressLayout.visibility = View.INVISIBLE
                contentLayout.visibility = View.INVISIBLE
                errorLayout.visibility = View.VISIBLE
            }
            State.NONE -> {
                progressLayout.visibility = View.INVISIBLE
                contentLayout.visibility = View.INVISIBLE
                errorLayout.visibility = View.INVISIBLE
            }
        }
    }

    private enum class State { LOADING, CONTENT, ERROR, NONE }
}