class Comment {
  int id;
  String content;
  String username;
  String nickname;
  int postId;

//<editor-fold desc="Data Methods">
  Comment({
    required this.id,
    required this.content,
    required this.username,
    required this.nickname,
    required this.postId,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': this.id,
      'content': this.content,
      'username': this.username,
      'nickname': this.nickname,
      'postId': this.postId,
    };
  }

  factory Comment.fromMap(Map<String, dynamic> map) {
    return Comment(
      id: map['id'] as int,
      content: map['content'] as String,
      username: map['username'] as String,
      nickname: map['nickname'] as String,
      postId: map['postId'] as int,
    );
  }
//</editor-fold>
}
