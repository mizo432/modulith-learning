# ベースイメージの指定
FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

                              # 必要に応じてビルド引数を設定
ARG INSTALL_MAVEN="false"
ARG MAVEN_VERSION=""
ARG INSTALL_GRADLE="true"
ARG GRADLE_VERSION=""

# MavenおよびGradleのインストール
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then \
                                      su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; \
                                  fi \
                                  && if [ "${INSTALL_GRADLE}" = "true" ]; then \
                                      su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; \
                                  fi

                              # 作業ディレクトリを設定
WORKDIR /app

                              # プロジェクトのソースコードをコンテナにコピー
COPY . .

                              # プロジェクトをビルド
RUN gradle clean build -x test -daemon

# アプリケーションを起動
CMD ["java", "-jar", "build/libs/service-registry-0.0.1-SNAPSHOT.jar"]
