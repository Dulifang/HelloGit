package com.example.admin.youhuo.view;

/**
 * Created by admin on 2016/11/23.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.admin.youhuo.utils.FileUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片三级缓存类：1. 原理：先从缓存中查找，缓存中没有，再从SD卡中查找，SD卡没有，从网络查找下载
 * ，下载完成，在SD卡和缓存中分别保存一份
 * 2.LruCatch: 类似于内存强引用的缓存，  存值方式为键值对存值（K  V）,取值方便
 * 当缓存中内存的大小超出本身的内存的时候，它会自动将最前面的内存强制抛出
 * 交给垃圾回收机制（GC）进行回收
 * 3.set集合在android中的使用：去重的功能
 * 4.如何避免出现图片错位：给set集合中的imaeview添加唯一的tag，图片下载完成之后，在将
 * 图片设值给imageview的时候遍历整个set结合进行对比，符合的再设值
 * <p>
 * 使用recycleView
 * 5.一般使用，结合application使用
 */

public class Loader {
    private FileUtils fileutils;
    private Context ctx;
    private final static int MAX_POOLS = 5;
    private ExecutorService thread_pool;
    private Set<ImageView> imgs = new HashSet<ImageView>();
    private int max_size = (int) (Runtime.getRuntime().maxMemory()) / 1024 / 5;
    private LruCache<String, Bitmap> lru = new LruCache<String, Bitmap>(max_size) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            // TODO Auto-generated method stub
            return value.getByteCount() / 1024;
        }
    };
    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 200) {
                Bitmap bitmap = (Bitmap) msg.obj;
                String name = msg.getData().getString("imgname");
                Iterator<ImageView> it = imgs.iterator();
                while (it.hasNext()) {
                    ImageView img = it.next();
                    String tag = (String) img.getTag();
                    if (tag.equals(name)) {
                        img.setImageBitmap(bitmap);
                        return;
                    }
                }
            }
        }
    };

    public void Load(String name, ImageView img, Context ctx) {
        if (name == null)
            return;
        if (img == null)
            return;
        this.ctx = ctx;
        Bitmap bitmap;
        String name1 = name.replace("/", "").replace(".", "").replace(":", "").replace("_", "");
        img.setTag(name1);
        imgs.add(img);
        bitmap = lru.get(name1);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
            return;
        }
        if (fileutils == null) {
            fileutils = new FileUtils(ctx);
            bitmap = fileutils.getBitmap(name1);
            if (bitmap != null) {
                img.setImageBitmap(bitmap);
                lru.put(name1, bitmap);
                return;
            }
        }
        if (thread_pool == null) {
            thread_pool = Executors.newFixedThreadPool(MAX_POOLS);

        }
        thread_pool.execute(new ImgThread(name));
    }

    private class ImgThread implements Runnable {
        private String name;

        public ImgThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(name);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30 * 1000);
                conn.setReadTimeout(30 * 1000);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                String name1 = name.replace("/", "").replace(".", "").replace(":", "").replace("_", "");
                if (bitmap != null) {
                    fileutils.SaveBitmap(name1, bitmap);
                    lru.put(name1, bitmap);
                    Message msg = hand.obtainMessage();
                    msg.what = 200;
                    msg.obj = bitmap;
                    Bundle data = new Bundle();
                    data.putString("imgname", name1);
                    msg.setData(data);
                    hand.sendMessage(msg);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
