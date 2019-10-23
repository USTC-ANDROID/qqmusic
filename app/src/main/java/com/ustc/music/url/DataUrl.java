package com.ustc.music.url;

public interface DataUrl {

    /**
     * 获取QQ音乐首页的数据
     */
    String Banner = "https://u.y.qq.com/cgi-bin/musicu.fcg?data={%22focus%22:%20{%22module%22:%20%22QQMusic.MusichallServer%22,%22method%22:%20%22GetFocus%22,%22param%22:%20{}}}";
    String RankUrl = "https://u.y.qq.com/cgi-bin/musicu.fcg?_=1569934525603";
    String TypeUrl = "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?picmid=1&rnd=0.2882718825643096&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8" +
            "&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&categoryId=10000000&sortId=5&sin=0&ein=19";


}
