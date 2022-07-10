// Copyright 2014 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PlatformChannel extends StatefulWidget {
  const PlatformChannel({super.key});

  @override
  State<PlatformChannel> createState() => _PlatformChannelState();
}

class _PlatformChannelState extends State<PlatformChannel> {
  static const MethodChannel methodChannel =
      MethodChannel('samples.flutter.io/timerevent');
  static const EventChannel newEventChannel =
  EventChannel('samples.flutter.io/new');

  String _time = 'Timer: unknown.';

  Future<void> _getTime() async {
    String time;
    try {
      final int? result = await methodChannel.invokeMethod('getTime');
      time = 'Timer: $result%.';
    } on PlatformException {
      time = 'Failed to get Timer.';
    }
    setState(() {
      _time = time;
    });
  }

  @override
  void initState() {
    super.initState();
    newEventChannel.receiveBroadcastStream().listen(_onNewEvent, onError: _onNewError);
  }


  void _onNewEvent(Object? event) {
    setState(() {
      _time =
          "Timer: $event";
    });
  }

  void _onNewError(Object error) {
    setState(() {
      _time = 'Timer: Error';
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(_time, key: const Key('Timer: ')),
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: ElevatedButton(
                  onPressed: _getTime,
                  child: const Text('Refresh'),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

void main() {
  runApp(const MaterialApp(home: PlatformChannel()));
}
