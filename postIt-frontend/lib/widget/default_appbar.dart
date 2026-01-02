import 'package:flutter/material.dart';

PreferredSizeWidget defaultAppBar({
  required bool isDarkMode,
  required Widget title,
  Widget? leading,
  List<Widget>? actions,
  bool centerTitle = true,
}) {
  return AppBar(
    backgroundColor: isDarkMode ? const Color(0xFF1E1E1E) : Colors.white,
    elevation: 0,
    centerTitle: centerTitle,
    leading: leading,
    title: title,
    actions: actions,
  );
}
