package com.active.orbit.baseapp.core.download

interface Downloader {

    fun downloadFile(url: String, type: String, title: String): Long
}