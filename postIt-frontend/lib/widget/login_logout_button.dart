import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class LoginLogoutButton extends StatelessWidget {
  final bool isLoggedIn;
  final VoidCallback onLoginPressed;
  final VoidCallback onLogoutPressed;

  const LoginLogoutButton(
      {super.key,
      required this.isLoggedIn,
      required this.onLoginPressed,
      required this.onLogoutPressed});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 40,
      constraints: const BoxConstraints(
        minWidth: 84,
        maxWidth: 480,
      ),
      child: ElevatedButton(
        onPressed: isLoggedIn ? onLogoutPressed : onLoginPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: const Color(0xFF13ec13),
          foregroundColor: const Color(0xFF0d1b0d),
          padding: const EdgeInsets.symmetric(horizontal: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          elevation: 0,
        ),
        child: Text(
          isLoggedIn ? 'Log Out' : 'Login',
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.bold,
            letterSpacing: 0.015,
          ),
        ),
      ),
    );
  }
}
