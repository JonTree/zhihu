package com.example.tree.zhihu.GsonClass;

import java.util.List;

public class PastNews {

    /**
     * date : 20190210
     * stories : [{"images":["https://pic1.zhimg.com/v2-70a641bb2e09e6b9378ef0db724ad838.jpg"],"type":0,"id":9707148,"ga_prefix":"021022","title":"年度小事 · 我知道你的过去，但这有什么关系"},{"images":["https://pic3.zhimg.com/v2-2579224f20de11d9da5e38a9c229b94e.jpg"],"type":0,"id":9706330,"ga_prefix":"021019","title":"年度热门 · 刚刚，我的画拍了 104 万英镑，现在我来亲手毁了它"},{"images":["https://pic1.zhimg.com/v2-7b13dcfa07bf309fb0dbb14d632d3420.jpg"],"type":0,"id":9706320,"ga_prefix":"021018","title":"年度热门 · 如果每年只拿一元年薪，离婚了分配资产，可以分到几元？"},{"images":["https://pic3.zhimg.com/v2-70067af6af5a1f4a4a523ee2a0f3837e.jpg"],"type":0,"id":9706345,"ga_prefix":"021016","title":"年度热门 · 地动仪为什么会被从教科书中删除？"},{"images":["https://pic3.zhimg.com/v2-4ef8a8828858362832555f09ad83674e.jpg"],"type":0,"id":9706307,"ga_prefix":"021010","title":"年度热门 · 「他离职影响了中国登月」，作为国企员工，借这个机会说几句"},{"images":["https://pic1.zhimg.com/v2-ba437180870804a3ac834a0dc0a9f768.jpg"],"type":0,"id":9706311,"ga_prefix":"021009","title":"年度热门 · 一个清华天才少年，一款领先抖音 3 年的产品，与一场庞氏骗局"},{"title":"年度热门 · 数学和物理太难？这些 GIF 让你秒懂抽象概念","ga_prefix":"021008","images":["https://pic3.zhimg.com/v2-1f3fd3693fa113625bdf5aa1e7c586e6.jpg"],"multipic":true,"type":0,"id":9706630},{"title":"年度热门 · 坐拥 9000 辆汽车、1000 列火车、40 架飞机\u2026\u2026不够，还不够","ga_prefix":"021007","images":["https://pic1.zhimg.com/v2-6ef8c351642f4601f8c2ecc41c1cadfc.jpg"],"multipic":true,"type":0,"id":9706569},{"images":["https://pic4.zhimg.com/v2-787b039efcac87dff28e30d40b19fa2f.jpg"],"type":0,"id":9706297,"ga_prefix":"021007","title":"年度热门 · 曾经美国人也挺爱存钱的，后来发生的事情跟中国有点像了"},{"images":["https://pic3.zhimg.com/v2-86c25e2f9c4a95b85203bc978d889c6a.jpg"],"type":0,"id":9707328,"ga_prefix":"021006","title":"年度瞎扯 · 全年最佳吐槽"}]
     */

    private String date;
    private List<StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic1.zhimg.com/v2-70a641bb2e09e6b9378ef0db724ad838.jpg"]
         * type : 0
         * id : 9707148
         * ga_prefix : 021022
         * title : 年度小事 · 我知道你的过去，但这有什么关系
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
