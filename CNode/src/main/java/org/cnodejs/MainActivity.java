package org.cnodejs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends ActionBarActivity {

    private SwipeRefreshLayout swipingLayout;
    private RecyclerView topicsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSwipingLayout();
        setupTopicsView();
    }

    private void setupSwipingLayout() {
        swipingLayout = (SwipeRefreshLayout) findViewById(R.id.swiping);
    }

    private void setupTopicsView() {
        topicsView = (RecyclerView) findViewById(R.id.topics);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        topicsView.setLayoutManager(layoutManager);
    }

}
