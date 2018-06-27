package com.yulin.origin.business.recent.response;

import com.yulin.origin.network.NetworkManager;
import com.yulin.origin.network.service.PoemService;

import java.util.List;

import io.reactivex.Observable;

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

    public static Observable<PoemListResponse> providePoems() {
        return NetworkManager.getInstance().getService(PoemService.class).getPoems();
    }

}
