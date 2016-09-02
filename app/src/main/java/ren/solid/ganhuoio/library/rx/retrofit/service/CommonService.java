package ren.solid.ganhuoio.library.rx.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by _SOLID
 * Date:2016/7/27
 * Time:14:53
 */
public interface CommonService {
    String BASE_URL = "http://www.example.com/";//这个不重要，可以随便写，但是必须有

    @GET
    Observable<ResponseBody> loadString(@Url String url);

    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String url);


    /**
     * @Field:Post传递的参数
     * @FormUrlEncoded：如果POST请求，传递数据，必须要有
     */
//    @FormUrlEncoded
//    @POST
//    Observable<ResponseBody> loadString(@Url String url);


}
