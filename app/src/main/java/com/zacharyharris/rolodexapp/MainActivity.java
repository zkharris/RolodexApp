package com.zacharyharris.rolodexapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private ProgressDialog pDialog;
    public int currentJSONIndex = 0;
    public Card card;
    public int cardsListSize;
    public int randomNum;

    public TextView lastNameTextView;
    public TextView firstNameTextView;
    public TextView emailTextView;
    public TextView companyTextView;
    public TextView startDateTextView;
    public TextView bioTextView;
    public ImageView avatarView;

    public Button nextButton;
    public Button prevButton;

    // URL to get contacts JSON
    private static String url = "https://s3-us-west-2.amazonaws.com/udacity-mobile-interview/CardData.json";

    ArrayList<Card> cardList;
    List<String> colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardList = new ArrayList<>();

        lastNameTextView = (TextView) findViewById(R.id.lastNameInsert);
        firstNameTextView = (TextView) findViewById(R.id.firstNameInsert);
        emailTextView = findViewById(R.id.emailInsert);
        companyTextView = findViewById(R.id.companyInsert);
        startDateTextView = findViewById(R.id.startDateInsert);
        bioTextView = findViewById(R.id.bioInsert);
        avatarView = findViewById(R.id.avatarImageView);

        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.previousButton);

        colorList = new ArrayList<String>();
        colorList.add("#d1afff");
        colorList.add("#b2cefe");
        colorList.add("#abe9a1");
        colorList.add("#ffe77f");
        colorList.add("#fc2a2");
        colorList.add("#ff0000");

        Random randomGenerator = new Random();

        randomNum = randomGenerator.nextInt(6);

        View backgroundView = findViewById(R.id.mainView);
        View root = backgroundView.getRootView();
        String randomColor = colorList.get(randomNum);
        backgroundView.setBackgroundColor(Color.parseColor(randomColor));


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextCard(currentJSONIndex);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevCard(currentJSONIndex);
            }
        });

        card = new Card();
        new getCards().execute();
    }

    public void nextCard(int index){
        if(index == cardsListSize - 1){
            currentJSONIndex = 0;
        } else {
            currentJSONIndex++;
        }
        Random randomGenerator = new Random();

        randomNum = randomGenerator.nextInt(6);

        View backgroundView = findViewById(R.id.mainView);
        View root = backgroundView.getRootView();
        String randomColor = colorList.get(randomNum);
        backgroundView.setBackgroundColor(Color.parseColor(randomColor));

        card = new Card();
        new getCards().execute();

    }

    public void prevCard(int index){
        if(index == 0){
            currentJSONIndex = cardsListSize - 1;
        } else {
            currentJSONIndex--;
        }
        Random randomGenerator = new Random();

        randomNum = randomGenerator.nextInt(6);

        View backgroundView = findViewById(R.id.mainView);
        View root = backgroundView.getRootView();
        String randomColor = colorList.get(randomNum);
        backgroundView.setBackgroundColor(Color.parseColor(randomColor));
        card = new Card();
        new getCards().execute();
    }

    private class getCards extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray cardsList = new JSONArray(jsonStr);
                    cardsListSize = cardsList.length();

                    JSONObject currCard = cardsList.getJSONObject(currentJSONIndex);

                    String lastName = currCard.getString("lastName");
                    String firstName = currCard.getString("firstName");
                    String email = currCard.getString("email");
                    String company = currCard.getString("company");
                    String startDate = currCard.getString("startDate");
                    String bio = currCard.getString("bio");
                    String photoURL = currCard.getString("avatar");

                    card.setLastName(lastName);
                    card.setFirstName(firstName);
                    card.setEmail(email);
                    card.setCompany(company);
                    card.setStartDate(startDate);
                    card.setBio(bio);
                    card.setPhotoURL(photoURL);


                    // looping through All Contacts
                    /*for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        cardList.add(contact);
                    }*/


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            firstNameTextView.setText(card.getFirstName());
            lastNameTextView.setText(card.getLastName());
            emailTextView.setText(card.getEmail());
            companyTextView.setText(card.getCompany());
            startDateTextView.setText(card.getStartDate());
            bioTextView.setText(card.getBio());
            Glide.with(MainActivity.this).load(card.getPhotoURL()).into(avatarView);
        }
    }






}
