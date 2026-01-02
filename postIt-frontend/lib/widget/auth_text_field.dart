import 'package:flutter/material.dart';

class AuthTextField extends StatelessWidget {
  const AuthTextField({
    super.key,
    required this.labelText,
    this.icon,
    this.obscureText,
    required this.textEditingController,
    this.onChanged,
    this.isSamePassword,
    this.errorText,
    required this.isDarkMode,
  });

  final String labelText;
  final IconData? icon;
  final bool? obscureText;
  final TextEditingController textEditingController;
  final void Function(String)? onChanged;
  final bool? isSamePassword;
  final String? errorText;
  final bool isDarkMode;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        TextFormField(
          obscureText: obscureText != null ? obscureText! : false,
          controller: textEditingController,
          onChanged: onChanged,
          decoration: InputDecoration(
            labelText: labelText,
            labelStyle: TextStyle(
              color: isDarkMode ? Colors.grey[400] : const Color(0xFF6c757d),
            ),
            suffixIcon: icon != null
                ? Icon(
                    icon,
                    color: isDarkMode ? Colors.grey[400] : Colors.grey[600],
                  )
                : null,
            filled: true,
            fillColor:
                isDarkMode ? const Color(0xFF1E1E1E) : const Color(0xFFF8F9FA),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide(
                color: isDarkMode ? Colors.grey[700]! : Colors.grey[300]!,
              ),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide(
                color: isDarkMode ? Colors.grey[700]! : Colors.grey[300]!,
              ),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: const BorderSide(
                color: Color(0xFF13ec13),
                width: 2,
              ),
            ),
            contentPadding: const EdgeInsets.symmetric(
              horizontal: 16,
              vertical: 16,
            ),
          ),
          style: TextStyle(
            color: isDarkMode ? Colors.white : const Color(0xFF0e171b),
          ),
        ),
        if (isSamePassword != null && !isSamePassword!)
          Padding(
            padding: const EdgeInsets.only(top: 5.0),
            child: Text(
              errorText!,
              style: const TextStyle(color: Colors.red, fontSize: 13),
            ),
          )
      ],
    );
  }
}
