package com.example.tree.zhihu.GsonClass;

public class NewsExtra{

    /**
     * long_comments : 4
     * popularity : 214
     * short_comments : 41
     * comments : 45
     */

    public static String getExtraShortcommentUrl(String s) {
        StringBuilder news = new StringBuilder("https://news-at.zhihu.com/api/4/story/0000000/short-comments");
        news.delete(38, 45);
        news.insert(38,s);
        final String string = news.toString();
        return string;
    }

    private int long_comments;
    private int popularity;
    private int short_comments;
    private int comments;

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}