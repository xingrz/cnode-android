package org.cnodejs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.cnodejs.api.Constants;
import org.cnodejs.api.GsonRequest;
import org.cnodejs.api.model.TopicContent;
import org.cnodejs.util.ImageLoader;

public class TopicActivity extends ActionBarActivity {

    private static final String TAG = "TopicActivity";

    private TopicRepliesAdapter repliesAdapter;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));

        setContentView(R.layout.activity_topic);

        ListView repliesView = (ListView) findViewById(R.id.replies);
        addHeaderViewTo(repliesView);

        repliesAdapter = new TopicRepliesAdapter(this);
        repliesView.setAdapter(repliesAdapter);

        queue = Volley.newRequestQueue(this);

        loadReplies(getIntent().getStringExtra("id"));
    }

    private void addHeaderViewTo(ListView repliesView) {
        Intent intent = getIntent();
        View topicHeaderView = getLayoutInflater().inflate(R.layout.topic_header, repliesView, false);

        ((TextView) topicHeaderView.findViewById(R.id.user)).setText(intent.getStringExtra("user"));
        ((TextView) topicHeaderView.findViewById(R.id.title)).setText(intent.getStringExtra("title"));
        ((TextView) topicHeaderView.findViewById(R.id.content)).setText(intent.getStringExtra("content"));
        ImageLoader.load((ImageView) topicHeaderView.findViewById(R.id.avatar), intent.getStringExtra("avatar"));

        repliesView.addHeaderView(topicHeaderView, null, false);
    }

    private void loadReplies(String id) {
        queue.add(new GsonRequest<TopicContent>(
                Request.Method.GET, Constants.API_V1 + "/topic/" + id, TopicContent.class,
                new Response.Listener<TopicContent>() {
                    @Override
                    public void onResponse(TopicContent response) {
                        repliesAdapter.addAll(response.data.replies);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
