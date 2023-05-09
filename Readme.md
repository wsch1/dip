## Инструкция по запуску авто-тестов.

### Требуемые приложения для запуска.
* IDEA
* GIT
* NodeJS. 
* Docker. 
* Google Chrome браузер.

### Шаги для запуска
1. Создать Gradle проект в нужной вам папке
2. С помощью команды `git init` создать локальный репозиторий.
3. С помощью команды `git clone https://github.com/IqaEnganer/CoursePaper.git` клонировать удаленный.
4. Запустить Docker Desktop.
6. С помощью команды `docker-compose up` создать контейнеры и запустить их.
7. С помощью команды `java -jar artifacts/aqa-shop.jar` запустить SUT.
8. Проверить работоспособность [SUT](http://localhost:8080/)
9. Запустить тесты с помощью команды `./gradlew test`

### Настройки для запуска на СУБД Postgresql.
#### Запуск приложения происходит со специальным флагом.
* ` Java -jar artifacts/aqa-shop.jar --spring.profiles.active=post `
* Для запуска тестов `./gradlew test -D db.url=jdbc:postgresql://localhost:5432/app`

### Документация 
* [План тестирования](documentation/Plan.md)
* [Проделанная работа](documentation/Summary.md)
* [Отчет](documentation/Report.md)

