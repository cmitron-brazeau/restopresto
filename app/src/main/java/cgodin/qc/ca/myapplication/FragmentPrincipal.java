package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by o.aspirot on 2017-11-09.
 */

public class FragmentPrincipal extends Fragment {

    private testInterface unInterface;

    public FragmentPrincipal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentacceuil, container, false);
        view.setBackgroundColor(Color.WHITE);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        unInterface = (testInterface)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unInterface = null;
    }
}