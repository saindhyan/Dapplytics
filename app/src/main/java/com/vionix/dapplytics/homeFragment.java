package com.vionix.dapplytics;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.vionix.dapplytics.R.drawable.clearsky;
import static com.vionix.dapplytics.R.drawable.floodmp;
import static com.vionix.dapplytics.R.drawable.lightrain;
import static com.vionix.dapplytics.R.drawable.moderaterain;
import static com.vionix.dapplytics.R.drawable.overcastclouds;
import static com.vionix.dapplytics.R.drawable.scatteredcloud;
import static com.vionix.dapplytics.R.drawable.ukf;


public class homeFragment extends Fragment {

    AutoCompleteTextView autoCompleteTextView;
    Animation tabOpen, tabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    Button city;
    private RecyclerView nr;
    private List<headingmodel> headingmodelList = new ArrayList<>();
    private int pagenumber = 1, per_page = 20;
    String cl = "";
    private static  String API_KEY="b971358de21d4af48ae24b5faf06bbok";
    private static String TAG="MainActivity";
    private ArrayList<NewsArticle> mArticleList;
    private ArticleAdapter mArticleAdapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    String cordinate;


//    private String apikey = "67df503444f54f2e8d521e0de23fbc2e";
//    private String headlineurl = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=" + apikey;

    FusedLocationProviderClient fusedLocationProviderClient;
    FloatingActionButton sm, nh, cmp, floatingActionButton;

    ImageView wimg,map;
    TextView tvResult, nht, smt, cmpt, dangertittle,pronearea;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    DecimalFormat df = new DecimalFormat("#.##");


    private headlinesadapter headlinesadapter;


    @Override
    public void onResume() {
        super.onResume();
        getWeatherDetails();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();
            getlocation2();
            getWeatherDetails();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }
    }

//    private void fetchData() {
//        StringRequest request = new StringRequest(Request.Method.GET, headlineurl+"&page="+ pagenumber+"&per_page="+per_page,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            JSONArray jsonArray = jsonObject.getJSONArray("articles");
//                            int length = jsonArray.length();
//
//                            for (int i = 0; i < length; i++) {
//
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                String source="mysource";
//
////                                JSONObject s1 = jsonObject1.getJSONObject("source");
////                                String source = s1.getString("name");
//
//
//                                String title = jsonObject1.getString("title");
//
//                                String desp = jsonObject1.getString("description");
//
//                                headingmodelList.add(new headingmodel(title, desp, source));
//                            }
//                            headlinesadapter.notifyDataSetChanged();
//                            pagenumber++;
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), "Data Not Found !!!", Toast.LENGTH_SHORT).show();
//            }
//
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> map= new HashMap<>();
//                map.put("key",apikey);
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(request);
//
//    }

    private void getWeatherDetails() {


        String tempUrl = "";
        String location = autoCompleteTextView.getText().toString().trim();

        tempUrl = url + "?q=" + location + "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    tvResult.setTextColor(Color.rgb(68, 134, 199));
                    output += "Current weather of " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";

                    if (output.contains("clear sky")) {
                        wimg.setImageResource(clearsky);

                    } else if (output.contains("overcast clouds")) {
                        wimg.setImageResource(overcastclouds);

                    } else if (output.contains("scattered clouds")) {
                        wimg.setImageResource(scatteredcloud);

                    } else if (output.contains("few clouds")) {
                        wimg.setImageResource(scatteredcloud);

                    } else if (output.contains("moderate rain")) {
                        wimg.setImageResource(moderaterain);

                    }else if (output.contains("light rain")) {
                        wimg.setImageResource(lightrain);

                    } else {

                    }
                    tvResult.setText(output);

                    if (output.contains("Uttarakhand")){
                        dangertittle.setText("Flood ( HIGH ALERT )");
                        pronearea.setText("Chamoli\n Haridwar\n Nainital\n Pithoragarh\n Uttarkashi");
                        map.setImageResource(ukf);
                        map.setVisibility(View.VISIBLE);



                    }else if (output.contains("Madhya Pradesh")){
                        dangertittle.setText("Flood");
                        pronearea.setText("Rewa\n Raisen\n Mandla\n Seoni\n Sehore\n Sagar\n Damoh\n Tikamgarh \nand Agar-Malwa");
                        map.setImageResource(floodmp);
                        map.setVisibility(View.VISIBLE);

                    }else{
                        dangertittle.setText("NO DATA FOUND");
                        pronearea.setText("NO DATA FOUND");
                        map.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getlocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        autoCompleteTextView.setText(addresses.get(0).getAdminArea());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);


        tvResult = view.findViewById(R.id.tvResult);
        nht = view.findViewById(R.id.nht);
        wimg = view.findViewById(R.id.wimg);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        sm = view.findViewById(R.id.sm);
        nh = view.findViewById(R.id.nh);
        cmpt = view.findViewById(R.id.cmpt);
        smt = view.findViewById(R.id.smt);
        cmp = view.findViewById(R.id.cmp);
        city = view.findViewById(R.id.city);
        dangertittle = view.findViewById(R.id.dangertittle);
        pronearea = view.findViewById(R.id.pronareas);
        map = view.findViewById(R.id.map);

        nr = view.findViewById(R.id.recyclerview_id);



//        AndroidNetworking.initialize(getContext());
//
//        // setting the JacksonParserFactory
//        AndroidNetworking.setParserFactory(new JacksonParserFactory());
//
//        // assigning views to their ids
//        nr=view.findViewById(R.id.recyclerview_id);
//
//        // setting the recyclerview layout manager
//        nr.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // initializing the ArrayList of articles
//        mArticleList=new ArrayList<>();
//
//        // calling get_news_from_api()
//        get_news_from_api();
////        fetchData();
        headingmodelList.add(new headingmodel(getString(R.string.nh1), getString(R.string.ndesc1), getString(R.string.npb1)));
        headingmodelList.add(new headingmodel(getString(R.string.nh2), getString(R.string.ndesc2), getString(R.string.npb2)));
        headingmodelList.add(new headingmodel(getString(R.string.nh3), getString(R.string.ndesc3), getString(R.string.npb3)));
        headingmodelList.add(new headingmodel(getString(R.string.nh4), getString(R.string.ndesc4), getString(R.string.npb4)));
//        headingmodelList.add(new headingmodel("heading 1","fghjkjhhvgv","piyush"));
//        headingmodelList.add(new headingmodel("heading 1","fghjkjhhvgv","piyush"));

        headlinesadapter = new headlinesadapter(headingmodelList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        nr.setLayoutManager(linearLayoutManager);
        headlinesadapter = new headlinesadapter(headingmodelList, getContext());
        nr.setAdapter(headlinesadapter);
        headlinesadapter.notifyDataSetChanged();

        tabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.tab_open);
        tabClose = AnimationUtils.loadAnimation(getContext(), R.anim.tab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(),
                R.array.state,
                R.layout.dropdown_style);
        autoCompleteTextView.setAdapter(adapter);
        TextInputLayout tls = view.findViewById(R.id.tls);


        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), weatherforcast.class);
                startActivity(i);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animatetab();
            }
        });
        sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animatetab();
                Intent i = new Intent(getActivity(), safety.class);
                startActivity(i);

            }
        });
        nh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendsms();
                animatetab();

            }
        });
        cmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animatetab();
                Intent i = new Intent(getActivity(), camps.class);
                startActivity(i);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        tls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });

        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getWeatherDetails();
            }

            @Override
            public void afterTextChanged(Editable s) {
                getWeatherDetails();
            }
        });
        return view;

    }
    private void getlocation2() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        cordinate=addresses.get(0).getAddressLine(0);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private void sendsms() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
     // Use the Builder class for convenient dialog construction
            AlertDialog alertDialog1 = new AlertDialog.Builder(
                    getContext()).create();

            // Setting Dialog Title
            alertDialog1.setTitle("NEED HELP?");

            // Setting Dialog Message
            alertDialog1.setMessage("YOU WANT TO SEND HELP MESSAGE?");

            // Setting Icon to Dialog

            // Setting OK Button
            alertDialog1.setButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog
                    // closed

                    database.getReference().child("number").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                    String phone = snapshot.getValue().toString();
                                    String message = "I Need Help \nLocation - " + cordinate + "\n\n\n from dapplytics";
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(phone, null, message, null, null);
                                Toast.makeText(getContext(), "SMS SENT", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });

            // Showing Alert Message
            alertDialog1.show();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 44);

        }

    }

    private void animatetab() {
        if (isOpen) {
            floatingActionButton.startAnimation(rotateForward);
            sm.startAnimation(tabClose);
            nh.startAnimation(tabClose);
            cmpt.startAnimation(tabClose);
            smt.startAnimation(tabClose);
            nht.startAnimation(tabClose);
            cmp.startAnimation(tabClose);
            sm.setClickable(false);
            nh.setClickable(false);
            cmp.setClickable(false);
            isOpen = false;
        } else {
            floatingActionButton.startAnimation(rotateBackward);
            sm.startAnimation(tabOpen);
            nh.startAnimation(tabOpen);
            smt.startAnimation(tabOpen);
            cmpt.startAnimation(tabOpen);
            nht.startAnimation(tabOpen);
            cmp.startAnimation(tabOpen);
            sm.setClickable(true);
            nh.setClickable(true);
            cmp.setClickable(true);
            isOpen = true;
        }
    }

    public void get_news_from_api(){
        // clearing the articles list before adding news ones
        mArticleList.clear();

        // Making a GET Request using Fast
        // Android Networking Library
        // the request returns a JSONObject containing
        // news articles from the news api
        // or it will return an error
        AndroidNetworking.get("https://newsapi.org/v2/top-headlines")
                .addQueryParameter("country", "in")
                .addQueryParameter("apiKey",API_KEY)
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        // disabling the progress bar

                        // handling the response
                        try {

                            // storing the response in a JSONArray
                            JSONArray articles=response.getJSONArray("articles");

                            // looping through all the articles
                            // to access them individually
                            for (int j=0;j<articles.length();j++)
                            {
                                // accessing each article object in the JSONArray
                                JSONObject article=articles.getJSONObject(j);

                                // initializing an empty ArticleModel
                                NewsArticle currentArticle=new NewsArticle();

                                // storing values of the article object properties
                                String author=article.getString("author");
                                String title=article.getString("title");
                                String description=article.getString("description");
                                String url=article.getString("url");
                                String urlToImage=article.getString("urlToImage");
                                String publishedAt=article.getString("publishedAt");
                                String content=article.getString("content");

                                // setting the values of the ArticleModel
                                // using the set methods
                                currentArticle.setAuthor(author);
                                currentArticle.setTitle(title);
                                currentArticle.setDescription(description);
                                currentArticle.setUrl(url);
                                currentArticle.setUrlToImage(urlToImage);
                                currentArticle.setPublishedAt(publishedAt);
                                currentArticle.setContent(content);

                                // adding an article to the articles List
                                mArticleList.add(currentArticle);
                            }

                            // setting the adapter
                            mArticleAdapter=new ArticleAdapter(getContext(),mArticleList);
                            nr.setAdapter(mArticleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // logging the JSONException LogCat
                            Log.d(TAG,"Error : "+e.getMessage());
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // logging the error detail and response to LogCat
                        Log.d(TAG,"Error detail : "+error.getErrorDetail());
                        Log.d(TAG,"Error response : "+error.getResponse());
                    }
                });
    }

}