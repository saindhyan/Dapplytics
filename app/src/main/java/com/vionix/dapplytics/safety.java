package com.vionix.dapplytics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class safety extends AppCompatActivity {
TextView tv1,tv2,tv3,tv4,tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv1.setText("1. Elevate the furnace, water heater, and electric panel if susceptible to flooding\n\n" +
                "2. Install check valves in sewer traps to prevent floodwater from backing up into your home.\n\n" +
                "3. Seal walls in basements with waterproofing compounds to avoid seepage.\n\n" +
                "4. Keep an adequate supply of food, candles and drinking water in case you are trapped inside your home\n\n");
        tv2.setText("1. Listen to designated radio/TV emergency alert systems for emergency instructions.\n\n" +
                "2. Secure/bring in outdoor furniture or other items that might float away and become a potential hazard.\n\n" +
                "3. Move valuable items and papers/documents to upper floors.\n\n");
        tv3.setText("1. Seek higher ground. Do not wait for instructions.\n\n" +
                "2. Be aware of flash flood areas such as canals, streams, drainage channels.\n\n" +
                "3. Be ready to evacuate.\n\n" +
                "4. If instructed, turn off utilities at main switches and unplug appliances - do not touch electrical equipment if wet.\n\n" +
                "5. If you must leave your home, do not walk through moving water. Six inches of moving water can knock you off your feet. Use a stick to test depth.\n\n" +
                "6. Do not try to drive over a flooded road. If your car stalls, abandon it immediately and seek an alternate route.\n\n");
        tv4.setText("1. Stay away from flood water - do not attempt to swim, walk or drive through the area\n\n" +
                "2. Be aware of areas where water has receded. Roadways may have weakened and could collapse.\n\n" +
                "3. Avoid downed power lines and muddy waters where power lines may have fallen.\n\n" +
                "4. Do not drink tap water until advised by the Health Unit that the water is safe to drink.\n\n" +
                "5. Once flood waters have receded you must not live in your home until the water supply has been declared safe for use, all flood-contaminated rooms have been thoroughly cleaned and disinfected, adequate toilet facilities are available, all electrical appliances and heating/cooling systems have been inspected, food, utensils and dishes have been examined, cleaned or disposed of, and floor drains and sumps have been cleaned and disinfected.\n\n");
        tv5.setText("1. Never pour down used water in  drain. Use it to water the plants.\n\n" +
                "2. Replace dripping faucets by replacing washers.\n\n" +
                "3. Check all plumbing for leakages and get the faulty repaired by plumber.\n\n" +
                "4. Take a bath by bucket rather than by shower.\n\n" +
                "5. Use mulch to retain water in the soil. \n");
    }
}