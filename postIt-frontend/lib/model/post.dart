import 'member.dart';

class Post {
  int? id;
  String? title;
  String? content;
  int? viewCount;
  int? commentCount;
  String? username;
  String? nickname;

  Post({
    this.id,
    this.title,
    this.content,
    this.viewCount,
    this.commentCount,
    this.username,
    this.nickname,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': this.id,
      'title': this.title,
      'content': this.content,
      'viewCount': this.viewCount,
      'commentCount': this.commentCount,
      'username': this.username,
      'nickname': this.nickname,
    };
  }

  factory Post.fromMap(Map<String, dynamic> map) {
    return Post(
      id: map['id'],
      title: map['title'],
      content: map['content'],
      viewCount: map['viewCount'],
      commentCount: map['commentCount'],
      username: map['username'] as String?,
      nickname: map['nickname'] as String?,
    );
  }
}
