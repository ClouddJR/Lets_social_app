package com.lets.app.utils

import android.util.Base64
import com.google.firebase.firestore.GeoPoint
import java.nio.charset.Charset
import kotlin.math.floor

object ChunkUtils {

    //524,169 = NTI0LDE2OQ==
    //523,169 = NTIzLDE2OQ==

    fun getChunkFromLocation(location: GeoPoint): String {
        val lat = floor((location.latitude * 10)).toInt()
        val lon = floor(location.longitude * 10).toInt()

        val str = "$lat,$lon"

        return Base64.encode(str.toByteArray(), Base64.DEFAULT)
                .toString(Charset.defaultCharset())
    }

    private fun getChunkFromString(locationString: String): String {
        return Base64.encode(locationString.toByteArray(), Base64.DEFAULT)
                .toString(Charset.defaultCharset())
    }

    fun getAllChunkNearby(location: GeoPoint): List<String> {
        val centerLat = floor((location.latitude * 10)).toInt()
        val centerLon = floor((location.longitude * 10)).toInt()

        val latArray = (centerLat - 1..centerLat + 1)
        val lonArray = (centerLon - 1..centerLon + 1)

        val chunksNearby = mutableListOf<String>()

        latArray.forEach { lat ->
            lonArray.forEach { lon ->
                chunksNearby.add(getChunkFromString("$lat,$lon"))
            }
        }

        return chunksNearby
    }
}