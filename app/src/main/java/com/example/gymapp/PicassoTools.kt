package com.squareup.picasso

object PicassoTools {
    fun clearCache(p: Picasso) {
        p.cache.clear()
    }
}