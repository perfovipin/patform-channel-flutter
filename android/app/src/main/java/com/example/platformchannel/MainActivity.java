// Copyright 2014 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.example.platformchannel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CountDownTimer;
import java.util.Date;
import java.text.SimpleDateFormat;
import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  private static final String TIMER_CHANNEL = "samples.flutter.io/timerevent";
  private static final String NEW_CHANNEL = "samples.flutter.io/new";
    int mints;
    int sec;

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {

    new EventChannel(flutterEngine.getDartExecutor(), NEW_CHANNEL).setStreamHandler(
            new StreamHandler() {
                private CountDownTimer timer;
              @Override
              public void onListen(Object arguments, EventSink events) {
                timer = new CountDownTimer(10*60*1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //Date date = new Date(milliseconds);
                        int secs = (int)(millisUntilFinished/1000);
                        mints = (int)(secs/60);
                        sec = secs - (60 * mints);
                        events.success(mints+":"+sec);
                    }

                    public void onFinish() {
                        //mTextField.setText("done!");
                    }
                }.start();

              }

              @Override
              public void onCancel(Object arguments) {

              }
            }
    );


    new MethodChannel(flutterEngine.getDartExecutor(), TIMER_CHANNEL).setMethodCallHandler(
      new MethodCallHandler() {
        @Override
        public void onMethodCall(MethodCall call, Result result) {
          if(call.method.equals("getTime")) {
              result.success(mints+":"+sec);
          }
        }
      }
    );
  }
}
