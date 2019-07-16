# はじめに
本サンプルはQUARKUSを使用してRESTful APIを実装するものです。

* GRAALVMをあらかじめインストールしておいてください。

# 利用Extension
1. quarkus-resteasy
1. quarkus-resteasy-jsonb
1. quarkus-hibernate-orm
1. quarkus-jdbc-mariadb
1. quarkus-hibernate-validator

# 作ったサンプルをJavaで動作させる場合
1. MariaDBを起動
1. src/main/resources/application.propertiesを上記MariaDBに合わせて変更
1. 以下のコマンドを投入して起動
    ```
    $ mvn quarkus:dev
    ```
1. IntelliJ IDEA等の統合開発環境から5005ポートを利用してリモートデバッグ可能

# 作ったサンプルをdocker用のimageにする場合
1. GRAALVM_HOMEを設定し、以下のコマンドを起動
    ```
    $ mvn package -Pnative
    ```
1. targetディレクトリにxxxx-runnerという実行可能ファイルが作成される。