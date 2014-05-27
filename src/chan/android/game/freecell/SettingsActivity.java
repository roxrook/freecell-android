package chan.android.game.freecell;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.Arrays;
import java.util.List;


public class SettingsActivity extends SherlockActivity {

    private static final Background[] BACKGROUNDS = new Background[]{
            new Background("Main Screen", R.drawable.shadow_main_screen),
            new Background("Aluminum", R.drawable.bg_drawable_aluminum),
            new Background("Purple", R.drawable.bg_drawable_purple),
            new Background("Green", R.drawable.bg_drawable_green),
            new Background("Blue", R.drawable.bg_drawable_blue),
    };

    private CheckBox checkBoxSound;

    private CheckBox checkBoxAutoPlay;

    private CheckBox checkBoxClock;

    private Spinner spinnerBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        spinnerBackground = (Spinner) findViewById(R.id.setting_$_spinner_background);
        final BackgroundArrayAdapter adapter = new BackgroundArrayAdapter(this, BACKGROUNDS);
        spinnerBackground.setAdapter(adapter);
        spinnerBackground.setSelection(adapter.indexOf(GameSettings.getBackgroundDrawableId()));
        spinnerBackground.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                GameSettings.setBackgroundDrawableId(adapter.getItem(position).getResourceId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Ignore
            }
        });

        checkBoxSound = (CheckBox) findViewById(R.id.setting_$_checkbox_sound);
        checkBoxSound.setChecked(GameSettings.isSoundEnabled());

        checkBoxAutoPlay = (CheckBox) findViewById(R.id.setting_$_checkbox_auto_play);
        checkBoxAutoPlay.setChecked(GameSettings.isAutoPlayEnabled());

        checkBoxClock = (CheckBox) findViewById(R.id.setting_$_checkbox_clock);
        checkBoxClock.setChecked(GameSettings.isClockEnabled());
    }

    @Override
    public void onBackPressed() {
        GameSettings.enableSound(checkBoxSound.isChecked());
        GameSettings.enableAutoPlay(checkBoxAutoPlay.isChecked());
        GameSettings.enableClock(checkBoxClock.isChecked());
        super.onBackPressed();
    }

    private static class Background {

        final String name;
        final int resourceId;

        public Background(String name, int resourceId) {
            this.name = name;
            this.resourceId = resourceId;
        }

        public String getName() {
            return name;
        }

        public int getResourceId() {
            return resourceId;
        }
    }

    private static class BackgroundArrayAdapter extends BaseAdapter {

        final List<Background> backgrounds;
        final Context context;

        public BackgroundArrayAdapter(Context context, Background[] backgrounds) {
            this.context = context;
            this.backgrounds = Arrays.asList(backgrounds);
        }

        @Override
        public int getCount() {
            return backgrounds.size();
        }

        @Override
        public Background getItem(int position) {
            return backgrounds.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public int indexOf(int resourceId) {
            for (int i = backgrounds.size() - 1; i >= 0; --i) {
                if (backgrounds.get(i).getResourceId() == resourceId) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.background_row, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.image.setImageResource(backgrounds.get(position).getResourceId());
            viewHolder.name.setText(backgrounds.get(position).getName());
            return convertView;
        }

        static class ViewHolder {
            final ImageView image;
            final TextView name;

            public ViewHolder(View v) {
                image = (ImageView) v.findViewById(R.id.background_row_$_imageview);
                name = (TextView) v.findViewById(R.id.background_row_$_textview);
            }
        }
    }
}
