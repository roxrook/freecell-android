package chan.android.game.freecell.model;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import chan.android.game.freecell.R;

public class CongratsDialogFragment extends DialogFragment {

    public interface OnDialogClickListener {

        public void onLeftButtonClick();

        public void onRightButtonClick();
    }

    private OnDialogClickListener listener;

    public CongratsDialogFragment() {

    }

    public void setOnClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.congrats_dialog, container, false);
        Button left = (Button) root.findViewById(R.id.congrats_dialog_$_button_left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftButtonClick();
                }
            }
        });
        Button right = (Button) root.findViewById(R.id.congrats_dialog_$_button_right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRightButtonClick();
                }
            }
        });
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return root;
    }
}
