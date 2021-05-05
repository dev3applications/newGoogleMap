package com.deerbrain.googlemapsbase.Parcel

import android.os.Environment
import android.os.StatFs
import android.util.Log
import com.deerbrain.googlemapsbase.App
import com.google.android.gms.maps.model.Tile
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit


class ParcelCacheManager {

    companion object {
        val context = App.context

        public fun deleteAllParcelCache() {
            //delete all files inside of the parcel Cache
            val folder = File("${context.cacheDir}/tiles");
            if (folder.exists()) {
                val children: Array<String> = folder.list()
                for (i in children.indices) {
                    File(folder, children[i]).delete()
                }
            }
        }

        public fun deleteParcelFilesOver30Days() {
            //this will delete ParcelCache for all files that are older than 30 days old.
            val targetDir: File = File("${context.cacheDir}/tiles")
            if (!targetDir.exists()) {
                throw RuntimeException(
                    java.lang.String.format(
                        "Log files directory '%s' " +
                                "does not exist in the environment", "${context.cacheDir}/tiles"
                    )
                )
            }

            val files = targetDir.listFiles()
            for (file in files) {
                val diff: Long = Date().getTime() - file.lastModified()

                // Granularity = DAYS;
                val desiredLifespan: Long = TimeUnit.DAYS.toMillis(30)
                if (diff > desiredLifespan) {
                    file.delete()
                }
            }
        }

        public fun parcelDirectorySizeMB(): Int {
            // calculate the directory size of all of the parcel tiles stored on the device.
            val folder = File("${context.cacheDir}/tiles");
            val file_size: Int = java.lang.String.valueOf(folder.length() / 1024).toInt()

            return file_size
        }

        private fun isThereSpaceToWrite(): Boolean {
            //update this function to check for space on the device to ensure that we have space to store the jpg
            //this will return free space on device
          /*  val statFs = StatFs(Environment.getExternalStorageDirectory().getAbsolutePath())
            val Free = statFs.availableBlocks * statFs.blockSize / 1048576
            if (Free > 1000)
                return true
            else
                return false*/
            return true
        }

        private fun store(x: Int, y: Int, z: Int): Tile {

            Log.i("TAG", "getTile: ")
            val folder = File("${context.cacheDir}/tiles");
            if (!folder.exists()) {
                folder.mkdir()
            }
            val file = File("${context.cacheDir}/tiles/${x}_${y}_${z}.tile")
            if (isThereSpaceToWrite()) {

                try {
                    val inputStream = getTileUrl(
                        x,
                        y,
                        z
                    )?.readBytes()
                    val fileOutputStream = FileOutputStream(file)

                    fileOutputStream.write(inputStream)

                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: Exception) {
                    Log.d("TAG", "getTile: " + e.message)
                }
            }

            return Tile(x, y, file.readBytes())
        }

        private fun getTileUrl(x: Int, y: Int, z: Int): URL? {
          /*  return URL("https://mt1.google.com/vt/lyrs=y&x=${x}&y=${y}&z=${z}")*/
            var url="https://reportallusa.com/dyn/tile.py?map=siteroot/Base_Layers.map&layer=Parcels&mode=tile&tilemode=gmap&tile=${x}+${y}+${z}&client=ozEw4rZCd9"
            Log.e("response",url)
            return URL("https://reportallusa.com/dyn/tile.py?map=siteroot/Base_Layers.map&layer=Parcels&mode=tile&tilemode=gmap&tile=${x}+${y}+${z}&client=ozEw4rZCd9")
        }

        fun getTile(x: Int, y: Int, z: Int): Tile {
            Log.i("TAG", "getTile: ")
            //Is the line Below the best way to store files on the device but the user can't access those files?
            val file = File("${context.cacheDir}/tiles/${x}_${y}_${z}.tile")
            return if (file.exists()) {
                Tile(x, y, file.readBytes())
            } else {
                store(
                    x,
                    y,
                    z
                )
            }
        }


    }
}