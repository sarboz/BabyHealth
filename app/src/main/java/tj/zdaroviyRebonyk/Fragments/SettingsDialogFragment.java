package tj.zdaroviyRebonyk.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import tj.zdaroviyRebonyk.R;

public class SettingsDialogFragment extends DialogFragment {

    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.settings_dialog, container, false);

        Button plus = v.findViewById(R.id.shriftPlus);
        Button minus = v.findViewById(R.id.shriftMinus);
        plus.setOnClickListener(mclickPlus);
        minus.setOnClickListener(mclickMinus);

        Button backgroundSunBtn = v.findViewById(R.id.backgroundSun);
        Button backgroundMonBtn = v.findViewById(R.id.backgroundMon);

        backgroundMonBtn.setOnClickListener(backgroundMon);
        backgroundSunBtn.setOnClickListener(backgroundSun);

        setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    View.OnClickListener mclickPlus, mclickMinus;

    public void shriftPlus(View.OnClickListener v) {
        mclickPlus = v;
    }

    public void shriftMinus(View.OnClickListener v) {
        mclickMinus = v;
    }

    View.OnClickListener backgroundSun, backgroundMon;

    public void backgroundSun(View.OnClickListener v) {
        backgroundSun = v;
    }

    public void backgroundMon(View.OnClickListener v) {
        backgroundMon = v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }
}
