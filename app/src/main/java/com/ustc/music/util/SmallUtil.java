package com.ustc.music.util;

/**
 * 小工具
 * 放一些乱七八糟的小工具
 */
public class SmallUtil {

    /**
     * 播放量格式化工具
     * @param num 歌曲的播放量
     * @return 返回格式化的字符串 例如：某歌曲播放量为82345 返回8.3万
     */
    public static String accessNumFormat(int num) {
        int val = num / 1000;
        if(val == 0) {
            return val + "";
        } else {
            float fval = val / 10f;
            return fval + "万";
        }
    }


}
