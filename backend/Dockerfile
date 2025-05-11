FROM sbtscala/scala-sbt:eclipse-temurin-21.0.5_11_1.10.7_3.6.3

WORKDIR /app

COPY ./build.sbt ./build.sbt
COPY ./project ./project
COPY . .

RUN sbt compile
EXPOSE 9000
CMD ["sbt", "run"]