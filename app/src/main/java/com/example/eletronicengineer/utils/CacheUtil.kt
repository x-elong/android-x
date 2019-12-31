package com.example.eletronicengineer.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal
import android.os.Environment.MEDIA_MOUNTED
import android.util.Log
import com.example.eletronicengineer.custom.LoadingDialog


class CacheUtil {
    companion object {
        lateinit var loadingDialog: LoadingDialog
        fun getCacheSize(context: Context): String {
            val getCacheDirSize = getFolderSize(context.cacheDir)
            val getExternalCacheDir = getFolderSize(context.externalCacheDir!!)
            Log.i("","${getCacheDirSize},${getExternalCacheDir}")
            return getFormatSize( getCacheDirSize+ getExternalCacheDir)
        }
        fun getFolderSize(file: File): Double {
            var size: Double = 0.0
            try {
                val fileList = file.listFiles()
                for (f in fileList) {
                    if (f.isDirectory)
                        size += getFolderSize(f)
                    else
                        size += f.length()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return size
        }

        fun getFormatSize(size: Double): String {
            val kiloByte = size / 1024

            val megaByte = kiloByte / 1024
            if (megaByte < 1) {
                val result1 = BigDecimal(kiloByte.toString())
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
            }

            val gigaByte = megaByte / 1024
            if (gigaByte < 1) {
                val result2 = BigDecimal(megaByte.toString())
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
            }

            val teraBytes = gigaByte / 1024
            if (teraBytes < 1) {
                val result3 = BigDecimal(gigaByte.toString())
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
            }


            val result4 = BigDecimal(teraBytes.toString())
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
        }


        /**
         * 清楚内部缓存和外部缓存
         * @param context
         */
        fun cleanCache(context: Context) {
            loadingDialog = LoadingDialog(context,"正在清除缓存...")
            loadingDialog.show()
            cleanInternalCache(context)
            cleanExternalCache(context)
            loadingDialog.dismiss()
        }

        /**
         * * 清除内部缓存(/data/data/com.xxx.xxx/cache) * *
         *
         * @param context
         */
        fun cleanInternalCache(context: Context) {
            deleteFilesByDirectory(context.cacheDir)
        }

        /**
         * * 清除外部缓存(/mnt/sdcard/android/data/com.xxx.xxx/cache)
         *
         * @param context
         */
        fun cleanExternalCache(context: Context) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED
                )
            ) {
                deleteFilesByDirectory(context.externalCacheDir)
            }
        }

        /**
         * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
         *
         * @param directory
         */
        fun deleteFilesByDirectory(directory: File?) {
            if (directory != null && directory.exists() && directory.isDirectory) {
                for (item in directory.listFiles()) {
                    item.delete()
                }
            }
        }

        /**
         * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
         *
         * @param context
         */
        fun cleanDatabases(context: Context) {
            deleteFilesByDirectory(
                File(
                    "/data/data/"
                            + context.packageName + "/databases"
                )
            )
        }

        /**
         * * 按名字清除本应用数据库 * *
         *
         * @param context
         * @param dbName
         */
        fun cleanDatabaseByName(context: Context, dbName: String) {
            context.deleteDatabase(dbName)
        }

        /**
         * 清除/data/data/com.xxx.xxx/files下的内容
         *
         * @param context
         */
        fun cleanFiles(context: Context) {
            deleteFilesByDirectory(context.filesDir)
        }

        /**
         * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
         *
         * @param context
         */
        fun cleanSharedPreference(context: Context) {
            deleteFilesByDirectory(
                File(
                    "/data/data/"
                            + context.packageName + "/shared_prefs"
                )
            )
        }
    }

}