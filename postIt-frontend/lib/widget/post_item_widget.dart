import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../model/post.dart';

class PostItemWidget extends StatefulWidget {
  final Post post;
  final bool isDarkMode;

  const PostItemWidget({
    super.key,
    required this.post,
    required this.isDarkMode,
  });

  @override
  State<PostItemWidget> createState() => _PostItemWidgetState();
}

class _PostItemWidgetState extends State<PostItemWidget> {
  bool _isHovered = false;

  @override
  Widget build(BuildContext context) {
    final backgroundColor = widget.isDarkMode
        ? (_isHovered ? const Color(0xFF2A2A2A) : const Color(0xFF1E1E1E))
        : (_isHovered ? const Color(0xFFF0F0F0) : const Color(0xFFF8F9FA));

    final borderColor = _isHovered
        ? (widget.isDarkMode
            ? const Color(0xFF4A4A4A)
            : const Color(0xFFE0E0E0))
        : Colors.transparent;

    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      child: MouseRegion(
        onEnter: (_) => setState(() => _isHovered = true),
        onExit: (_) => setState(() => _isHovered = false),
        child: Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: backgroundColor,
            borderRadius: BorderRadius.circular(12),
            border: Border.all(
              color: borderColor,
              width: 1.5,
            ),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(widget.isDarkMode ? 0.3 : 0.1),
                blurRadius: _isHovered ? 12 : 8,
                offset: Offset(0, _isHovered ? 4 : 2),
              ),
            ],
          ),
          child: InkWell(
            onTap: () {
              Get.toNamed("/board/${widget.post.id}");
            },
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        widget.post.title ?? '',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color: widget.isDarkMode
                              ? Colors.white
                              : const Color(0xFF0e171b),
                          fontFamily: 'Roboto',
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 6),
                      Text(
                        widget.post.content ?? '',
                        style: TextStyle(
                          fontSize: 14,
                          color: widget.isDarkMode
                              ? Colors.grey[300]
                              : const Color(0xFF6c757d),
                          fontFamily: 'Roboto',
                          height: 1.4,
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        softWrap: false,
                      ),
                    ],
                  ),
                ),
                const SizedBox(width: 12),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // 조회수
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 8, vertical: 4),
                      decoration: BoxDecoration(
                        color: widget.isDarkMode
                            ? const Color(0xFF2A2A2A)
                            : Colors.white,
                        borderRadius: BorderRadius.circular(6),
                      ),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.visibility_outlined,
                            size: 16,
                            color: widget.isDarkMode
                                ? Colors.grey[400]
                                : const Color(0xFF6c757d),
                          ),
                          const SizedBox(width: 4),
                          Text(
                            '${widget.post.viewCount ?? 0}',
                            style: TextStyle(
                              fontSize: 13,
                              color: widget.isDarkMode
                                  ? Colors.grey[400]
                                  : const Color(0xFF6c757d),
                              fontWeight: FontWeight.w500,
                              fontFamily: 'Roboto',
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 8),
                    // 댓글수
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 8, vertical: 4),
                      decoration: BoxDecoration(
                        color: widget.isDarkMode
                            ? const Color(0xFF2A2A2A)
                            : Colors.white,
                        borderRadius: BorderRadius.circular(6),
                      ),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.comment_outlined,
                            size: 16,
                            color: widget.isDarkMode
                                ? Colors.grey[400]
                                : const Color(0xFF6c757d),
                          ),
                          const SizedBox(width: 4),
                          Text(
                            '${widget.post.commentCount ?? 0}',
                            style: TextStyle(
                              fontSize: 13,
                              color: widget.isDarkMode
                                  ? Colors.grey[400]
                                  : const Color(0xFF6c757d),
                              fontWeight: FontWeight.w500,
                              fontFamily: 'Roboto',
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
