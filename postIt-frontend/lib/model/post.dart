import 'member.dart';

class Post {
  int? id;
  String? title;
  String? content;
  int? viewCount;
  Member? member;

  Post({
    required this.id,
    required this.title,
    required this.content,
    required this.viewCount,
    required this.member,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': this.id,
      'title': this.title,
      'content': this.content,
      'viewCount': this.viewCount,
      'username': member?.username,
      'nickname': member?.nickname,
    };
  }

  factory Post.fromMap(Map<String, dynamic> map) {
    return Post(
      id: map['id'],
      title: map['title'],
      content: map['content'],
      viewCount: map['viewCount'],
      member: Member(
        username: map['username'] as String?,
        nickname: map['nickname'] as String?,
      ),
    );
  }
}
