/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package com.du.android.recyclerview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.du.android.recyclerview.DragDropTouchListener;
import com.du.android.recyclerview.ItemTouchListenerAdapter;
import com.du.android.recyclerview.RecyclerViewAdapter;
import com.du.android.recyclerview.SwipeToDismissTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DemoActivity extends Activity
        implements ItemTouchListenerAdapter.RecyclerViewOnItemClickListener, ActionMode.Callback {


    private ActionMode actionMode;
    private RecyclerViewAdapterImpl adapter;
    private DragDropTouchListener dragDropTouchListener;

    private SwipeToDismissTouchListener swipeToDismissTouchListener;

    public class DemoModel {

        public String text;
        public long id;

        public DemoModel(String text) {
            this.text = text;
        }
    }

    public class DemoViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public DemoViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.demo_item_text);

        }
    }


    public class RecyclerViewAdapterImpl extends RecyclerViewAdapter<DemoModel, DemoViewHolder> {

        List<DemoModel> items = new ArrayList<DemoModel>();


        @Override
        public void swapPositions(int from, int to) {
            Collections.swap(items, from, to);
        }

        RecyclerViewAdapterImpl() {
            setHasStableIds(true);
            for (int i = 0; i < 20; i++) {
                DemoModel demoModel = new DemoModel("Test " + i);
                demoModel.id = i;
                items.add(demoModel);
            }

        }


        @Override
        public DemoViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.demo_item, viewGroup, false);
            return new DemoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DemoViewHolder viewHolder, final int position) {
            super.onBindViewHolder(viewHolder, position);
            DemoModel model = items.get(position);
            viewHolder.text.setText(model.text);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }


        @Override
        public long getItemId(int position) {
            if (position < 0 || position >= items.size()) {
                return -1;
            }
            return items.get(position).id;

        }


        public void removeItem(int pos) {
            items.remove(pos);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        this.adapter = new RecyclerViewAdapterImpl();
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView, this));


        swipeToDismissTouchListener = new SwipeToDismissTouchListener(recyclerView, new SwipeToDismissTouchListener.DismissCallbacks() {

            @Override
            public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
                return SwipeToDismissTouchListener.SwipeDirection.RIGHT;
            }

            @Override
            public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
                for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
                    adapter.removeItem(data.position);
                    adapter.notifyItemRemoved(data.position);
                }
            }
        });


        recyclerView.addOnItemTouchListener(swipeToDismissTouchListener);


        dragDropTouchListener = new DragDropTouchListener(recyclerView, this) {
            @Override
            protected void onItemSwitch(RecyclerView recyclerView, int from, int to) {
                adapter.swapPositions(from, to);
                adapter.clearSelection(from);
                adapter.notifyItemChanged(to);
                if(actionMode!=null) actionMode.finish();
            }

            @Override
            protected void onItemDrop(RecyclerView recyclerView, int position) {
            }
        };

        recyclerView.addOnItemTouchListener(dragDropTouchListener);
    }

    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {
        Log.d("", "onItemClick()");
        if (this.actionMode != null) {
            toggleSelection(position);
        }
    }


    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        actionMode.setTitle("Selected " + adapter.getSelectedItemCount());
    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        Log.d("", "onItemLongClick()");
        startActionMode(this);
        toggleSelection(position);
        dragDropTouchListener.startDrag();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.menu_am, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        swipeToDismissTouchListener.setEnabled(false);
        this.actionMode = actionMode;
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        swipeToDismissTouchListener.setEnabled(true);
        adapter.clearSelections();
        this.actionMode = null;

    }


}
