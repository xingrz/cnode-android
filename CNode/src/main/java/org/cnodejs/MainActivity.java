package org.cnodejs;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.cnodejs.account.AccountAuthenticator;
import org.cnodejs.api.GsonRequest;
import org.cnodejs.api.model.Topic;
import org.cnodejs.api.model.TopicList;

public class MainActivity extends ActionBarActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    private AccountManager accountManager;

    private ActionBar actionBar;
    private SwipeRefreshLayout swipingLayout;

    private TopicListAdapter topicsAdapter;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setContentView(R.layout.activity_main);

        accountManager = AccountManager.get(this);

        setupSwipingLayout();
        setupTopicsView();
        setupTabs(actionBar);
    }

    private final ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            load();
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };

    private void setupTabs(ActionBar actionBar) {
        createTab(actionBar, R.string.tab_all, "all");
        createTab(actionBar, R.string.tab_share, "share");
        createTab(actionBar, R.string.tab_ask, "ask");
        createTab(actionBar, R.string.tab_job, "job");
    }

    private void createTab(ActionBar actionBar, int title, String tab) {
        actionBar.addTab(actionBar.newTab().setText(title).setTag(tab).setTabListener(tabListener));
    }

    private void setupSwipingLayout() {
        swipingLayout = (SwipeRefreshLayout) findViewById(R.id.swiping);
        swipingLayout.setOnRefreshListener(this);
    }

    private void setupTopicsView() {
        topicsAdapter = new TopicListAdapter(this, new TopicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Topic item) {
                openTopic(item);
            }
        });

        RecyclerView topicsView = (RecyclerView) findViewById(R.id.topics);
        topicsView.setLayoutManager(new LinearLayoutManager(this));
        topicsView.setItemAnimator(new DefaultItemAnimator());
        topicsView.setAdapter(topicsAdapter);
    }

    private void load() {
        ActionBar.Tab selected =  actionBar.getSelectedTab();
        String tab = selected == null ? "all" : (String) selected.getTag();

        swipingLayout.setRefreshing(true);

        new GsonRequest<TopicList>(Request.Method.GET, TopicList.class, "/topics?tab=%s", tab) {
            @Override
            protected void deliverResponse(TopicList response) {
                Log.d(TAG, "loaded " + response.data.size() + " topics");
                swipingLayout.setRefreshing(false);
                if (topicsAdapter.equals(response.data)) {
                    Toast.makeText(MainActivity.this, R.string.no_update, Toast.LENGTH_SHORT).show();
                } else {
                    topicsAdapter.clear();
                    topicsAdapter.addAll(response.data);
                }
            }

            @Override
            public void deliverError(VolleyError error) {
                Log.e(TAG, "error loading topics", error);
                swipingLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
            }
        }.enqueue(queue);
    }

    @Override
    public void onRefresh() {
        load();
    }

    private void openTopic(Topic topic) {
        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra("id", topic.id);
        intent.putExtra("title", topic.title);
        intent.putExtra("content", topic.content);
        intent.putExtra("user", topic.author.loginname);
        intent.putExtra("avatar", topic.author.avatarUrl);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                openAbout();
                return true;
            case R.id.signin:
                signIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAbout() {
        String homepage = "https://github.com/xingrz/cnode-android";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(homepage)));
    }

    private void signIn() {
        accountManager.addAccount(AccountAuthenticator.ACCOUNT_TYPE, null, null, null, this,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Log.d(TAG, String.valueOf(future.isDone()));
                    }
                }, null);
    }

}
