package com.project.realhealthbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BreathingBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottomsheet_breathing, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        View circle = view.findViewById(R.id.breathingCircle);

        circle.startAnimation(
                AnimationUtils.loadAnimation(requireContext(),
                        R.anim.breathing_scale)
        );
    }



    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            parent.getLayoutParams().height =
                    ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

}

