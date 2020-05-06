package com.example.demo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * @version 1.0
 * @description:
 * @projectName：demo
 * @see: com.example.demo.util
 * @author： RenBin
 * @createTime：2020/4/30 8:12
 */
public class Music163 {

    /**
     * @description: 获取歌单
     * @author：RenBin
     * @createTime：2020/4/30 10:40
     */
    public static String getPlayList(Long id) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(String.format("http://music.163.com/api/playlist/detail?id=%s",
                        id));

                client = HttpClients.createDefault();
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * @description: 根据id下载单首歌曲
     * @author：RenBin
     * @createTime：2020/4/30 13:42
     */
    public static void downloadFile(Long id, String filePath, String fileName) {
        String urlPath = String.format("http://music.163.com/song/media/outer/url?id=%d.mp3", id);
        HttpURLConnection httpURLConnection = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileFullName = filePath + File.separatorChar + fileName+".mp3";
        System.out.println(String.format("%s  %d", fileFullName, id));
        // 文件名
        File file = new File(fileFullName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (OutputStream out = new FileOutputStream(fileFullName);
             BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(out)){
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

            int size;
            int bufferSize=1024*10;
            byte[] buf = new byte[bufferSize];
            while ((size = bin.read(buf)) != -1) {
                bufferedOutputStream.write(buf, 0, size);
            }
            out.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @description: 将歌单中的歌曲下载到指定目录
     * @author：RenBin
     * @createTime：2020/4/30 13:46
     */
    public static void dowloadList(Long listId, String path) {
        // 歌单的id
        String playListStr = Music163.getPlayList(listId);
        // 下载的路径
        JSONObject jsonObject = JSONObject.parseObject(playListStr);
        JSONArray trackArr = jsonObject.getJSONObject("result").getJSONArray("tracks");
        JSONObject curJsonObject;
        Long id;
        String name;
        for (Object o : trackArr) {
            curJsonObject = (JSONObject) o;
            name= (String) curJsonObject.get("name");
            id= Long.valueOf((Integer) curJsonObject.get("id"));
            Music163.downloadFile(id,path,name);
        }

    }
}
