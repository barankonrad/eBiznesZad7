FROM sbtscala/scala-sbt:eclipse-temurin-21.0.5_11_1.10.7_3.6.3

ARG USER_NAME=appuser
ARG USER_UID=100001
ARG USER_GID=${USER_UID}

RUN groupadd -g ${USER_GID} ${USER_NAME} && \
    useradd -u ${USER_UID} -g ${USER_GID} -m -d /home/${USER_NAME} ${USER_NAME}

WORKDIR /app

COPY build.sbt ./
COPY project ./project
COPY app ./app
RUN chown -R ${USER_NAME}:${USER_NAME} /app

USER ${USER_NAME}

RUN sbt -batch compile
EXPOSE 9000
CMD ["sbt", "-batch", "run"]