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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

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
                        dangertittle.setText("Flood");
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

        nr = view.findViewById(R.id.nr);
//        fetchData();
        headingmodelList.add(new headingmodel("Uttarakhand Braces for Intense Weather; Extremely Heavy Rains In Forecast on October 18", "Saturday, October 16: Intense weather activity is set to drench Uttarakhand this Sunday and Monday, October 17-18, as heavy showers have been forecast across the state.", "TWC Indi"));
        headingmodelList.add(new headingmodel("उत्तराखंड: भारी बारिश का अलर्ट, सभी जिलों में सोमवार को बंद रहेंगे स्कूल और आंगनबाड़ी केंद्र ", "Uttarakhand weather Update News: 18 अक्तूबर को उत्तरकाशी, चमोली, रुद्रप्रयाग, पिथौरागढ़, बागेश्वर, अल्मोड़ा, नैनीताल, चंपावत, देहरादून, टिहरी व पौड़ी के कई और हरिद्वार व ऊधमसिंह नगर के कुछ इलाकों में भारी से बहुत भारी बारिश हो सकती है।", "अमर उजाला नेटवर्क, देहरादून"));
        headingmodelList.add(new headingmodel("Uttarakhand Weather Update News: भारी बारिश के अलर्ट के बाद केदारनाथ यात्रा दो दिन के लिए अस्थाई रूप से रोकी", "मौसम विभाग द्वारा 17 अक्तूबर से दो-तीन दिन तक चारधाम सहित अधिकांश पर्वतीय क्षेत्रों में भारी बारिश की चेतावनी जारी की गई है। जिसको देखते हुए प्रशासन ने केदारनाथ, गंगोत्री और यमुनोत्री धाम की यात्रा रोक दी है। ", "न्यूज डेस्क"));
        headingmodelList.add(new headingmodel(" IMD predicts heavy rain, hailstorm in Kerala, 16 other states, UTs. Full forecast till Oct 19", "A western disturbance lies as a cyclonic circulation over southern parts of Afghanistan and nearby areas in lower levels, leading to rainfall and hailstorm in several parts of India. (HT_PRINT)", "mint"));
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
                    String cordinates = "coer collage";
                    String phone = "+91 9675028282";
                    String message = "I Need Help" + cordinates + "from dapplytics";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, message, null, null);
                    Toast.makeText(getContext(), "SMS SENT", Toast.LENGTH_SHORT).show();

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


}