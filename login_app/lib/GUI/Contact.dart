import 'package:flutter/material.dart';
import 'package:login_app/GUI/Welcome_Screen.dart';
import 'package:login_app/Requests/RestApi.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Contact extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return Contact_State();
  }
 
}
class Contact_State extends State<Contact>{
  
  List<dynamic> contacts = [];
  
  Future<dynamic> getContact() async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var res = await RestApi.getContact(prefs.getString("tok").toString());
     contacts = res;
    setState(() {
      
    });
     print(contacts);
     return res;
  }
 stringToColor(String hexcode){
    hexcode = hexcode.toUpperCase().replaceAll("#","");
    if(hexcode.length == 6) hexcode = "FF"+hexcode;
    return Color(int.parse(hexcode,radix: 16));
   }
@override
void initState(){
    super.initState();
    var res = getContact();
    print(res);
  }
String buttonColorop = "#787779";
@override
  Widget build(BuildContext context) {
    return Container(
      child: ListView.separated(
        padding: EdgeInsets.all(30),
        itemCount: contacts.length,
        itemBuilder: ((context, index) {
          return ElevatedButton(
             style: ElevatedButton.styleFrom(
                            elevation : 8,
                            shadowColor: Color.fromARGB(255, 68, 38, 6),
                            primary: stringToColor(buttonColorop),
                            onPrimary : Color.fromARGB(210, 217, 255, 4),
                            padding: EdgeInsets.all(10),
                            fixedSize: Size(MediaQuery.of(context).size.width*0.20,MediaQuery.of(context).size.width*0.30),
                        ), 
            onPressed: () {
              print(contacts[index]['sessionId']);
              Welcome_Screen_State.getSessionTosendTo(contacts[index]['sessionId']);
            },
           child: Column(mainAxisAlignment: MainAxisAlignment.center,children: [Row(children:[Expanded(child: Icon(Icons.person_rounded,color: Color.fromARGB(255, 0, 176, 0),size: 60,)) ,Text(contacts[index]['username'],textAlign: TextAlign.center ,style: TextStyle(color: Colors.white , fontSize: 20 , fontWeight: FontWeight.w600 ),),Expanded(flex: 1, child: SizedBox(height:MediaQuery.of(context).size.height*0.09 ,),)]),Expanded(flex: 1, child: Container(),),SizedBox(width:MediaQuery.of(context).size.width*0.1 ,)],)
            );
        }), separatorBuilder: (BuildContext context, int index) => SizedBox(height: MediaQuery.of(context).size.height*0.05,) ,
        ),
    );
  }
}
