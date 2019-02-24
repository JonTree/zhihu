package com.example.tree.zhihu.gson;

import java.util.List;

public class LongComment {

    public static String getLongCommentUrl(String id) {
        StringBuilder news = new StringBuilder("https://news-at.zhihu.com/api/4/story//long-comments");
        news.insert(38,id);
        final String string = news.toString();
        return string;
    }

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * author : 坂本先生不喝牛奶
         * content : 哈哈哈哈哈哈哈哈哈哈哈复活草哈哈哈哈哈哈哈哈哈哈哈
         * avatar : http://pic3.zhimg.com/v2-3a97a8266259e7d06c3bb218f4dda75a_im.jpg
         * time : 1550574488
         * id : 32924431
         * likes : 0
         * reply_to : {"content":"您好，请问经营企业的工商信息，行政处理等信息都在哪里可以查到？","status":0,"id":32923898,"author":"陈晨"}
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private int id;
        private int likes;
        private ReplyToBean reply_to;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public static class ReplyToBean {
            /**
             * content : 您好，请问经营企业的工商信息，行政处理等信息都在哪里可以查到？
             * status : 0
             * id : 32923898
             * author : 陈晨
             */

            private String content;
            private int status;
            private int id;
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
