package com.szyz.rock.model;

public class Page {

    private double mid = -1; //分页标识
    private long curPage = 1; //当前页
    private long goPage = -1; // 跳转页
    private int pageSize = 10;
    private long totalPage;
    private long totalSize;

    private Object model; // 额外参数


    //需要跳过的页数
    public long getSkip() {
        if(goPage ==-1)
            return 0;
        if(curPage > goPage){
            return (curPage - goPage -1) * pageSize;
        }else{
            return (goPage - curPage -1) * pageSize;
        }
       // return skip;
    }
    public int getLtOrGt(){
        if (goPage ==-1)
            return 0;
        if(curPage < goPage){
            return -1;
        }
        return 1;
    }


    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }

    public long getGoPage() {
        return goPage;
    }

    public void setGoPage(long goPage) {
        this.goPage = goPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalPage = totalSize%this.pageSize==0?totalSize/this.pageSize:(totalSize/this.pageSize+1);
        this.totalSize = totalSize;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
