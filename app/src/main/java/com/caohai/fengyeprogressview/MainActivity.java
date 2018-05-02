package com.caohai.fengyeprogressview;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MainActivity extends AppCompatActivity {
    private NewProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (NewProgressView) findViewById(R.id.progress);
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(20000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                progressView.setProgress(value);
            }
        });
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.cancel();
                progressView.initView();
                animator.start();
            }
        });
    }
}
