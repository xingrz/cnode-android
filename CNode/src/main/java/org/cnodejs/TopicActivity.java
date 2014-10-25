package org.cnodejs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.cnodejs.api.Constants;
import org.cnodejs.api.GsonRequest;
import org.cnodejs.api.model.TopicContent;

public class TopicActivity extends ActionBarActivity {

    private static final String TAG = "TopicActivity";

    private TopicRepliesAdapter repliesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_topic);

        repliesAdapter = new TopicRepliesAdapter(this);

        RecyclerView repliesView = (RecyclerView) findViewById(R.id.replies);
        repliesView.setLayoutManager(new LinearLayoutManager(this));
        repliesView.setAdapter(repliesAdapter);

        String id = getIntent().getStringExtra("id");

        Volley.newRequestQueue(this).add(new GsonRequest<TopicContent>(
                Request.Method.GET, Constants.API_V1 + "/topic/" + id, TopicContent.class,
                new Response.Listener<TopicContent>() {
                    @Override
                    public void onResponse(TopicContent response) {
                        repliesAdapter.setTopic(response.data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error loading replies", error);
                        Toast.makeText(
                                TopicActivity.this,
                                R.string.error_loading,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.open_in_browser:
                openInBrowser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.BASE + "/topic/" + getIntent().getStringExtra("id")));
        startActivity(intent);
    }

}
