// import 'package:flutter/material.dart';
// import 'package:get/get.dart';
// import 'package:postit_frontend/controller/detail_page_controller.dart';
// import 'package:postit_frontend/view/post_write_page.dart';
//
// import '../model/comment.dart';
//
// class PostDetailPage extends GetView<DetailPageController> {
//   const PostDetailPage({super.key, required this.postId});
//
//   static const String route = "/detail";
//
//   final String postId;
//   final String postTitle = "How to make a perfect salad";
//   final String postContent =
//       "I have been making salads for myself for years and I think I have perfected the art of making a perfect salad. Here are my tips: 1. Make sure you have a good mix of greens. I usually use spinach and arugula. 2. Add some crunch. This can be anything from nuts to seeds to croutons. 3. Add some protein. This can be chicken, tofu or even hard boiled eggs. 4. Add some fruit. I like to add berries, oranges, or apples. 5. Dressing. You can buy your own dressing or make your own. My favorite is balsamic vinaigrette.";
//
//   @override
//   Widget build(BuildContext context) {
//     int intPostId = int.parse(postId);
//     controller.getDetailPost(intPostId);
//     return Scaffold(
//       appBar: AppBar(
//         leading: IconButton(
//           icon: Icon(Icons.arrow_back),
//           onPressed: () {
//             Navigator.pop(context);
//           },
//         ),
//         title: Text("Post Detail"),
//         backgroundColor: Colors.white,
//       ),
//       body: Obx(
//         () => controller.post.value == null
//             ? LinearProgressIndicator()
//             : SingleChildScrollView(
//                 padding: EdgeInsets.all(16.0),
//                 child: Column(
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   children: <Widget>[
//                     Text(controller.post.value?.title ?? "no title",
//                         style: TextStyle(
//                             fontSize: 20, fontWeight: FontWeight.bold)),
//                     SizedBox(height: 8),
//                     Text(controller.post.value?.content ?? postContent),
//                     if (controller.isAuthor()) ...[
//                       SizedBox(height: 16),
//                       Row(
//                         mainAxisAlignment: MainAxisAlignment.end,
//                         children: [
//                           ElevatedButton(
//                             onPressed: () {
//                               Get.toNamed(
//                                 PostWritePage.route,
//                                 arguments: controller.post.value, // post 객체 전체 전달
//                               );
//                             },
//                             child: Text('수정'),
//                           ),
//                           SizedBox(width: 8),
//                           ElevatedButton(
//                             onPressed: () {
//                               controller.deletePost();
//                             },
//                             style: ElevatedButton.styleFrom(
//                                 backgroundColor: Colors.red),
//                             child: Text('삭제'),
//                           ),
//                         ],
//                       ),
//                     ],
//                     SizedBox(height: 16),
//                     Row(
//                       mainAxisAlignment: MainAxisAlignment.spaceAround,
//                       children: <Widget>[
//                         Row(children: [Icon(Icons.thumb_up), Text("12")]),
//                         Row(children: [Icon(Icons.comment), Text("4")]),
//                         Row(children: [Icon(Icons.share), Text("2")]),
//                       ],
//                     ),
//                     Divider(),
//                     ListView.builder(
//                       shrinkWrap: true,
//                       physics: NeverScrollableScrollPhysics(),
//                       // ListView 내부 스크롤 방지
//                       itemCount: controller.comments.length,
//                       itemBuilder: (context, index) {
//                         var comment = controller.comments[index];
//                         return CommentWidget(comment: comment);
//                       },
//                     ),
//                     SizedBox(height: 16),
//                     Row(
//                       mainAxisSize: MainAxisSize.min,
//                       mainAxisAlignment: MainAxisAlignment.center,
//                       children: [
//                         Expanded(
//                           child: TextField(
//                             onChanged: (inputText) =>
//                                 controller.setCommentController(inputText),
//                             decoration:
//                                 InputDecoration(hintText: "Add a comment..."),
//                           ),
//                         ),
//                         IconButton(
//                             onPressed: () {
//                               controller.saveComment(intPostId);
//                             },
//                             icon: Icon(Icons.send))
//                       ],
//                     ),
//                   ],
//                 ),
//               ),
//       ),
//     );
//   }
// }
//
// class CommentWidget extends StatelessWidget {
//   final Comment comment;
//
//   CommentWidget({required this.comment});
//
//   @override
//   Widget build(BuildContext context) {
//     return Column(
//       crossAxisAlignment: CrossAxisAlignment.start,
//       children: [
//         Row(
//           mainAxisAlignment: MainAxisAlignment.spaceBetween,
//           children: [Text(comment.author), Text("Oct 14")],
//         ),
//         Text(comment.content),
//         Row(children: [
//           Icon(Icons.thumb_up),
//           Text("likes"),
//         ]),
//         Divider(),
//         SizedBox(height: 8),
//       ],
//     );
//   }
// }
