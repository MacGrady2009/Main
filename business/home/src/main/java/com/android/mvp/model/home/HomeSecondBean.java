package com.android.mvp.model.home;

import java.util.List;

public class HomeSecondBean {


    /**
     * total : 10
     * totalPage : 1
     * next : false
     * result : [{"examRoomId":1,"examRoomName":"结构化考试","questionAmount":5,"userAmount":8,"onlineUserAmount":0,"examFormat":"标准结构化","beginTime":"06.07 08:00","examRoomStatus":"未上线"},{"examRoomId":3,"examRoomName":"结构化2","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"标准结构化","beginTime":"06.07 08:00","examRoomStatus":"未上线"},{"examRoomId":5,"examRoomName":"结构化3","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"标准结构化","beginTime":"06.07 08:00","examRoomStatus":"未上线"},{"examRoomId":7,"examRoomName":"结构化4","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"标准结构化","beginTime":"06.07 08:00","examRoomStatus":"未上线"},{"examRoomId":9,"examRoomName":"结构化5","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"标准结构化","beginTime":"06.07 08:00","examRoomStatus":"未上线"},{"examRoomId":2,"examRoomName":"无领导考试","questionAmount":5,"userAmount":8,"onlineUserAmount":0,"examFormat":"无领导","beginTime":"06.07 08:00","examRoomStatus":"进行中"},{"examRoomId":4,"examRoomName":"无领导考试","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"无领导","beginTime":"06.07 08:00","examRoomStatus":"进行中"},{"examRoomId":6,"examRoomName":"无领导考试","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"无领导","beginTime":"06.07 08:00","examRoomStatus":"进行中"},{"examRoomId":8,"examRoomName":"无领导考试","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"无领导","beginTime":"06.07 08:00","examRoomStatus":"进行中"},{"examRoomId":10,"examRoomName":"无领导考试","questionAmount":3,"userAmount":3,"onlineUserAmount":0,"examFormat":"场外看材料场内结构化","beginTime":"06.07 08:00","examRoomStatus":"进行中"}]
     */

    private int total;
    private int totalPage;
    private boolean next;
    private List<ResultBean> result;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * examRoomId : 1
         * examRoomName : 结构化考试
         * questionAmount : 5
         * userAmount : 8
         * onlineUserAmount : 0
         * examFormat : 标准结构化
         * beginTime : 06.07 08:00
         * examRoomStatus : 未上线
         */

        private int examRoomId;
        private String examRoomName;
        private int questionAmount;
        private int userAmount;
        private int onlineUserAmount;
        private String examFormat;
        private String beginTime;
        private String examRoomStatus;

        public int getExamRoomId() {
            return examRoomId;
        }

        public void setExamRoomId(int examRoomId) {
            this.examRoomId = examRoomId;
        }

        public String getExamRoomName() {
            return examRoomName;
        }

        public void setExamRoomName(String examRoomName) {
            this.examRoomName = examRoomName;
        }

        public int getQuestionAmount() {
            return questionAmount;
        }

        public void setQuestionAmount(int questionAmount) {
            this.questionAmount = questionAmount;
        }

        public int getUserAmount() {
            return userAmount;
        }

        public void setUserAmount(int userAmount) {
            this.userAmount = userAmount;
        }

        public int getOnlineUserAmount() {
            return onlineUserAmount;
        }

        public void setOnlineUserAmount(int onlineUserAmount) {
            this.onlineUserAmount = onlineUserAmount;
        }

        public String getExamFormat() {
            return examFormat;
        }

        public void setExamFormat(String examFormat) {
            this.examFormat = examFormat;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getExamRoomStatus() {
            return examRoomStatus;
        }

        public void setExamRoomStatus(String examRoomStatus) {
            this.examRoomStatus = examRoomStatus;
        }
    }
}
