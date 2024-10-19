# JDK21
FROM openjdk:21

# 인자 설정, jar 파일 복제
COPY build/libs/*.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
