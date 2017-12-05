package com.modim.lan.lanandroid;

import android.graphics.Point;
import android.view.MotionEvent;

public class MathUtil {
    public static int distance(MotionEvent event){
        double dx = (event.getX(0) - event.getX(1));
        double dy = (event.getY(0) - event.getY(1));
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
    public static Point midPoint(MotionEvent event) {
        double x = event.getX(0) + event.getX(1);
        double y = event.getY(0) + event.getY(1);
        Point pt = new Point((int)x/2, (int)y/2);
        return pt;
    }
    public static int distance(Point start, Point end){
        double dx = (end.x - start.x);
        double dy = (end.y - start.y);
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

}
