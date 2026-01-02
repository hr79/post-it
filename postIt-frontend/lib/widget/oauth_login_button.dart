import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class OauthLoginButton extends StatelessWidget {
  final VoidCallback? onPressed;
  final IconData icon;
  final String text;
  final Color backgroundColor;
  final Color textColor;
  final Color iconColor;

  const OauthLoginButton(
      {super.key,
      this.onPressed,
      required this.icon,
      required this.text,
      required this.backgroundColor,
      required this.textColor,
      required this.iconColor});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 48,
      child: ElevatedButton.icon(
        onPressed: onPressed ??
            () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('준비 중인 기능입니다!'),
                  duration: Duration(seconds: 2),
                ),
              );
            },
        icon: FaIcon(
          icon,
          size: 20,
          color: iconColor,
        ),
        label: Text(
          text,
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
            color: textColor,
          ),
        ),
        style: ElevatedButton.styleFrom(
          backgroundColor: backgroundColor, // Google Blue
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          elevation: 0,
        ),
      ),
    );
  }
}
