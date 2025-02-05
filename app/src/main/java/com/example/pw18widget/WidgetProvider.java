package com.example.pw18widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WidgetProvider";
    private static final String TOGGLE_FLASHLIGHT_ACTION = "com.example.pw18widget.TOGGLE_FLASHLIGHT";
    private static boolean isFlashOn = false;
    private static CameraManager cameraManager;
    private static String cameraId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate() called with: context = [" + context + "], appWidgetManager = [" + appWidgetManager + "], appWidgetIds = [" + appWidgetIds + "]");
        Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_view);

            Intent toggleIntent = new Intent(context, getClass());
            toggleIntent.setAction(TOGGLE_FLASHLIGHT_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        if (TOGGLE_FLASHLIGHT_ACTION.equals(intent.getAction())) {
            toggleFlashlight(context);
        }
    }

    private void toggleFlashlight(Context context) {
        Log.d(TAG, "toggleFlashlight() called with: context = [" + context + "]");
        Toast.makeText(context, "toggleFlashlight", Toast.LENGTH_SHORT).show();
        if (cameraManager == null) {
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                cameraId = cameraManager.getCameraIdList()[0];
            } catch (Exception e) {}
        }

        try {
            if (!isFlashOn) {
                cameraManager.setTorchMode(cameraId, true);
                isFlashOn = true;
            } else {
                cameraManager.setTorchMode(cameraId, false);
                isFlashOn = false;
            }
        } catch (Exception e) {}

    }
}
