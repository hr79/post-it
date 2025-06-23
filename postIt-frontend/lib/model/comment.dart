class Comment {
  int id;
  String content;
  String author;
  int postId;

//<editor-fold desc="Data Methods">
  Comment({
    required this.id,
    required this.content,
    required this.author,
    required this.postId,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': this.id,
      'content': this.content,
      'author': this.author,
      'postId': this.postId,
    };
  }

  factory Comment.fromMap(Map<String, dynamic> map) {
    return Comment(
      id: map['id'] as int,
      content: map['content'] as String,
      author: map['author'] as String,
      postId: map['postId'] as int,
    );
  }
//</editor-fold>
}