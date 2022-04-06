package com.akame.developkit.util

import android.content.Context
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object FileUtil {
    /**
     * 创建文件
     * @param filePath 文件路径
     */
    fun createFile(filePath: String): File {
        val file = File(filePath)
        //如果存在 删除以前的
        if (file.exists()) {
            file.delete()
        }
        return file
    }

    /**
     * 创建文件夹
     */
    fun createDir(dirPath: String): File {
        val file = File(dirPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return /storage/emulated/0/Android/data/{$包名}/cache
     */
    fun getCachePath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState() || !Environment.isExternalStorageRemovable()
        ) {
            context.externalCacheDir?.path ?: context.cacheDir.path
        } else {
            context.cacheDir.path
        }
    }

    /**
     * 获取图片缓存地址
     */
    fun getImageCachePath(context: Context): String =
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
            ?: context.cacheDir.path + "/image"

    /**
     * 获取视频缓存地址
     */
    fun getVideoCachePath(context: Context): String =
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path
            ?: context.cacheDir.path + "/video"

    fun deleteFile(fileList: ArrayList<String>) {
        fileList.forEach {
            deleteFile(it)
        }
    }

    /**
     * 删除文件
     */
    fun deleteFile(filePath: String?) {
        filePath?.let {
            deleteFile(File(filePath))
        }
    }

    fun deleteFile(file: File?) {
        file?.let {
            if (file.exists()) {
                file.delete()
            }
        }
    }

    fun deleteDir(dirPath: String?) {
        dirPath?.let {
            val dirFile = File(it)
            if (dirFile.isDirectory) {
                val files = dirFile.listFiles()
                if (files?.isEmpty() == true) {
                    return
                }
                for (i in files.indices) {
                    deleteDir(files[i].path)
                }
            }
            dirFile.delete()
        }
    }

    /**
     * 获取文件大小
     */
    fun getFileSize(filePath: String): Long = File(filePath).length()

    /**
     * 获取整个文件夹大小
     */
    fun getDirSize(dirPath: String?): Long {
        var size = 0L
        return dirPath?.let {
            val dirFile = File(it)
            if (dirFile.isDirectory) {
                val files = dirFile.listFiles()
                if (files?.isEmpty() == true) {
                    return 0
                }
                for (i in files.indices) {
                    size += getDirSize(files[i].path)
                }
            } else {
                size += dirFile.length()
            }
            size
        } ?: size
    }

    /**
     * 获取程序缓存大小
     */
    suspend fun getCacheSize(context: Context): Long {
        return withContext(Dispatchers.Default) {
            val picPath = getCachePath(context)
            val moviePath = getVideoCachePath(context)
            val externalCachePath = context.externalCacheDir?.path
            val picSize = getDirSize(picPath)
            val movieSize = getDirSize(moviePath)
            val externalCacheSize = getDirSize(externalCachePath)
            picSize + movieSize + externalCacheSize
        }
    }

    suspend fun cleanCache(context: Context) {
        withContext(Dispatchers.Default) {
            val picPath = getImageCachePath(context)
            val moviePath = getVideoCachePath(context)
            val externalCachePath = context.externalCacheDir?.path
            deleteDir(picPath)
            deleteDir(moviePath)
            deleteDir(externalCachePath)
        }
    }

    /**
     * 获取文件后缀名
     */
    fun getFileSuffixName(file: File): String {
        val fileName = file.name
        return fileName.substring(fileName.lastIndexOf(".") + 1)
    }

    /**
     * 是否为图片类型
     */
    fun isImageFile(fileName: String) = fileName.endsWith(".jpg")
            || fileName.endsWith(".png")
            || fileName.endsWith(".webp")

}