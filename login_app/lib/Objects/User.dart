class User{
  late String _userName;
  late int    _id;
  User(String username,int id){
    this._userName = username;
    this._id       = id;
  }
  int getId(){
    return this._id;
  }
  String getUserName(){
    return this._userName;
  }
}