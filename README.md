 # Тестовое задание
Необходимо написать 2 сервиса:
-  Сервис по приему сообщений. 1 endpoint.
a. POST запрос: Принимает в себя объект, содержащий в себе поля Отправитель(string) и Сообщение(string). Content type application/xml.
(1) При получении сообщения, сервис через Кафку отправляет данные второму сервису на отправку. Topic – message.send
b. GET запрос: Выводит список сообщений по пользователю, если передан и если не передан, то последние 10 сообщений
-  Сервис по отправке сообщений 1 endpoint. Должен быть Kafka consumer, который при получении сообщения, отправляет Email на адрес, указанный в файле конфигурации и сохраняет код ответа сервера в базу данных.
a. GET запрос: Выводит список отправленных сообщений и код ответа от сервера email

Требования:
- Срок: 72 часа.
- Spring boot
- Java 11 или новее
- PostgreSQL
- Hibernate для работы с базой данных
-  Необходимо залить в гит и отправить ссылки на репозитории
-  В readme.md необходимо написать инструкцию по разворачиванию и документацию по методам

Для сборки и запуска приложения вам необходимо:
- Java 17
- Maven 3
- Apache Kafka (running)
- SMTP Email Server (e.g., Gmail)

## Описание сервисов

### Структура message-service-receiver
```
├── pom.xml
├── src
    └── main
        ├── java
        │   └── com
        │       └── example
        │           └── messageservicereceiver
        │               ├── MessageServiceReceiverApplication.java  //главный класс приложения с методом main.
        │               ├── config
        │               │   └── KafkaConfig.java                    //класс содержит конфигурацию для Kafka-продюсеров,Определяет фабрики Kafka-продюсеро
        │               ├── controller
        │               │   └── MessageController.java              //класс контроллера, который определяет конечные точки REST API 
        │               ├── entity
        │               │   ├── Information.java                    //класс содержащий в себе поля Id,Отправитель(string),Сообщение(string) для POST запроса send-message и для сериализаций
        │               │   └── Message.java                        //сущностный класс для взаимодействия с базой данных,имеет поля id, sender, message, emailResponseCode
        │               ├── impl
        │               │   └── MessageServiceImpl.java             //класс реализует интерфейс MessageService, содержит бизнес-логику для отправки сообщений и взаимодействия с темами Kafka
        │               ├── repository
        │               │   └── MessageRepository.java              //Интерфейс репозитория, используемый для операций CRUD с сущностью Message
        │               └── service
        │                   └── MessageService.java                 //сервисный интерфейс, который определяет методы
        └── resources
            └── application.yml 
 ```
 
 Для корректной работы вашего приложения в IntelliJ IDEA или другой среде разработки вам нужно задать значения переменных окружения или использовать те, что указаны после двоеточия файле application.yml. Вы можете поступить следующими способами:
- Установите значения переменных окружения в вашей среде разработки, если хотите использовать конкретные значения для  `KAFKA_BOOTSTRAP_SERVERS `,  `DB_URL `,  `DB_USERNAME` и `DB_PASSWORD `.
- Если не установлены переменные окружения, то будут использованы значения по умолчанию, которые указаны после двоеточия 
- Вы также можете оставить значения по умолчанию, если они удовлетворяют вашим требованиям.
 ```
 spring:
  # Kafka Producer Configuration
  kafka:
    producer:
      bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:29092}
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
  # Database Configuration
  datasource:
    url: jdbc:postgresql://${DB_URL:localhost:5432/ts_property_management}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:asdasd123}
  jpa:
    show-sql: true
 ```
 
 | API | METHOD | PARAMETER |DESCRIPTION|
| ------ | ------ | ------ | ------ |
| /send-message | POST |@RequestBody Information information|Принимает application/xml сообщение, сохраняет в таблице message, сообщение отправляется в Kafka на указанный топик `message.send`
| /list | GET |@RequestParam(required = false) String sender|Если sender не null выводит все сообщений этого sender, а если null то последние 10|

> NOTE: чтобы принимался application/xml использовался 
    	<dependency>
			<groupId>com.fasterxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-xml</artifactId>
		</dependency>
		
### Структура message-service-receiver

 ```
├── pom.xml
├── src
   └── main
       ├── java
       │   └── com
       │       └── example
       │           └── messageservicesender
       │               ├── MessageServiceSenderApplication.java  //главный класс приложения с методом main.
       │               ├── config
       │               │   ├── EmailConfig.java                  //класс является конфигурацией для настроек электронной почты 
       │               │   └── KafkaConfig.java                  //класс содержит конфигурацию для Kafka-консюмеров
       │               ├── controller
       │               │   └── MessageController.java            //класс контроллера, который определяет конечные точки REST API 
       │               ├── entity
       │               │   ├── Message.java                       //сущностный класс для взаимодействия с базой данных,имеет поля id, sender, message, emailResponseCode
       │               │   └── listener
       │               │       └── Information.java               //класс содержащий в себе поля Id,Отправитель(string),Сообщение(string) и для десериализаций
       │               ├── impl
       │               │   ├── EmailServiceImpl.java              //класс реализует интерфейс EmailService и предоставляет методы для отправки электронных писем
       │               │   └── MessageServiceImpl.java            //класс реализует интерфейс MessageService и предоставляет методы для работы с сообщениями
       │               ├── listener
       │               │   └── MessageListener.java               //класс служит в качестве слушателя сообщений из Apache Kafka
       │               ├── repository
       │               │   └── MessageRepository.java             //Интерфейс репозитория, используемый для операций CRUD с сущностью Message
       │               └── service
       │                   ├── EmailService.java                  //сервисный интерфейс, который определяет методы
       │                   └── MessageService.java                //сервисный интерфейс, который определяет методы
       └── resources
           └── application.yml 
 ```
 Для корректной работы вашего приложения в IntelliJ IDEA или другой среде разработки вам нужно задать значения переменных окружения или использовать те, что указаны после двоеточия файле application.yml. Вы можете поступить следующими способами:
- Установите значения переменных окружения в вашей среде разработки, если хотите использовать конкретные значения для KAFKA: `KAFKA_BOOTSTRAP_SERVERS `,  `KAFKA_GROUP_ID`,`DB_URL `;  DB: `DB_USERNAME`, `DB_PASSWORD `; MAIL: `MAIL_USERNAME` , `MAIL_PASSWORD`,  `EMAIL_TO`.
- `EMAIL_TO` это почта на которую будет отправляться сообщение 
- Если не установлены переменные окружения, то будут использованы значения по умолчанию, которые указаны после двоеточия 
- Вы также можете оставить значения по умолчанию, если они удовлетворяют вашим требованиям.
 ```
 # Application
server:
  port: 8081
spring:
  # Database
  datasource:
    url: jdbc:postgresql://${DB_URL:localhost:5432/ts_property_management}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:asdasd123}
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
  # Kafka Consumer Configuration
  kafka:
    consumer:
      bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:29092}
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonSerializer
      group-id: ${KAFKA_GROUP_ID:message-consumer-group}
      properties:
        spring.json.trusted.packages: "*"
      auto-offset-reset: earliest
  # Email
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME:soondet2@gmail.com}
    password: ${MAIL_PASSWORD:vwdvapekdmckidnq}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
email:
  address: ${EMAIL_TO:soondet@mail.ru}
 ```
 
  | API | METHOD | PARAMETER |DESCRIPTION|
| ------ | ------ | ------ | ------ |
| @KafkaListener consume()| |Information information|Метод принимает объект типа Information и отправляет сообщение на почту по адресу  `EMAIL_TO ` от имени пользователя  `MAIL_USERNAME ` с темой "Тестовое задание". Если сообщение успешно отправлено, то объект Information с необходимым идентификатором (Id) сохраняется в таблице message с кодом ответа  `emailResponseCode ` равным  `200 `. Если возникает ошибка типа MailException, то код ответа устанавливается в  `400 `, а в случае других исключений (Exception), код ответа устанавливается в  `500 `.
| /list | GET ||Вывод всех сообщений|
## Запуск

1. Запустить контейнеры в фоновом режиме, docker-compose.yml файле определены три контейнера:
- zookeeper, port: 22181:2181, image: confluentinc/cp-zookeeper:latest
- kafka, port: 29092:29092, image: confluentinc/cp-kafka:latest
- kafdrop, port: 9000:9000, obsidiandynamics/kafdrop:latest
```shell
docker-compose up -d
```



2. Установите PostgreSQL, запустите PostgreSQL сервер, создайте базу данных `ts_property_management`
3. Перед тем как запустить оба сервиса, вы должны выбрать один из двух способов настройки переменных окружения и конфигурационных значений, в зависимости от ваших предпочтений: 1)Задать значения переменных окружения перед запуском сервисов. 2)Сервисы могут быть запущены с дефолтными значениями.

> NOTE:  Перед запуском сервисов обязательно убедитесь, что значение переменной `EMAIL_TO` или дефолтное значение установлено на ваш адрес электронной почты. Это необходимо для того, чтобы убедиться, что сообщения будут успешно отправляться на вашу почту.

4. Запустить оба Spring Boot приложения
- message-service-receiver, port:8080
- message-service-sender, port:8081
