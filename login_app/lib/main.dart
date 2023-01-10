import 'package:flutter/material.dart';
import 'package:login_app/GUI/Login_Screen.dart';
import 'package:login_app/GUI/Welcome_Screen.dart';

void main() {
  runApp( MyApp());
}


class MyApp extends StatelessWidget {
  late SharedAppData sharedData;
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Chat App',
      routes:{
        "Welcome" : (context) => Welcome_Screen(),
      },
      home:  Login_Screen(),
    );
  }
}


