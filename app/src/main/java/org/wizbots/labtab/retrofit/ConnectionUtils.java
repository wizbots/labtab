package org.wizbots.labtab.retrofit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class ConnectionUtils {

    private static String TAG = ConnectionUtils.class.getName();

    public static CZResponse execute(Call call) {
        try {
            Response<ResponseBody> response = call.execute();
            int code = response.code();
            if (code == HttpConstant.SC_OK) {
                //return new NDResponse(HttpConstant.SC_OK, new String(readFullyBytes(response.body().byteStream(), 2 * 4096),"UTF-8"));
                return new CZResponse(HttpConstant.SC_OK, getStringFromInputStream(response.body().byteStream()));
            } else {
                return new CZResponse(code, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static byte[] readFullyBytes(InputStream is, int blockSize) {
        byte[] bytes = null;
        if (is != null) {
            try {
                int readed = 0;
                byte[] buffer = new byte[blockSize];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((readed = is.read(buffer)) >= 0) {
                    bos.write(buffer, 0, readed);
                }
                bos.flush();
                bytes = bos.toByteArray();
            } catch (IOException e) {

            }
        }
        return bytes;
    }

    public static String getStringFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String data;
        while ((data = bufferedReader.readLine()) != null) {
            stringBuilder.append(data);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
