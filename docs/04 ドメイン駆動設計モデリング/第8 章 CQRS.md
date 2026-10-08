# 第8章 CQRS

CQRS(Command Query Responsibility Segregation: コマンドクエリ責務分離)
は、DDDを実装する上で避けられない課題を解決する方法です。レイヤーとしてはアプリケーション層に定義する内容になりますが、ボリュームが大きいためアプリケーション層に続いて独立した章として解説します。

## 8.1 DDDの参照系処理で発生する問題

DDDの戦術的設計パターンを使っていると、永続化層との入出力はリポジトリを使うことになります。更新系の処理では、エンティティや値オブジェクトでドメイン知識を表現し、リポジトリを使って集約単位で永続化するという構成をとると、非常に保守性の良いものになります。

しかし、参照系の処理、特に一覧画面で複数の集約の情報を表示したいような場面で、リポジトリを使用するだけだと問題が発生することがあります。

例として、次のようなケースを考えます。タスク、ユーザー、ラベルという3つの集約があり、それぞれにリポジトリがあるとします。

```plantuml
@startuml
package "タスク集約" {
  class Task {
    id: TaskId
    name: String
    status: TaskStatus
    userId: UserId
    labelId: LabelId
  }

  class TaskId {
    value: String
  }

  class TaskStatus {
    value: String
  }

  interface TaskRepository {
    findById(id: TaskId): Task
    findByUserId(userId: UserId): List<Task>
    save(task: Task): void
  }

  Task --> TaskId
  Task --> TaskStatus
  TaskRepository ..> Task
}

package "ユーザー集約" {
  class User {
    id: UserId
    name: String
    email: String
  }

  class UserId {
    value: String
  }

  interface UserRepository {
    findById(id: UserId): User
    save(user: User): void
  }

  User --> UserId
  UserRepository ..> User
}

package "ラベル集約" {
  class Label {
    id: LabelId
    name: String
    color: Color
  }

  class LabelId {
    value: String
  }

  class Color {
    value: String
  }

  interface LabelRepository {
    findById(id: LabelId): Label
    save(label: Label): void
  }

  Label --> LabelId
  Label --> Color
  LabelRepository ..> Label
}

Task --> UserId: 参照
Task --> LabelId: 参照
@enduml
```

そのような場合に、以下のようなタスク一覧画面を作成することになりました。

```plantuml
@startuml
!define RECTANGLE class

RECTANGLE "タスク一覧" as TaskList {
  タスク名 | 担当者 | ラベル | ステータス
  --
  買い物 | 山田太郎 | 家事 | 未完了
  レポート作成 | 山田太郎 | 仕事 | 完了
  電話対応 | 佐藤花子 | 仕事 | 未完了
}
@enduml
```

これを1つのアプリケーションサービスクラスで実現しようとすると、3つのリポジトリからそれぞれ値を取得し、戻り値のクラスに詰め替えるような実装にせざるを得ません。すると、以下のような問題が発生します。

* 複数の集約から値を取得して戻り値の型に詰め替える処理が、ループが増えて読みにくいコードになる
* 画面に返す必要のない値を取得するのでパフォーマンスが悪化する
* 複数集約の条件で絞り込んでのページングができない

上記のような一覧画面が必要になることは多いので、ほとんどのプロジェクトで突き当たる問題です。

## 8.2 解決策

CQRSを導入します。CQRSとは、「参照に使用するモデルと更新に使用するモデルを分離する」というアーキテクチャです。モデルという言葉は多義語ですが、この文脈ではアプリケーションコード上のモデル、つまり更新系のクラスと参照系のクラスを分けるということになります。

```plantuml
@startuml
package "更新系モデル" {
  class Task {
    id: TaskId
    name: String
    status: TaskStatus
    userId: UserId
    labelId: LabelId

    complete(): void
    postpone(): void
  }

  interface TaskRepository {
    findById(id: TaskId): Task
    save(task: Task): void
  }

  class TaskService {
    completeTask(taskId: TaskId): void
    postponeTask(taskId: TaskId): void
  }

  TaskService --> TaskRepository: 使用
  TaskRepository ..> Task: 返却
}

package "参照系モデル" {
  class TaskDto {
    taskId: String
    taskName: String
    userName: String
    labelName: String
    status: String
  }

  interface TaskQueryService {
    fetchByUserId(userId: UserId): List<TaskDto>
    fetchAll(): List<TaskDto>
  }

  class TaskQueryServiceImpl {
    fetchByUserId(userId: UserId): List<TaskDto>
    fetchAll(): List<TaskDto>
  }

  TaskQueryService <|.. TaskQueryServiceImpl
  TaskQueryService ..> TaskDto: 返却
}

note bottom of "更新系モデル"
  ドメインモデルを使用して
  ビジネスロジックを実装
end note

note bottom of "参照系モデル"
  表示に特化した専用モデルを定義
  パフォーマンスを最適化
end note
@enduml
```

更新系モデルは、ドメインオブジェクト(エンティティ、値オブジェクトなど)をそのまま使用します。

参照系モデルは、特定のユースケースに特化した値の型を定義します。また、その値を取得するためのサービスも独自に定義します。例として、以下のようなクラスになります。

```java
// リスト8.1 TaskDto
public class TaskDto {
    private String taskId;
    private String taskName;
    private String userName;
    private String labelName;
}

// リスト8.2 TaskQueryService
public interface TaskQueryService {
    public List<TaskDto> fetchByUserId(UserId userId);
}
```

本書では参照用モデルの型をDTO、それを取得するためのサービスをクエリサービスという命名にしています。これは公式な定義は特にないので、各プロジェクトで決めてください。

### 8.2.1 参照用モデルの型を定義するレイヤー

参照用モデルの型を定義するクラスは、アーキテクチャ上どのように位置付けられるのでしょうか。

クエリサービスのインターフェイスと戻りの型をアプリケーション層に、クエリサービスの実装クラスはインフラ層に配置します。アプリケーション層は「引数でこのような条件を指定すると、このような型で返ってくる」という抽象的な知識(
What)だけ持ち、値の取得に関する具体的な知識(How)はインフラ層に隠蔽します。(
クエリサービスのインターフェイスがドメイン層ではなくアプリケーション層である理由は後述します)

```plantuml
@startuml
!include <C4/C4_Container>

LAYOUT_WITH_LEGEND()

Person(user, "ユーザー")

Container_Boundary(application, "アプリケーション") {
  Container(presentation, "プレゼンテーション層", "Controller, View", "ユーザーインターフェース")

  Container_Boundary(application_layer, "アプリケーション層") {
    Component(application_service, "アプリケーションサービス", "TaskService", "ユースケースを実現")
    Component(query_interface, "クエリサービスインターフェース", "TaskQueryService", "参照系の抽象")
    Component(dto, "DTO", "TaskDTO", "参照系の戻り値型")
  }

  Container_Boundary(domain_layer, "ドメイン層") {
    Component(domain_model, "ドメインモデル", "Task, User, Label", "ドメイン知識を表現")
    Component(repository_interface, "リポジトリインターフェース", "TaskRepository", "永続化の抽象")
  }

  Container_Boundary(infrastructure_layer, "インフラ層") {
    Component(repository_impl, "リポジトリ実装", "TaskRepositoryImpl", "更新系の永続化")
    Component(query_impl, "クエリサービス実装", "TaskQueryServiceImpl", "参照系の永続化")
  }
}

Container(database, "データベース", "PostgreSQL", "データの永続化")

Rel(user, presentation, "利用")
Rel(presentation, application_service, "利用")
Rel(presentation, query_interface, "利用")
Rel(application_service, domain_model, "操作")
Rel(application_service, repository_interface, "利用")
Rel(query_interface, dto, "返却")
Rel(repository_interface, domain_model, "返却")
Rel(repository_impl, repository_interface, "実装")
Rel(query_impl, query_interface, "実装")
Rel(repository_impl, database, "更新系クエリ")
Rel(query_impl, database, "参照系クエリ")

note right of query_interface
  アプリケーション層に配置
  Whatのみを定義
end note

note right of query_impl
  インフラ層に配置
  Howを実装
end note
@enduml
```

永続化層がRDBの場合、クエリサービスの実装クラスではDTOに詰め替えるのに必要な値を取得するクエリを書きます。この際のクエリは、複数テーブルをJOINして1リクエストで取得する、必要なカラムのみSELECTする、というようにパフォーマンスを最適化させるために自由に記述することができます。このチューニングが自由になることがリポジトリを使用する場合との大きな違いです。

また、クエリを実装する方法も、保守性を考慮して最適な方法を選ぶことができます。直接StringでSQLを書いても、クエリビルダのようなライブラリを使用しても良く、更新系で使用しているものとは異なるライブラリを使用しても構いません。

なお、筆者がJavaのプロジェクトで実装するときは、リポジトリもクエリサービスもjOOQ[^1]
というタイプセーフにクエリを実行できるライブラリを使用しています。

[^1]: https://www.jooq.org/

## 8.3 メリット、デメリット

前述の問題の裏返しになりますが、以下のようなメリットがあります。

* 複数集約にまたがるデータを取得する際のコードがシンプルになり、保守性が高まる
* クエリパフォーマンスが上がる、チューニングしやすくなる
* 複数集約の条件で絞り込んでのページングができるようになる

一方、デメリットは以下のようなものがあります。

* ドメインオブジェクトのデータが参照されている場所が追いにくくなる
* アーキテクチャ自体が複雑になり、理解にコストがかかる

デメリットの1つ目について補足します。CQRSを使用しない場合、ドメインオブジェクトのデータがどこで使用されるかを調べるためには、ドメインオブジェクトのゲッターから参照元を追えばすべて把握できます。しかし、CQRSを使用すると、参照系のオブジェクトが別途作られることにより、その方法ではすべての参照が追えなくなってしまいます。

```plantuml
@startuml
package "CQRSなし" {
  class Task {
    id: TaskId
    name: String
    status: TaskStatus

    getId(): TaskId
    getName(): String
    getStatus(): TaskStatus
  }

  class TaskController {
    showTask(id: TaskId): View
  }

  TaskController --> Task: 参照

  note bottom of TaskController
    ドメインオブジェクトを直接参照
    getterを追跡すれば参照箇所が特定できる
  end note
}

package "CQRSあり" {
  class Task2 {
    id: TaskId
    name: String
    status: TaskStatus

    getId(): TaskId
    getName(): String
    getStatus(): TaskStatus
  }

  class TaskDto {
    id: String
    name: String
    status: String
  }

  interface TaskQueryService {
    findById(id: TaskId): TaskDto
  }

  class TaskQueryServiceImpl {
    findById(id: TaskId): TaskDto
  }

  class TaskController2 {
    showTask(id: TaskId): View
  }

  TaskController2 --> TaskQueryService: 参照
  TaskQueryService ..> TaskDto: 返却
  TaskQueryServiceImpl --> Task2: 内部で参照
  TaskQueryService <|.. TaskQueryServiceImpl

  note bottom of TaskController2
    DTOを介して間接的に参照
    getterからの追跡が困難になる
  end note
}
@enduml
```

このように、メリットは非常に大きいですが、デメリットも確実にあるため、常に使用すれば良いというものではありません。しかし、「問題」の最後に述べた通り、DDDを用いている限りは避けられない重要な問題を解決する手法です。問題の大きさ、メリットデメリットを考慮して導入判断できることが重要です。

## 8.4 実装時の注意事項

### 8.4.1 部分的導入の可否

誤解されることがありますが、CQRSは部分的な導入が可能です。

つまり、「参照用モデルと更新用モデルを完全に分ける必要はない」ということです。必要なところだけ参照に特化したモデルを導入するのが適切です。

```plantuml
@startuml
package "部分的CQRS導入" {
  package "更新系モデル" {
    class Task {
      id: TaskId
      name: String
      status: TaskStatus

      complete(): void
      postpone(): void
    }

    interface TaskRepository {
      findById(id: TaskId): Task
      save(task: Task): void
    }
  }

  package "参照系モデル" {
    class TaskListDto {
      taskId: String
      taskName: String
      userName: String
      labelName: String
    }

    interface TaskListQueryService {
      fetchAll(): List<TaskListDto>
    }
  }

  package "両方で共有" {
    class User {
      id: UserId
      name: String
      email: String
    }

    interface UserRepository {
      findById(id: UserId): User
      findAll(): List<User>
      save(user: User): void
    }
  }
}

note bottom of "参照系モデル"
  複雑な一覧画面など
  パフォーマンスが重要な部分だけ
  参照専用モデルを導入
end note

note bottom of "両方で共有"
  シンプルなCRUDで
  パフォーマンス問題がない部分は
  従来通りリポジトリを使用
end note
@enduml
```

### 8.4.2 型を定義するレイヤーがアプリケーション層である理由

なぜ参照用モデルの型をアプリケーション層に定義するのでしょうか。それは、最適な参照用モデルは個別のユースケースに依存したものだからです。

例えば、タスク一覧画面とは別に、自分のタスクだけを表示する画面があったとします。

```plantuml
@startuml
!define RECTANGLE class

RECTANGLE "自分のタスク" as MyTaskList {
  タスク名 | ステータス | 期限
  --
  買い物 | 未完了 | 2023/5/1
  レポート作成 | 完了 | 2023/4/15
}
@enduml
```

その他に、ユーザーごとに最後に完了したタスクを表示する画面もあったとします。

```plantuml
@startuml
!define RECTANGLE class

RECTANGLE "最後に完了したタスク" as LastCompletedTask {
  ユーザー | タスク名 | 完了日時
  --
  山田太郎 | レポート作成 | 2023/4/15 10:30
  佐藤花子 | 会議資料準備 | 2023/4/14 15:45
}
@enduml
```

表示時に取得する内容は画面、ユースケースによって異なっています。そのため、取得内容を定義する型は個別に定義するべきものです。そして、ドメイン知識(
ルール・制約)は一切表現していないので、ドメイン層の責務には含めるべきではないでしょう。以上のことから、アプリケーション層に定義します。

また、これらの戻り値の型は完全に一致しない限り使い回すべきではありません。あるDTOには10個の項目があり、ユースケースAでは1～5個目を、ユースケースBでは3,
4, 7～10個目を使用する、ユースケースCでは・・・という風に最大公約数な項目を持つDTOを定義してしまうと、どこで何を使っているのかがわからなくなり、肥大化して保守性がどんどん落ちていきます。

ここでも責務を意識することが重要です。ユースケースA、B、Cで使い回すクラスは、「このクラスは何をするクラスか？」という問いに端的に答えられるでしょうか？ユースケースごとに型を分ければ、「このクラスは×××ユースケースで取得する値を表現するクラス」というように責務が明確に定義できます。

```plantuml
@startuml
package "良い設計: 個別のDTO" {
  class TaskListDto {
    taskId: String
    taskName: String
    userName: String
    labelName: String
  }

  class MyTaskDto {
    taskId: String
    taskName: String
    dueDate: Date
    status: String
  }

  class LastCompletedTaskDto {
    userName: String
    taskName: String
    completedAt: DateTime
  }

  note bottom of TaskListDto
    タスク一覧画面用
    責務が明確
  end note

  note bottom of MyTaskDto
    自分のタスク画面用
    責務が明確
  end note

  note bottom of LastCompletedTaskDto
    最後に完了したタスク画面用
    責務が明確
  end note
}

package "悪い設計: 共通DTO" {
  class CommonTaskDto {
    taskId: String
    taskName: String
    userName: String
    labelName: String
    dueDate: Date
    status: String
    completedAt: DateTime
    // その他多数のプロパティ
  }

  note bottom of CommonTaskDto
    複数の画面で共有
    責務が不明確
    使用されないプロパティが多い
    変更の影響範囲が広い
  end note
}
@enduml
```

### 8.4.3 更新系との整合性を確保する方法

アプリケーション層でテストを書きましょう。参照処理だけのテストだけでも書くのが最低限ですが、業務上重要な部分に関しては、更新処理との結合テストを書くと良いでしょう。

例えば、承認処理をした結果を一覧画面に反映する、といったケースでは、承認処理のアプリケーションサービスと、参照処理のクエリサービスを続けて呼び出すテストを書きましょう。

モデルを分けても、テストさえあればバグの混入を防ぐことは可能です。

```plantuml
@startuml
class TaskApprovalTest {
  +testApprovalReflectedInList()
}

class TaskApprovalService {
  +approve(taskId: TaskId): void
}

interface TaskRepository {
  findById(id: TaskId): Task
  save(task: Task): void
}

interface TaskListQueryService {
  fetchAll(): List<TaskListDto>
}

TaskApprovalTest --> TaskApprovalService: 1. 承認処理を実行
TaskApprovalTest --> TaskListQueryService: 2. 一覧を取得して検証
TaskApprovalService --> TaskRepository: 使用
TaskRepository ..> Task: 返却

note right of TaskApprovalTest
  更新処理と参照処理を
  続けて呼び出すテストで
  整合性を確認
end note
@enduml
```

## 8.5 よくある誤解

### 8.5.1 データソース分離の必要性

データソースを分離するアーキテクチャは、以下のようなものです。

```plantuml
@startuml
package "クライアント" {
  [Webブラウザ]
  [モバイルアプリ]
}

package "アプリケーション" {
  package "更新系" {
    [コマンドAPI]
    [コマンドモデル]
    database "更新系DB" {
      [マスターデータ]
    }
  }

  package "参照系" {
    [クエリAPI]
    [クエリモデル]
    database "参照系DB" {
      [レプリカデータ]
      [マテリアライズドビュー]
    }
  }

  [同期処理]
}

[Webブラウザ] --> [コマンドAPI]: 更新
[Webブラウザ] --> [クエリAPI]: 参照
[モバイルアプリ] --> [コマンドAPI]: 更新
[モバイルアプリ] --> [クエリAPI]: 参照

[コマンドAPI] --> [コマンドモデル]
[コマンドモデル] --> [マスターデータ]: 書き込み

[クエリAPI] --> [クエリモデル]
[クエリモデル] --> [レプリカデータ]: 読み取り
[クエリモデル] --> [マテリアライズドビュー]: 読み取り

[マスターデータ] --> [同期処理]: データ変更
[同期処理] --> [レプリカデータ]: 複製
[同期処理] --> [マテリアライズドビュー]: 更新

note bottom of "更新系"
  トランザクション整合性を重視
  書き込み最適化
end note

note bottom of "参照系"
  読み取り性能を重視
  結果整合性を許容
end note
@enduml
```

「CQRS = データソース分離」と思われることがありますが、これは誤解です。

データソース分離は、別の問題の解決のために行う、モデル分離の次のステップと考えることができます。解決する問題は、パフォーマンス問題です。

参照系処理のパフォーマンスを上げたい場合、参照用インスタンスを更新系のレプリカにすれば、参照系のインスタンスだけスケールアウトするなどのパフォーマンスチューニングが可能になります。インスタンス単位で分けなくても、参照系処理向けにマテリアルビューを作成する、というものデータソース分離の一環として考えることができます。

また、更新系処理のパフォーマンスを上げたい場合は、更新時にはNoSQLに高速に書き込んでおき、参照時には集計結果を表示するといった手段などが選択可能です。

データソース分離が解決する問題はモデル分離とは別のものなので、きちんと問題と解決策が対応しているかを判断して、導入を判断する必要があります。

### 8.5.2 イベントソーシングとの関係

「CQRS = イベントソーシング」も誤解です。

こちらもCQRSの文脈でよく一緒に出てくるので混乱しやすいですが、「CQRSとイベントソーシングは相性が良い」というだけであり、その二つは別で考えることができます。

イベントソーシングとは、データ永続化時にドメインオブジェクトの状態をそのまま保存するのではなく、「ユーザーが登録された」「タスクが完了された」といったイベントそのものを永続化するというアーキテクチャです。

```plantuml
@startuml
package "従来の永続化" {
  class Task {
    id: TaskId
    name: String
    status: TaskStatus
  }

  database "データベース" {
    [タスクテーブル]
  }

  Task --> [タスクテーブル]: 状態を直接保存

  note bottom of [タスクテーブル]
    id | name | status
    --+------+-------
    1 | 買い物 | 未完了
    2 | レポート | 完了
  end note
}

package "イベントソーシング" {
  class TaskEvent {
    eventId: EventId
    taskId: TaskId
    type: EventType
    data: EventData
    timestamp: DateTime
  }

  database "イベントストア" {
    [イベントテーブル]
  }

  TaskEvent --> [イベントテーブル]: イベントを保存

  note bottom of [イベントテーブル]
    eventId | taskId | type | data | timestamp
    --------+--------+------+------+----------
    1 | 1 | 作成 | {name: "買い物"} | 2023-01-01 10:00
    2 | 2 | 作成 | {name: "レポート"} | 2023-01-02 11:00
    3 | 2 | 完了 | {} | 2023-01-03 15:30
  end note
}
@enduml
```

参照時にすぐクエリできるように、参照用データを別途集計する処理を挟むと、必然的に参照/更新のモデルの分離や、データソースの分離が行われます。

つまりイベントソーシングはCQRS、データソース分離と併せて導入されますが、CQRS=イベントソーシングではないのです。

なお、イベントソーシングの導入目的はパフォーマンス最適化以外に、データを積み上げ型で永続化して証跡を残せることや、データ更新によるバグ発生を防ぐことなどが挙げられます。

## 8.6 Q&A

### 8.6.1 クエリサービスの分割判断

*

*

1回DBから取得した結果を用いて再度検索をしたい場合、アプリケーションサービスクラスで複数のクエリサービスを使っても良いでしょうか？それとも、1つのクエリサービスでアプリケーションサービスを通さず、全処理を行ってDTOを作るほうが良いのでしょうか？
**

2つの検索の関係がシンプルであれば、1つのクエリサービスでも良いと思います。1つ目の検索結果に応じて2つ目の検索条件を変えるなど、クエリサービス内の処理が複雑になる場合は、分離した方がテストが書きやすくなるので良いでしょう。

```plantuml
@startuml
package "シンプルな場合: 1つのクエリサービス" {
  class TaskListQueryService {
    fetchTasksWithUserAndLabel(): List<TaskListDto>
  }

  note bottom of TaskListQueryService
    1つのクエリサービスで
    複数テーブルを結合して取得
  end note
}

package "複雑な場合: 複数のクエリサービス" {
  class TaskQueryService {
    fetchTasks(): List<TaskDto>
  }

  class UserQueryService {
    fetchUsersByIds(ids: List<UserId>): List<UserDto>
  }

  class LabelQueryService {
    fetchLabelsByIds(ids: List<LabelId>): List<LabelDto>
  }

  class TaskListApplicationService {
    getTaskList(): List<TaskListDto>
  }

  TaskListApplicationService --> TaskQueryService: 1. タスク取得
  TaskListApplicationService --> UserQueryService: 2. ユーザー取得
  TaskListApplicationService --> LabelQueryService: 3. ラベル取得

  note bottom of TaskListApplicationService
    アプリケーションサービスで
    複数のクエリサービスを組み合わせて
    結果を構築
  end note
}
@enduml
```

### 8.6.2 DTOとして返した値の扱い

**クエリサービスが返すDTOは、コントローラー側でテンプレートエンジン用のViewModelや、APIが返すjson用クラスなどに変換するイメージでしょうか。
**

まさしくその通りです。クエリサービスの戻り値はクライアントに依存しない抽象的な型で、コントローラーがそこからクライアントに応じたクラスに詰め替えるのです。

```plantuml
@startuml
package "アプリケーション層" {
  class TaskListDto {
    taskId: String
    taskName: String
    userName: String
    labelName: String
  }

  interface TaskListQueryService {
    fetchAll(): List<TaskListDto>
  }
}

package "プレゼンテーション層" {
  class TaskListController {
    getTaskList(): String
    getTaskListJson(): ResponseEntity
  }

  class TaskListViewModel {
    tasks: List<TaskViewModel>
  }

  class TaskListJsonResponse {
    tasks: List<TaskJsonDto>
  }
}

TaskListController --> TaskListQueryService: 使用
TaskListController ..> TaskListViewModel: 変換(Web UI用)
TaskListController ..> TaskListJsonResponse: 変換(API用)

note right of TaskListController
  コントローラーがDTOを
  クライアント固有の形式に変換
end note
@enduml
```

### 8.6.3 参照用モデルの使い所

**何らかのマスタデータを使用する際、そのマスターのメンテナンス画面では更新用モデルを使うと思いますが、セレクトボックスなどで表示用にデータを取得するのはクエリサービスでよいのでしょうか？
**

「8.4.1
部分的導入の可否」の節で、「必要なところだけ参照に特化したモデルを導入する」のが良いと書きました。どのような画面でも、参照用モデルを定義する必要性がなければ、更新用モデルであるドメインオブジェクトを使用します。

そのため、セレクトボックスのデータを取得するのに、何らかの必要性がなければクエリサービスを定義せず、リポジトリを利用する方法で構いません。

### 8.6.4 実装の都合にドメイン層が影響を受ける場合

**N+1クエリやトランザクションの都合に合わせて、泣く泣くドメインモデルを変更することがあるのですが、そのようにアプリケーション全体の観点から仕方なくドメインモデルを曲げられることはありますか？
**

トランザクション範囲を考慮して集約の範囲を検討することは必要なことなので、よくあります。

しかし、N+1問題という参照時の(しかも、使用するライブラリにも依存する)
問題に起因してドメインモデルが歪められるのは望ましくありません。そのような事情はインフラ層に隠蔽できないか検討し、必要であればCQRSの導入も選択肢に入れます。

### 8.6.5 集約内の一部の値だけ取得したい場合の対応方法

**集約内の要素である大きめの値オブジェクトだけデータを取得したい場合はどのようにすれば良いでしょうか？
**

基本的にはリポジトリでドメインオブジェクトを取得し、アプリケーションサービスの処理で必要な項目だけ詰め替えれば良いですが、パフォーマンスが問題になる場合はCQRSの導入を検討します。

```plantuml
@startuml
package "集約" {
  class Order {
    id: OrderId
    customerInfo: CustomerInfo
    items: List<OrderItem>
    shippingAddress: Address
    billingAddress: Address

    getShippingAddress(): Address
  }

  class CustomerInfo {
    name: String
    email: String
    phone: String
  }

  class Address {
    street: String
    city: String
    state: String
    zipCode: String
    country: String
  }

  class OrderItem {
    productId: ProductId
    quantity: int
    price: Money
  }

  Order *-- CustomerInfo
  Order *-- "1..*" OrderItem
  Order *-- "shipping" Address
  Order *-- "billing" Address
}

package "アプリケーション層" {
  class ShippingAddressDto {
    orderId: String
    street: String
    city: String
    state: String
    zipCode: String
    country: String
  }

  class OrderRepository {
    findById(id: OrderId): Order
  }

  class ShippingAddressQueryService {
    findByOrderId(orderId: OrderId): ShippingAddressDto
  }

  class ShippingApplicationService {
    getShippingAddress(orderId: OrderId): ShippingAddressDto
  }

  ShippingApplicationService --> OrderRepository: 使用
  ShippingApplicationService ..> ShippingAddressDto: 変換

  note bottom of ShippingApplicationService
    リポジトリで集約全体を取得し
    必要な部分だけDTOに変換
  end note

  note bottom of ShippingAddressQueryService
    パフォーマンスが問題になる場合は
    CQRSを導入して必要な部分だけ
    効率的に取得
  end note
}
@enduml
```
