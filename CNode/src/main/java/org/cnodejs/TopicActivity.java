package org.cnodejs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
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
import org.cnodejs.util.Markdown;

public class TopicActivity extends ActionBarActivity {

    private static final String TAG = "TopicActivity";

    private TopicRepliesAdapter repliesAdapter;

    private String id;
    private String title;
    private String content;
    private String user;
    private String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        user = intent.getStringExtra("user");
        avatar = intent.getStringExtra("avatar");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);

        setContentView(R.layout.activity_topic);

        ListView repliesView = (ListView) findViewById(R.id.replies);
        addHeaderViewTo(repliesView);

        repliesAdapter = new TopicRepliesAdapter(this);
        repliesView.setAdapter(repliesAdapter);

        Volley.newRequestQueue(this).add(new GsonRequest<TopicContent>(
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

    private void addHeaderViewTo(ListView repliesView) {
        View topicHeaderView = getLayoutInflater().inflate(R.layout.topic_header, repliesView, false);

        ((TextView) topicHeaderView.findViewById(R.id.user)).setText(user);
        ((TextView) topicHeaderView.findViewById(R.id.title)).setText(title);
        Markdown.render((TextView) topicHeaderView.findViewById(R.id.content), content);
        ImageLoader.load((ImageView) topicHeaderView.findViewById(R.id.avatar), avatar);

        repliesView.addHeaderView(topicHeaderView, null, false);
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
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE + "/topic/" + id));
        startActivity(intent);
    }

}
