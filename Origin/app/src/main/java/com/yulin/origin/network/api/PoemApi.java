package com.yulin.origin.network.api;

import com.yulin.io.retrofit.response.DefResponse;
import com.yulin.origin.business.category.response.CategoryResponse;
import com.yulin.origin.business.content.response.PoemContentResponse;
import com.yulin.origin.business.recent.response.PoemListResponse;
import com.yulin.origin.network.NetConstant;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by liu_lei on 2017/6/29.
 *
 */

public interface PoemApi {

    @GET(NetConstant.API.POEMS)
    Observable<DefResponse<PoemListResponse>> getPoems();

    // 首页 - 分类
    @GET(NetConstant.API.CATEGORY)
    Observable<DefResponse<CategoryResponse>> getCategory();

    // 获取诗词详情
    @GET(NetConstant.API.POEM_CONTENT)
    Observable<DefResponse<PoemContentResponse>> getPoemContent();

}
