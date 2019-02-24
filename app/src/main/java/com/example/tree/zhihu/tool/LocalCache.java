package com.example.tree.zhihu.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.ContentValues.TAG;

public class LocalCache {
    //获得路径
    private static final String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhihu_pure";


    //保存所获得的Json数据
    public static void saveJsonData(Context context, String dataUrl, String data) throws IOException {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput(MD5Encoder.encode(dataUrl), Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }

    //读出所保存的Json数据
    public static String readJsonData(Context context, String dataUrl) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput(MD5Encoder.encode(dataUrl));
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            //读取
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }



    // 将图片写入本地缓存
    public static void setLocalImageCache(String url, Bitmap bitmap) {
        File dir = new File(LOCAL_CACHE_PATH);
        //没有文件夹创建文件夹
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();  // 创建文件夹
        }
        try {
            String fileName = MD5Encoder.encode(url);
            File cacheFile = new File(dir, fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
            // 参1:图片格式;参2:压缩比例0-100; 参3:输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取本地图片缓存
    public static Bitmap getLocalImageCache(String url) {
        try {
            File cacheFile = new File(LOCAL_CACHE_PATH,MD5Encoder.encode(url));
            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
