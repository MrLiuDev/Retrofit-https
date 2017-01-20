package me.mrlliu.retrofit_https;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


import javax.net.ssl.SSLSocketFactory;


import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv;
    private Button btn;

    private OkHttpClient.Builder client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);

        int[] certificates = new int[]{R.raw.ca};
        SSLSocketFactory socketFactory = MyHttpsFactory.getSSLSocketFactory(this, certificates);
        client = new OkHttpClient.Builder();
        client.sslSocketFactory(socketFactory, MyHttpsFactory.getX509());
    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://mrliu.me/")
                .client(client.build())
                .build();
        IMyService service = retrofit.create(IMyService.class);

        Call<ResponseBody> call = service.getData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    final String string = response.body().string();
                    Log.d("TAG", "onResponse:"+string);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(string);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", t.toString());
            }
        });
    }
}
