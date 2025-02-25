目次
=====

#

# ビジネス層

## Query, Command, Coordinator

#

# ファイルダウンロード

```puml
@startuml
Client -> RequestController: post
RequestController --> Coordinator: registerReportRequest
RequestedReportListener --> CreateReportCoordinator: execute
CreateReportCoordinator  --> ReportDataQuery: findBy
CreateReportCoordinator  --> ReportCreator: create
CreatedReportListener --> finishRequest: execute

Client -> RequestController: get

@enduml


```
