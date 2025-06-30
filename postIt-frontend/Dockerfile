# 베이스 이미지
# FROM nginx:stable
FROM nginx:stable-alpine-slim

# 빌드된 Flutter 웹 파일을 복사
COPY build/web /usr/share/nginx/html

# Nginx 설정을 복사 (선택적)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# 포트 80을 노출
EXPOSE 80

# Nginx를 실행 (기본적으로 실행되지만 명시적으로 추가)
CMD ["nginx", "-g", "daemon off;"]