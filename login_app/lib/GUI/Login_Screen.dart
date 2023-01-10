import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:login_app/Objects/user.dart';

import '../Requests/RestApi.dart';

class Login_Screen extends StatefulWidget{
  @override
  State<Login_Screen> createState(){
      return Login_Screen_state();
    }
}
class Login_Screen_state extends State<Login_Screen>{
    String color1 = "#2A280C";
    String color2 = "#938A16";
    String color3 = "#CFA300";
    String buttonColorP = "#C97100";
    String buttonColorop = "#FFB351";
    String textColor ="#FFFFFF";
    late TextEditingController emailC = new TextEditingController(),passC = new TextEditingController();
    GlobalKey<FormState> _loginForm = new GlobalKey<FormState>();
    static late User user;
    stringToColor(String hexcode){
    hexcode = hexcode.toUpperCase().replaceAll("#","");
    if(hexcode.length == 6) hexcode = "FF"+hexcode;
    return Color(int.parse(hexcode,radix: 16));
   }

  Widget build(BuildContext context){
    return Scaffold(
      appBar: AppBar(
        backgroundColor: stringToColor(color3),
        title: Text("Welcome to the login space"),
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 15),
        decoration: BoxDecoration(
          gradient: LinearGradient(begin:Alignment.bottomCenter,end:Alignment.topCenter ,colors: [stringToColor(color3),stringToColor(color3),stringToColor(color3)]),
        ),
        child: ListView(
          //mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(child:Container(child: Image.asset("images/accountLogo.png"))),
            Expanded(flex:3,
            child:Form(
              key: _loginForm,
              child: Column(
              children: [
                reusableTextField("E-mail", Icons.contact_mail, false, emailC),
                SizedBox(height: 20,),
                reusableTextField("Password", Icons.password, true, passC),
              ],
            ))),
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
                          print("Login");
                          var res = await RestApi.login(emailC.text,passC.text);
                          print("this is the : "+res.toString());
                          try{if(res["response"] == "true") {
                            try{user = new User( res["userName"] , res["id"] );}catch(e){print(e.toString());}
                          Navigator.of(context).pushNamed("Welcome");
                            }}catch(e){print(e.toString());}
                          
                        },
                        child: Row(mainAxisAlignment: MainAxisAlignment.center,children: [Expanded(flex:2,child:Text("Login",textAlign: TextAlign.center ,style: TextStyle(color: stringToColor(textColor) , fontSize: 20 , fontWeight: FontWeight.w600 ),)),Expanded(flex: 1, child: Container(),),Expanded(child: Icon(Icons.login,color: stringToColor(textColor),))],)
                        ))
          ],
        ),
    ));
  }
  TextFormField reusableTextField(String text, IconData icon, bool isPasswordType,
    TextEditingController controller) {
  return TextFormField(
    controller: controller,
    obscureText: isPasswordType,
    enableSuggestions: !isPasswordType,
    autocorrect: !isPasswordType,
    cursorColor: Colors.white,
    validator: (val) {
       val!.isEmpty? "Remplir ce champ":null;
       },
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