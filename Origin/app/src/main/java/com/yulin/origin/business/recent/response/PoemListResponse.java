package com.yulin.origin.business.recent.response;

import java.util.List;

/**
 * Created by liu_lei on 2017/5/29.
 */

public class PoemListResponse {

    private List<PoemItemResponse> poems;

    public PoemListResponse(List<PoemItemResponse> poems) {
        this.poems = poems;
    }

    public PoemListResponse() {
    }

    public List<PoemItemResponse> getPoems() {
        return poems;
    }

    public void setPoems(List<PoemItemResponse> poems) {
        this.poems = poems;
    }

    @Override
    public String toString() {
        return "PoemListResponse{" +
                "poems=" + poems +
                '}';
    }

}
