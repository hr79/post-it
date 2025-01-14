class Member {
  String? username;
  String? password;
  String? nickname;
  String? email;
  String? loginType;

  Map<String, dynamic> toMap() {
    return {
      'username': this.username,
      'password': this.password,
      'nickname': this.nickname,
      'email': this.email,
      'loginType': this.loginType,
    };
  }

  factory Member.fromMap(Map<String, dynamic> map) {
    return Member(
      username: map['username'] as String,
      password: map['password'] as String,
      nickname: map['nickname'] as String,
      email: map['email'] as String,
      loginType: map['loginType'] as String,
    );
  }

  Member({
    this.username,
    this.password,
    this.nickname,
    this.email,
    this.loginType,
  });
}