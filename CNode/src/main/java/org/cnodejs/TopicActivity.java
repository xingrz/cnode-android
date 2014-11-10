package org.cnodejs;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.cnodejs.api.Constants;
import org.cnodejs.api.GsonRequest;
import org.cnodejs.api.model.TopicContent;

public class TopicActivity extends ActionBarActivity {

    private static final String TAG = "TopicActivity";

    private RecyclerView repliesView;

    private Toolbar toolbar;
    private int[] primary = new int[3];

    private float transHeight;
    private float scrolled;

    private TopicRepliesAdapter repliesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topic);

        int color = getResources().getColor(R.color.primary);
        primary[0] = Color.red(color);
        primary[1] = Color.green(color);
        primary[2] = Color.blue(color);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        repliesAdapter = new TopicRepliesAdapter(this);

        repliesView = (RecyclerView) findViewById(R.id.replies);
        repliesView.setLayoutManager(new LinearLayoutManager(this));
        repliesView.setAdapter(repliesAdapter);
        repliesView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                handleScroll(dy);
            }
        });

        String id = getIntent().getStringExtra("id");

        new GsonRequest<TopicContent>(
                Request.Method.GET, TopicContent.class, "/topic/%s", id) {
            @Override
            protected void deliverResponse(TopicContent response) {
                toolbar.setTitle(response.data.title);
                toolbar.setTitleTextColor(Color.TRANSPARENT);
                repliesAdapter.setTopic(response.data);
            }

            @Override
            public void deliverError(VolleyError error) {
                Log.e(TAG, "error loading replies", error);
                Toast.makeText(TopicActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
            }
        }.enqueue(Volley.newRequestQueue(this));
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

    // TODO: 需要封装
    private void handleScroll(int delta) {
        if (transHeight == 0) {
            TopicRepliesAdapter.ViewHolder holder
                    = (TopicRepliesAdapter.ViewHolder) repliesView.findViewHolderForPosition(0);

            if (holder == null) {
                return;
            }

            View title = holder.title;
            transHeight = title.getTop() + title.getHeight();

            return;
        }

        scrolled += delta;
        if (scrolled < 0) {
            scrolled = 0;
        }

        if (scrolled < transHeight) {
            toolbar.setBackgroundColor(color(scrolled / transHeight));
        } else if (scrolled >= 0) {
            toolbar.setBackgroundColor(color(1));
        }

        if (scrolled < transHeight / 2) {
            toolbar.setTitleTextColor(Color.TRANSPARENT);
        } else if (scrolled < transHeight) {
            float alpha = (scrolled - transHeight) / (transHeight / 2);
            toolbar.setTitleTextColor(Color.argb((int) (alpha * 0xff), 0, 0, 0));
        } else {
            toolbar.setTitleTextColor(Color.BLACK);
        }

        if (scrolled >= transHeight || delta < 0) {
            float y = toolbar.getY() - delta / 2;

            if (y > 0) {
                y = 0;
            }

            if (y < -toolbar.getHeight()) {
                y = -toolbar.getHeight();
            }

            toolbar.setY(y);
        }
    }

    private void openInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.BASE + "/topic/" + getIntent().getStringExtra("id")));
        startActivity(intent);
    }

    private int color(float alpha) {
        return Color.argb((int) (alpha * 0xff), primary[0], primary[1], primary[2]);
    }

}
