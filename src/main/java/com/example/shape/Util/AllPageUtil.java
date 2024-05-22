package com.example.shape.Util;

import java.io.Serializable;
import java.util.List;

public class AllPageUtil<T> implements Serializable {

        // 每页显示多少条记录
        private int pageSize;
        //当前第几页数据
        private int currentPage;
        // 一共多少条记录
        private int totalRecord;
        // 一共多少页记录
        private int totalPage;
        //要显示的数据
        private List<T> dataList;
        //构造函数
        public AllPageUtil(int pageNum, int pageSize, List<T> sourceList){
            //如果数据源为空 跳出该构造方法
            if(sourceList == null || sourceList.isEmpty()){
                return;
            }

            // 总记录条数等于数据源的长度
            this.totalRecord = sourceList.size();

            // 每页显示多少条记录
            this.pageSize = pageSize;

            //获取最大页数  所有记录除以每页显示的条数
            this.totalPage = this.totalRecord / this.pageSize;
            if(this.totalRecord % this.pageSize !=0){
                this.totalPage = this.totalPage + 1;
            }

            // 当前第几页数据  如果参数pageNum超过最大页数
            this.currentPage = this.totalPage < pageNum ?  this.totalPage : pageNum;

            // 起始索引
            int fromIndex    = this.pageSize * (this.currentPage -1);

            // 结束索引
            int toIndex  = this.pageSize * this.currentPage > this.totalRecord ? this.totalRecord : this.pageSize * this.currentPage;

            this.dataList = sourceList.subList(fromIndex, toIndex);
        }
        //空构造器
        public AllPageUtil(){

        }
        //构造器
        public AllPageUtil(int pageSize, int currentPage, int totalRecord, int totalPage,
                           List<T> dataList) {
            super();
            this.pageSize = pageSize;
            this.currentPage = currentPage;
            this.totalRecord = totalRecord;
            this.totalPage = totalPage;
            this.dataList = dataList;
        }
        /*
         * Getter 和  Setter 方法
         */
        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalRecord() {
            return totalRecord;
        }

        public void setTotalRecord(int totalRecord) {
            this.totalRecord = totalRecord;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<T> getDataList() {
            return dataList;
        }

        public void setDataList(List<T> dataList) {
            this.dataList = dataList;
        }


}
