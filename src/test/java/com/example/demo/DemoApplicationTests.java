package com.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.Music163;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Test
    void contextLoads() {
    }

    /**
     * @description: 网易云音乐歌单下载
     * @author：RenBin
     * @createTime：2020/4/30 13:37
     */
    @Test
    void dowloadMusicList() {
        // 歌单ID
        Long listId = 2524566250L;
        // 下载路径
        String path = "C:\\Users\\Adminastrator\\Downloads\\music";
        Music163.dowloadList(listId, path);
    }

    @Test
    void lookMusicList() {
        long listId = 4917799722L;
        String playListStr = Music163.getPlayList(listId);
        JSONObject jsonObject = JSONObject.parseObject(playListStr);
        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("tracks");
        if (jsonArray.size() < 1) {
            return;
        }
        JSONObject curJSONObject;
        for (Object o : jsonArray) {
            curJSONObject = (JSONObject) o;
            System.out.println(curJSONObject.get("name"));
        }
    }
}
