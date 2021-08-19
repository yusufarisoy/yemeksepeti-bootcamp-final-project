package com.yusufgokmenarisoy.foodorder.util

import android.view.View

class Extension {

    companion object {

        fun View.show() {
            visibility = View.VISIBLE
        }

        fun View.hide() {
            visibility = View.GONE
        }
    }
}