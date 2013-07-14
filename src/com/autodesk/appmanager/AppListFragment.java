package com.autodesk.appmanager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.autodesk.appmanager.dummy.DummyContent;

/**
 * A list fragment representing a list of Apps. This fragment also supports tablet devices by allowing list items to be
 * given an 'activated' state upon selection. This helps indicate which item is currently being viewed in a
 * {@link AppDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks} interface.
 */
public class AppListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the activated item position. Only used on
     * tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must implement. This mechanism allows
     * activities to be notified of item selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does nothing. Used only when this fragment is not
     * attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation
     * changes).
     */
    public AppListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App[] apps = new App[] { /*
                                  * new App("com.sketchbook", "SBM"), new App("com.adsk.sketchbook.galaxy",
                                  * "SBM Samsung"), new App("com.adsk.sketchbookhd", "SBP"), new
                                  * App("com.adsk.sketchbookhd.galaxy", "SBP Samsung"), new App("pixlr.OMatic",
                                  * "Pixlr OMatic"),
                                  */
        new App("com.pixlr.express", "Pixlr Express") };

        PackageManager pm = this.getActivity().getPackageManager();
        for (App app : apps) {
            try {
                PackageInfo pInfo = pm.getPackageInfo(app.getPackageName(), PackageManager.GET_SIGNATURES);
                app.setInfo(pm, pInfo);
                AppManager.getInstance().install(this.getActivity(), app,
                        Environment.getExternalStorageDirectory() + "/Download/Pixlr_Express_1.2.apk");
            } catch (NameNotFoundException e) {
            }
        }

        setListAdapter(new AppListAdapter(getActivity(), 0, apps));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated' state when
     * touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private static class ViewHolder {
        private CheckBox selection;
        private TextView name;
        private ImageView icon;
        private ImageView status;
    }

    public class AppListAdapter extends ArrayAdapter<App> {

        public AppListAdapter(Context context, int textViewResourceId, App[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflator = AppListFragment.this.getActivity().getLayoutInflater();
                View view = inflator.inflate(R.layout.fragment_app_list_item, null);

                holder = new ViewHolder();
                holder.icon = (ImageView) view.findViewById(R.id.icon);
                holder.selection = (CheckBox) view.findViewById(R.id.selection);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.status = (ImageView) view.findViewById(R.id.status);

                view.setTag(holder);

                convertView = view;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            App app = getItem(position);
            holder.name.setText(app.getDisplayName());
            holder.icon.setImageDrawable(app.getIcon());

            return convertView;
        }

    }
}
