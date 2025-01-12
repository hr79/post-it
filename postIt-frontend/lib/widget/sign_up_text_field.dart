import 'package:flutter/material.dart';

class SignUpTextField extends StatelessWidget {
  const SignUpTextField({
    super.key,
    required this.labelText,
    required this.icon,
    this.obscureText,
    required this.textEditingController,
    this.onChanged,
    this.isSamePassword,
    this.errorText,
  });

  final String labelText;
  final IconData icon;
  final bool? obscureText;
  final TextEditingController textEditingController;
  final void Function(String)? onChanged;
  final bool? isSamePassword;
  final String? errorText;

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
            suffixIcon: Icon(icon, color: Colors.grey),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8.0),
              borderSide: const BorderSide(color: Colors.grey),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8.0),
              borderSide: const BorderSide(color: Colors.blue),
            ),
            contentPadding:
                const EdgeInsets.symmetric(vertical: 16.0, horizontal: 12.0),
          ),
        ),
        if (isSamePassword != null && !isSamePassword!)
          Padding(
            padding: const EdgeInsets.only(top: 5.0),
            child: Text(
              errorText!,
              style: TextStyle(color: Colors.red, fontSize: 13),
            ),
          )
      ],
    );
  }
}
