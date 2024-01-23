package com.dere.msgapp.Actions;

import android.view.animation.TranslateAnimation;


public class Animations extends TranslateAnimation {
    public Animations(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,Long duration) {
        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
        setDuration(duration);
    }
}