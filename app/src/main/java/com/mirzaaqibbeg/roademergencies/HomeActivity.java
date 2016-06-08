package com.mirzaaqibbeg.roademergencies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mirzaaqibbeg.roademergencies.DataStructure.Advert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        imageView = (ImageView) findViewById(R.id.imageView);

        DownAdvert advert = new DownAdvert();
        advert.execute();
    }

    public void openMechanicActivity(View view) {
        Intent intent = new Intent(this, MechanicActivity.class);
        startActivity(intent);
    }

    public void openMedCare(View view) {
        Intent intent = new Intent(this, MedcareActivity.class);
        startActivity(intent);
    }

    public void openRestaurantBAr(View view) {
        Intent intent = new Intent(this, REstaurant.class);
        startActivity(intent);
    }

    public void openGasFuel(View view) {
        Intent intent = new Intent(this, GasFuel.class);
        startActivity(intent);
    }

    public void quickConnect(View view) {
        Intent intent = new Intent(this, SmsEditor.class);
        startActivity(intent);
    }

    class DownAdvert extends AsyncTask<String, Intent, ArrayList<Advert>> {

        ArrayList<Advert> arrayList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
        }

        @Override
        protected ArrayList<Advert> doInBackground(String... params) {
            try {
                String data = DownloadJsonContent.downloadContent(Fields.URL_ADVERTISEMENT);
                JSONObject object = new JSONObject(data);
                JSONArray array = object.getJSONArray("advertisement");
                for (int i = 0; i < array.length(); i++) {
                    Advert advert = new Advert();
                    JSONObject jsonObject = array.getJSONObject(i);
                    String url = jsonObject.getString("imageurl");
                    String redirectUrl = jsonObject.getString("redirecturl");
                    advert.setImageURL(url);
                    advert.setRedirectURL(redirectUrl);
                    arrayList.add(advert);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return arrayList;
        }

        @Override
        protected void onPostExecute(final ArrayList<Advert> adverts) {
            super.onPostExecute(adverts);


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (adverts.size() > 0) {
                        int l = adverts.size();
                        int incr = 0;
                        while (true) {
                            if (incr == l - 1) {
                                incr = 0;
                            } else {
                                incr = incr + 1;
                            }
                            final int finalIncr = incr;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!HomeActivity.this.isDestroyed()) {


                                        Glide
                                                .with(HomeActivity.this)
                                                .load(adverts.get(finalIncr).getImageURL())
                                                .override(1000,300)
                                                .into(imageView);
                                    }
                                }
                            });
                            Log.e(Fields.GAS, adverts.get(0).getImageURL());
                            synchronized (this) {
                                try {
                                    this.wait(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            });
            thread.start();


        }
    }
}
