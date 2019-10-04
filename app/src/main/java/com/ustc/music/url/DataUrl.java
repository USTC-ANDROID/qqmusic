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
    String zuiXinZhuanJi = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=recom07639530285381979&" +
            "g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&not" +
            "ice=0&platform=yqq.json&needNewCode=0&data={\"comm\":{\"ct\":24},\"new_album\"" +
            ":{\"module\":\"newalbum.NewAlbumServer\",\"method\":\"get_new_album_info\",\"p" +
            "aram\":{\"area\":1,\"sin\":0,\"num\":10}}}";

    String zuiXinZhuanJiPicUrl = "http://y.gtimg.cn/music/photo_new/T002R300x300M000{mid}.jpg?max_age=2592000";


}
