Простое веб-приложение блога, реализованное с использованием Spring Framework.

Функционал включает в себя публикацию постов, комментарии и лайки с возможностью редактирования всего перечисленного.

## Возможности

- Просмотр всех постов и поиск по тегу
- Просмотр отдельного поста
- Добавление, редактирование и удаление постов
- Добавление, редактирование и удаление комментариев
- Увеличение/уменьшение количества лайков
- Загрузка изображений для постов

## Сборка и запуск

### 1. Клонирование репозитория

```bash 
git clone https://github.com/yjailbir/blog-app-yandex-practicum-sprint-3.git
```

### 2. Настройка свойств приложения

В файле src/main/resources/application.properties заполнить все поля:
- spring.datasource.url={url вашей базы данных (если это не Postgres, то нужно добавить в pom.xml зависимость на соответствующий драйвер)}
- spring.datasource.username={имя пользователя вашей базы данных}
- spring.datasource.password={пароль вашей базы данных}
- values.img_folder={полный путь к папке, где будут храниться изображения, загруженные в блог}

### 3. Сборка WAR-файла

1. Установить [Apache Maven](https://maven.apache.org/download.cgi)

2. Выполнить в каталоге проекта команду
```bash
mvn clean package
```

В результате сборки будет создан файл:  
`target/blog-app-yandex-practicum-sprint-3.war`

### 4. Деплой на Tomcat

1. Установить [Apache Tomcat](https://tomcat.apache.org/download-10.cgi)
2. Скопировать файл `blog-app-yandex-practicum-sprint-3.war` в директорию `webapps` Tomcat:
3. Запустите сервер: bin/startup.sh на Linux системах или bin/startup.bat на Windows 
4. Открыть в браузере домашнюю страницу блога:
   ```
   http://localhost:8080/blog_app_yandex_practicum_sprint_3_war/posts
   ```

### 5. Остановка сервера

Для остановки приложения запустите bin/shutdown.sh на Linux системах или bin/shutdown.bat на Windows