package com.example.tree.zhihu.GsonClass;

import java.util.ArrayList;
import java.util.List;

public class News {

    public static String getNewsUrl(String s) {
        StringBuilder news = new StringBuilder("https://news-at.zhihu.com/api/4/news/before/00000000");
        news.delete(44, 52);
        news.append(s);
        final String string = news.toString();
        return string;
    }

    public static String getNewsExtraUrl(int id){
        StringBuilder newsExtraUrl = new StringBuilder("https://news-at.zhihu.com/api/4/story-extra/0000000");
        newsExtraUrl.delete(44, 51);
        newsExtraUrl.append(id);
        final String string = newsExtraUrl.toString();
        return string;
    }

    public static String getNewsContentUrl(int id){
        StringBuilder newsContentUrl = new StringBuilder("https://news-at.zhihu.com/api/4/news/0000000");
        newsContentUrl.delete(37, 44);
        newsContentUrl.append(id);
        final String string = newsContentUrl.toString();
        return string;
    }



    /**
     * date : 20190217
     * stories : [{"images":["https://pic1.zhimg.com/v2-aef3e62b3752c69a4882c29435e09e78.jpg"],"type":0,"id":9678743,"ga_prefix":"021710","title":"- 我要减肥，这次是认真的\r\n- 你哪次不说是认真的"},{"images":["https://pic3.zhimg.com/v2-f800a8fe4e332577e408adef5970f89a.jpg"],"type":0,"id":9707648,"ga_prefix":"021709","title":"归化球员没法入选国足？我们帮你查了查 FIFA 的规定"},{"images":["https://pic2.zhimg.com/v2-11dea3a8cf9a8b1538134aab4d1f2855.jpg"],"type":0,"id":9707629,"ga_prefix":"021708","title":"起底「疟疾治疗癌症之父」和那个「亲爱的爸爸」"},{"images":["https://pic3.zhimg.com/v2-2b6f10fae8552cd069b96c73c6afcd6e.jpg"],"type":0,"id":9707122,"ga_prefix":"021707","title":"千古未解之谜之麻辣烫到底是四川的还是东北的？"},{"title":"我从零开始，做了一款国产性教育游戏","ga_prefix":"021707","images":["https://pic3.zhimg.com/v2-f044f9b2348e9b8cc6525e9ba5d3ba46.jpg"],"multipic":true,"type":0,"id":9707638},{"images":["https://pic1.zhimg.com/v2-6790d67f5ab48401211df8717e7dbd8c.jpg"],"type":0,"id":9707622,"ga_prefix":"021706","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic2.zhimg.com/v2-c514804bb28144e0b892af633c2a8a39.jpg","type":0,"id":9707638,"ga_prefix":"021707","title":"我从零开始，做了一款国产性教育游戏"},{"image":"https://pic1.zhimg.com/v2-94e917f7a18a45639ca44d7938223bc8.jpg","type":0,"id":9707122,"ga_prefix":"021707","title":"千古未解之谜之麻辣烫到底是四川的还是东北的？"},{"image":"https://pic3.zhimg.com/v2-9e56171f0aedd93e793fc684f225b3b6.jpg","type":0,"id":9707629,"ga_prefix":"021708","title":"起底「疟疾治疗癌症之父」和那个「亲爱的爸爸」"},{"image":"https://pic2.zhimg.com/v2-d28bc065ec40ac2f1c34cfa6856b6b11.jpg","type":0,"id":9707608,"ga_prefix":"021609","title":"快死绝的穿山甲，和吃不够的中国人"},{"image":"https://pic1.zhimg.com/v2-910c5b213ea5a4e5bae4008914305a14.jpg","type":0,"id":9707547,"ga_prefix":"021619","title":"我是渐冻人罗罔极，我想找个喜欢的姑娘"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories = null;

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

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }


    public static class StoriesBean {
        /**
         * images : ["https://pic1.zhimg.com/v2-aef3e62b3752c69a4882c29435e09e78.jpg"]
         * type : 0
         * id : 9678743
         * ga_prefix : 021710
         * title : - 我要减肥，这次是认真的
         - 你哪次不说是认真的
         * multipic : true
         */

        private int id;
        private String title;
        private List<String> images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic2.zhimg.com/v2-c514804bb28144e0b892af633c2a8a39.jpg
         * type : 0
         * id : 9707638
         * ga_prefix : 021707
         * title : 我从零开始，做了一款国产性教育游戏
         */

        private String image;
        private int id;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
