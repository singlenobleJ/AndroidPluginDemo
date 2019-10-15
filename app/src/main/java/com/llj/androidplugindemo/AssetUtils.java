package com.llj.androidplugindemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetUtils {

    /**
     * 将assets中的文件拷贝到app的缓存目录，并且返回拷贝之后文件的绝对路径
     *
     * @param context
     * @param fileName
     * @return {@code plugin apk absolute path}
     */
    public static String copyAssetToCache(Context context, String fileName) {
        //app的缓存目录
        File cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            //如果没有缓存目录，就创建
            cacheDir.mkdirs();

        }
        //创建输出的文件位置
        File outPath = new File(cacheDir, fileName);
        if (outPath.exists()) {
            //如果该文件已经存在，就删掉
            outPath.delete();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //创建文件，如果创建成功，就返回true
            boolean success = outPath.createNewFile();
            if (success) {
                //拿到main/assets目录的输入流，用于读取字节
                is = context.getAssets().open(fileName);
                //读取出来的字节最终写到outPath
                fos = new FileOutputStream(outPath);
                //缓存区
                byte[] buf = new byte[is.available()];
                int byteCount;
                //循环读取
                while ((byteCount = is.read(buf)) != -1) {
                    fos.write(buf, 0, byteCount);
                    fos.flush();
                }
                Log.i("AssetUtils", "插件apk复制到app缓存文件夹成功!");
                return outPath.getAbsolutePath();
            } else {
                Log.e("AssetUtils", "插件apk复制到app缓存文件夹失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AssetUtils", "插件apk复制到app缓存文件夹失败!");
        } finally {
            closeIO(fos, is, fos);
        }
        return null;
    }

    private static void closeIO(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}