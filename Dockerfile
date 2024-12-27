FROM openjdk:17-alpine
LABEL authors="LJH"

ENTRYPOINT ["top", "-b"]