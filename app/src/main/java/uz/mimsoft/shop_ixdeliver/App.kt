package uz.mimsoft.shop_ixdeliver

import android.app.Application

class App :Application(){
    companion object {
        private lateinit var app:App
        fun getInstance () :App = app
    }

    override fun onCreate() {
        app = this
        super.onCreate()
    }
}