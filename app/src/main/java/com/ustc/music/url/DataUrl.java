package com.ustc.music.url;

/**
 * 数据URL及对应的数据解析操作
 */
public interface DataUrl {

    /**
     * 获取QQ音乐首页的数据
     */
    String Banner = "https://u.y.qq.com/cgi-bin/musicu.fcg?data={%22focus%22:%20{%22module%" +
            "22:%20%22QQMusic.MusichallServer%22,%22method%22:%20%22GetFocus%22,%22param%22:%20{}}}";

//    public

    /**
     * 官方歌单列表的数据 size修改返回数据的条数
     */
    String GuangFangGeDan = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=recom16962001886744793" +
            "&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&no" +
            "tice=0&platform=yqq.json&needNewCode=0&data={\"comm\":{\"ct\":24},\"playlist\":" +
            "{\"method\":\"get_playlist_by_category\",\"param\":{\"id\":3317,\"curPage\":1," +
            "\"size\":6,\"order\":5,\"titleid\":3317},\"module\":\"playlist.PlayListPlazaServer\"}}";


    /**
     * 达人歌单url
     */
    String DaRenGeDan = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=recom41875803921016286&g_t" +
            "k=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice" +
            "=0&platform=yqq.json&needNewCode=0&data={\"comm\":{\"ct\":24},\"recomPlaylist\"" +
            ":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\"" +
            ":\"playlist.HotRecommendServer\"}}";

    /**
     * 最新专辑列表url
     */
    String zuiXinZhuanJi = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=recom4572131990219359&" +
            "g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&no" +
            "tice=0&platform=yqq.json&needNewCode=0&data={\"comm\":{\"ct\":24},\"playlist\"" +
            ":{\"method\":\"get_playlist_by_category\",\"param\":{\"id\":107,\"curPage\":1" +
            ",\"size\":10,\"order\":5,\"titleid\":107},\"module\":\"playlist.PlayListPlazaServer\"}}";

    String guanFangGeDanDetail = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids" +
            "_cp.fcg?type=1&json=1&utf8=1&onlysong=0&new_format=1&disstid={1}&g_tk=" +
            "5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=" +
            "0&platform=yqq.json&needNewCode=0";

    String refer = "https://y.qq.com/n/yqq/playsquare/{1}.html";

    String zuiXinZhuanJiPicUrl = "http://y.gtimg.cn/music/photo_new/T002R300x300M000{mid}.jpg?max_age=2592000";


    String musicList = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?t" +
            "ype=1&json=1&utf8=1&onlysong=0&new_format=1&disstid={1}&g_tk=5381&logi" +
            "nUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform" +
            "=yqq.json&needNewCode=0";

    String playMusicStep1 = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey016166" +
            "46856678372&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outChar" +
            "set=utf-8&notice=0&platform=yqq.json&needNewCode=0&data={\"req_0\":{\"module" +
            "\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"500" +
            "61212\",\"songmid\":[\"{1}\"],\"songtype\":[0],\"uin\":\"0\",\"log" +
            "inflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":0,\"format\":\"json\",\"ct" +
            "\":24,\"cv\":0}}";

    String playMusicStep2 = "http://153.37.232.148/amobile.music.tc.qq.com/{1}";

    String musicLogo = "https://y.gtimg.cn/music/photo_new/T002R300x300M000{1}.jpg?max_age=2592000";

    String musicLrc = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?-=MusicJs" +
            "onCallback_lrc&pcachetime=1571717459244&songmid={1}&g_tk=5381&logi" +
            "nUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";
}
