GET
====
RESTful APIではHTTPメソッドとHTTPステータスコードは特定の意味を持ちます。

GETリクエストは、リソースを取得するために使用されます。これは通常、データベースからデータを読み込むための操作に対応しています。

成功時のHTTPステータスコード:

- 200 OK: 要求されたリソースが成功して返されたことを意味します。このリクエストはデータを体で返すのが一般的です。

エラー時のHTTPステータスコード:

- 404 Not Found: 要求されたリソースが見つからないことを示します。

- 400 Bad Request: サーバーがリクエストを理解できないことを示します。たとえば、クエリパラメータが間違っている場合などです。

- 500 Internal Server Error: サーバー側のエラーを表現する一般的なコードです。具体的なエラーの詳細はメッセージボディに記載されることが多いです。

# 特記事項

## 一覧で0件を返す場合

一覧を取得するためのRESTful APIのGETリクエストにおいて、結果が0件だった場合の適切なHTTPステータスコードは
200 OK です。

一般的に、一覧の取得操作が正常に完了し、その結果が0件である場合、これはサーバの観点から見れば問題ない（つまり、成功）と見なされます。そのため、結果が0件であっても

- 200 OK を返すべきです。

そしてレスポンスボディは、通常は空の配列やリストの形式で返されます。例えば、JSON形式においては []
となります。

これにより、クライアント側ではHTTPステータスコードを見て通信が成功したかどうかを判断し、レスポンスボディを解析して具体的なデータを取得できます。

なお、具体的なエンドポイントやコンテキストによっては、0件だった場合に別のステータスコードを返すことを定義している場合もあります。そのため、具体的な仕様は常にAPIのドキュメンテーションや仕様書を確認してください。

## ページング

### リクエスト

RESTful
APIでページングを行う場合、一般的にはクエリパラメータを使用してページ数やページあたりの項目数をサーバに送信します。主にpageとlimit（またはsize）というパラメータが使われます。

例えば次のような形になります：

```json
GET /items?page=3&limit=20
```

このURLは、3ページ目のアイテムをリクエストし、1ページあたり20項目を取得することを求めています。

また、一部のAPIではoffsetとlimitを使用することもあります。offsetはリストの先頭からのオフセット（すなわち、最初に返す項目の位置）を指し、limitは返す項目の最大数を指します。この方法は、特定の範囲の項目を取得するため、またはスクロール可能なリストでの「次のN項目を取得する」などの操作によく使用されます。

なお、このようなページングのパラメータは、APIの設計方針や使用するフレームワーク・ライブラリによりますので、使用するAPIのドキュメンテーションや仕様を確認してください。

### レスポンス

上記は一例であり、具体的なフィールドの名称や構造はAPIやシステムの設計によります。

```json
{
  "data": [
    ...
    // 取得したデータの配列
  ],
  "pagination": {
    "current": 1,
    // 現在のページ番号
    "previous": null,
    // 前のページ番号（存在しない場合はnull）
    "next": 2,
    // 次のページ番号（存在しない場合はnull）
    "total": 10,
    // 総ページ数
    "limit": 20,
    // 1ページあたりの項目数
    "totalItems": 200
    // 総項目数
  }
}
```

また、結果の項目配列（上記のdata）を直接返すだけでなく、それぞれのページが持つ項目の範囲や、前後のページへのリンクをHTTPヘッダに含める設計を採用するAPIもあります。

ページ情報をレスポンスボディかHTTPヘッダに含めるかどうか、またその詳細な形式はAPIや使用フレームワーク、ライブラリの設計によるため、具体的な仕様はAPIのドキュメンテーションを確認してください。