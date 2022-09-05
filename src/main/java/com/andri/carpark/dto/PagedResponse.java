package com.andri.carpark.dto;

import java.io.Serializable;
import java.util.List;

public class PagedResponse<T> implements Serializable {

    private List<T> data;
    private int page;
    private int perPage;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponse() {

    }

    public PagedResponse(List<T> data, int page, int perPage, long totalElements, int totalPages, boolean last) {
        this.data = data;
        this.page = page;
        this.perPage = perPage;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> content) {
        this.data = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
