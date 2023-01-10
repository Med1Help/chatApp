import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:login_app/GUI/Login_Screen.dart';
import 'package:login_app/Requests/RestApi.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';
final socketUrl = 'http://192.168.1.116:8081/ws-message';
class Welcome_Screen extends StatefulWidget{
  State<Welcome_Screen> createState(){
    return Welcome_Screen_State();
  }
}
class Welcome_Screen_State extends State<Welcome_Screen>{
  String color1 = "#2A280C";
    String color2 = "#938A16";
    String color3 = "#CFA300";
    String buttonColorP = "#C97100";
    String buttonColorop = "#FFB351";
    String textColor ="#FFFFFF";
    late TextEditingController msgC = new TextEditingController();
    late List<dynamic> displayedMessage = [];
    StompClient? stompClient = null ;
    String commingMsg = "";
    stringToColor(String hexcode){
    hexcode = hexcode.toUpperCase().replaceAll("#","");
    if(hexcode.length == 6) hexcode = "FF"+hexcode;
    return Color(int.parse(hexcode,radix: 16));
   }
   void callbackfunc(StompFrame frame){
   //List<dynamic>? result = json.decode(frame.body!.toString());
      var res = jsonDecode(frame.body!.toString());
      commingMsg =  res["message"];
      print("should came : "+frame.body!.toString());
      setState(() {
        
      });
   }
   void onconnect(StompFrame frame){
    stompClient?.subscribe(
      destination: '/user/topic/message',
      callback: callbackfunc
      );
      //  Timer.periodic(Duration(seconds: 10), (_) {
      //   stompClient?.send(
      //     destination: '/app/chat.register',
      //     body: json.encode({'msg': "message"}),
      //   );
      // });
   }
   Future<String> getSessionIdTosend()async{
    var res = await RestApi.userToSend("Manal","0000");
    try {
      return res["sessionId"].toString();
    } catch (e) {
      return e.toString();
    }
   }
   Future<void> sendMessage() async{
     var res = await RestApi.userToSend("hello","1234567");
     print("message gone saved by "+res["id"].toString());
     var res2 = await RestApi.send(msgC.text,int.parse(res["id"].toString()));
    try {
     var msg = jsonEncode({"message":msgC.text,"sessionId":res["sessionId"].toString()});
      stompClient?.send(
      destination: '/app/chat',
      body: msg
      );
    } catch (e) {
      print(e.toString());
    }
   }
   Map<String,String> go = new Map();
   @override
   void initState(){
    super.initState();
    var g = <String,String>{"UserId" : Login_Screen_state.user.getId().toString() , "login" : Login_Screen_state.user.getUserName() , "passcode" : "1234567"};
    go.addEntries(g.entries);
    if(stompClient == null){
      try{stompClient = StompClient(
        config: StompConfig.SockJS(
          url: socketUrl,
          onConnect: onconnect,
          stompConnectHeaders:go,
          webSocketConnectHeaders:go, 
          onWebSocketError: (dynamic error)=>print(error.toString()),
          )
        );
      stompClient?.activate();}catch(e){print(e.toString());}  
    }
   }
  Widget build(BuildContext context){
    return Scaffold(
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 15),
        decoration: BoxDecoration(
          gradient: LinearGradient(begin:Alignment.bottomCenter,end:Alignment.topCenter ,colors: [stringToColor(color3),stringToColor(color3),stringToColor(color3)]),
        ),
        child: ListView(
          //mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(child:Container(child: Image.asset("images/chat.png"))),
            SizedBox(height: MediaQuery.of(context).size.height*0.05,),
            Text(commingMsg,style: GoogleFonts.shadowsIntoLight(height: 3 , fontSize: 40 ,fontWeight: FontWeight.w100, color: stringToColor(textColor),letterSpacing:.12),), //TextStyle(height: 2 , fontSize: 30 , color: stringToColor(textColor)),)
            Expanded(flex:2,child: reusableTextField("Message", Icons.message, false, msgC),),
            Expanded(flex:4,child:SizedBox(height: 30,)),
            Container(padding: EdgeInsets.symmetric(horizontal: 70), child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                            elevation : 8,
                            shadowColor: Color.fromARGB(255, 68, 38, 6),
                            primary: stringToColor(buttonColorP),
                            onPrimary : stringToColor(buttonColorop),
                            padding: EdgeInsets.all(10),
                            fixedSize: Size(270,50),
                            shape: RoundedRectangleBorder( borderRadius: BorderRadius.circular(80)),
                        ),
                        onPressed: ()async{
                          
                            print("welcome "+Login_Screen_state.user.getUserName());
                            if(msgC.text != ""){
                            await sendMessage();
                            }
                            msgC.text = "";
                            try{displayedMessage = await RestApi.getMsg(Login_Screen_state.user.getId() );}catch(e){print(e.toString());}
                            setState(() {
                              
                            });
                          
                        },
                        child: Row(mainAxisAlignment: MainAxisAlignment.center,children: [Expanded(flex:2,child:Text("Send",textAlign: TextAlign.center ,style: TextStyle(color: stringToColor(textColor) , fontSize: 20 , fontWeight: FontWeight.w600 ),)),Expanded(flex: 1, child: Container(),),Expanded(child: Icon(Icons.send,color: stringToColor(textColor),))],)
                        )),
            SizedBox(height: MediaQuery.of(context).size.height*0.05,),            
            Container(
              height:MediaQuery.of(context).size.height*0.2 ,
              child: ListView.builder(
                itemCount: displayedMessage.length,
                itemBuilder: (context,index){
                    return  Text(
                    displayedMessage[index]["message"],
                    style: GoogleFonts.shadowsIntoLight(height: 3 , fontSize: 40 ,fontWeight: FontWeight.w100, color: stringToColor(textColor),letterSpacing:.12), //TextStyle(height: 2 , fontSize: 30 , color: stringToColor(textColor)),
                    );
                }
                )
            )   
          ],
        ),
    ));
  }
  TextField reusableTextField(String text, IconData icon, bool isPasswordType,
    TextEditingController controller) {
  return TextField(
    controller: controller,
    obscureText: isPasswordType,
    enableSuggestions: !isPasswordType,
    autocorrect: !isPasswordType,
    cursorColor: Colors.white,
    style: TextStyle(color: Colors.white.withOpacity(0.9)),
    decoration: InputDecoration(
      prefixIcon: Icon(
        icon,
        color: Colors.white70,
      ),
      labelText: text,
      labelStyle: TextStyle(color: Colors.white.withOpacity(0.9)),
      filled: true,
      floatingLabelBehavior: FloatingLabelBehavior.never,
      fillColor: Colors.white.withOpacity(0.3),
      border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(30.0),
          borderSide: const BorderSide(width: 0, style: BorderStyle.none)),
    ),
    keyboardType: isPasswordType
        ? TextInputType.visiblePassword
        : TextInputType.emailAddress,
  );
}
  
}