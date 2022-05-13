package itts.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.huawei.hms.ml.scan.HmsScan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    // creating variables for request queue, array list, progressbar, edittext,
    // image button and recycler view.
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //initializing views.
        progressBar = findViewById(R.id.idLoadingPB);
        searchEdt = findViewById(R.id.idEdtSearchBooks);
        searchBtn = findViewById(R.id.idBtnSearch);

        Intent intent1 = getIntent();
        String hasil = intent1.getStringExtra("isbn_scan");
        searchEdt.setText(hasil);

        //initializing on click listener for search button.
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //checking if edittext field is empty or not.
                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Please enter search query");
                    return;
                }
                //if the search query is not empty then calling get book info method to load
                //all the books from the API.
                getBooksInfo(searchEdt.getText().toString());
            }
        });
    }

    private void getBooksInfo(String query) {
        //creating a new array list.
        bookInfoArrayList = new ArrayList<>();
        //below line is use to initialize the variable for request queue.
        mRequestQueue = Volley.newRequestQueue(SecondActivity.this);
        //below line is use to clear cache this will be use when the data is being updated.
        mRequestQueue.getCache().clear();
        //below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        //below line is creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);

        //below line is use to make json object request inside passing url,
        //get method and getting json object.
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                //inside on response method extracting all json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");

                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String subtitle = volumeObj.optString("subtitle");
                        //JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        JSONObject imageLinks2 = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = null;
                        if (imageLinks2 != null) {
                            thumbnail = imageLinks2.optString("thumbnail");
                        }
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");
                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                        String buyLink = saleInfoObj.optString("buyLink");
                       /* ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }*/
                        //after extracting all the data then saving this data in modal class.
                        BookInfo bookInfo = new BookInfo(title, subtitle, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink); //authorsArrayList,
                        //below line is use to pass modal class in array list.
                        bookInfoArrayList.add(bookInfo);
                        //below line is use to pass array list in adapter class.
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, SecondActivity.this);
                        //below line is use to add linear layout manager for recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SecondActivity.this, RecyclerView.VERTICAL, false);
                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
                        //below line is to setting layout manager and adapter to recycler view.
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //displaying a toast message when we get any error from API
                    Toast.makeText(SecondActivity.this, "Sorry, Book Not Found :(", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //also displaying error message in toast.
                Toast.makeText(SecondActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        //adding json object request in request queue.
        queue.add(booksObjrequest);

    }
}