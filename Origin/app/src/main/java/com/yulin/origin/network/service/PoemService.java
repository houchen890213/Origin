package com.yulin.origin.network.service;

import com.yulin.io.retrofit.annotation.ServiceMethod;
import com.yulin.io.retrofit.annotation.ServiceType;
import com.yulin.origin.business.category.response.CategoryResponse;
import com.yulin.origin.business.content.response.PoemContentResponse;
import com.yulin.origin.business.recent.response.PoemListResponse;
import com.yulin.origin.network.api.PoemApi;

import io.reactivex.Observable;

/**
 * Created by liu_lei on 2017/6/22.
 *
 */

public interface PoemService {

    @ServiceType(PoemApi.class)
    @ServiceMethod("getPoems")
    Observable<PoemListResponse> getPoems();

    // 首页 - 分类
    @ServiceType(PoemApi.class)
    @ServiceMethod("getCategory")
    Observable<CategoryResponse> getCategory();

    @ServiceType(PoemApi.class)
    @ServiceMethod("getPoemContent")
    Observable<PoemContentResponse> getPoemContent();

}
