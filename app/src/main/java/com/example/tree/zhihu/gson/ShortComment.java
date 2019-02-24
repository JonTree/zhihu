package com.example.tree.zhihu.gson;

import java.util.List;

public class ShortComment {

    public static String getShortCommentUrl(String id) {
        StringBuilder news = new StringBuilder("https://news-at.zhihu.com/api/4/story//short-comments");
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
         * author : 郭aurarol
         * content : 唔，是我记错了，谢谢
         * avatar : http://pic1.zhimg.com/2c82bce5979671d6dc2db943c374d284_im.jpg
         * time : 1550817826
         * reply_to : {"content":"病毒不可以是单纯的蛋白质，只能是DNA＋蛋白质orRNA＋蛋白质","status":0,"id":32934074,"author":"多么难决定"}
         * id : 32934421
         * likes : 0
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private ReplyToBean reply_to;
        private int id;
        private int likes;

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

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
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

        public static class ReplyToBean {
            /**
             * content : 病毒不可以是单纯的蛋白质，只能是DNA＋蛋白质orRNA＋蛋白质
             * status : 0
             * id : 32934074
             * author : 多么难决定
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
