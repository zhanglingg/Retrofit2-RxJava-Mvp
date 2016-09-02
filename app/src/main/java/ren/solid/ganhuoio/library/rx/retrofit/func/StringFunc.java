package ren.solid.ganhuoio.library.rx.retrofit.func;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * Created by _SOLID
 * Date:2016/7/27
 * Time:20:54
 */
public class StringFunc implements Func1<ResponseBody, String> {
    @Override
    public String call(ResponseBody responseBody) {
        String result = null;
        try {

            result = responseBody.string();
            Log.e("result", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
