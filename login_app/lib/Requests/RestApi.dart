import 'dart:convert';
import 'package:http/http.dart' as http;
class RestApi {
  static Future<dynamic> login(String username,String pass)async{
      print("bool login");
      // var res =  await http.post( Uri.parse("http://localhost:8081/api/getU"),headers: {'Content-type': 'application/json'},body:jsonEncode({"userName":username,"pass":pass}));
      var res =  await http.post( Uri.parse("http://192.168.1.116:8081/api/getU"),headers: {'Content-type': 'application/json'},body:jsonEncode({"userName":username,"pass":pass}));
      print("response : "+res.body.toString());
      var info = jsonDecode(res.body.toString());
      try {if(info["response"] == "true"){
        return info;
      }else{
        return false;
      }} catch (e) {
        print(e.toString());
        return false;
      }
  }
  static Future<dynamic> userToSend(String username,String pass)async{
      print("bool login");
      // var res =  await http.post( Uri.parse("http://localhost:8081/api/getU"),headers: {'Content-type': 'application/json'},body:jsonEncode({"userName":username,"pass":pass}));
      var res =  await http.post( Uri.parse("http://192.168.1.116:8081/api/getU"),headers: {'Content-type': 'application/json'},body:jsonEncode({"userName":username,"pass":pass}));
      print("response From sendTouser : "+res.body.toString());
      var info = jsonDecode(res.body.toString());
      try {if(info["response"] == "true"){
        return info; 
      }else{
        return false;
      }} catch (e) {
        print(e.toString());
        return false;
      }
  }
  static Future<dynamic> send(String msg,int id)async{
      print("bool login");
      // var res =  await http.post( Uri.parse("http://localhost:8081/api/send"),headers: {'Content-type': 'application/json'},body:jsonEncode({"message":msg,"id":id}));
      var res =  await http.post( Uri.parse("http://192.168.1.116:8081/api/send"),headers: {'Content-type': 'application/json'},body:jsonEncode({"message":msg,"userId":id}));
      print("response : "+res.body.toString());
      var info = jsonDecode(res.body.toString());
      try {if(info["response"] == "true"){
        return info;
      }else{
        return false;
      }} catch (e) {
        print(e.toString());
        return false;
      }
  }
  static Future<dynamic> getMsg(int id)async{
      print("bool login");
      // var res =  await http.get( Uri.parse("http://localhost:8081/api/getMessage/"+id.toString()));
      var res =  await http.get( Uri.parse("http://192.168.1.116:8081/api/getMessage/"+id.toString()));
      print("msgs : "+res.body.toString());
      var info = jsonDecode(res.body.toString());
      return info;
    
  }
}