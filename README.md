# kotlin-spring-ddd

## Run

```bash
# set up local db
docker-compose -f docker-compose.local.yml up
# run spring application
./gradlew bootRun
# request
curl http://localhost:8080/hello
```

## Development

1. generate idea project config

```bash
./gradlew idea
```

2. enable git hooks

```bash
git config --local core.hooksPath .githooks/
chmod 700 .githooks/pre-push
```

3. config your IDEA

- Import code style
  - Open IDEA, enter `Preferences -> Editor -> Code Style -> Import Schema`
  - Choose `config/GoogleStyle.xml`
    > This style is based on GoogleStyle and default Kotlin style of Intellij Team, is suitable for both Java and Kotlin.
- New line end of file
  - Enter `Preferences -> Editor -> General`
  - Check on `Other: Ensure line feed at file end on Save`
- Automatic format before commit when using IDEA commit dialog
  - Enter `Preferences -> Version Control -> Commit Dialog`
  - Check on all options (exclude copyright)

## Test

1. init test & integration test

```bash
# unit test & lint
./gradlew clean check
# api test (must run after test)
./gradlew clean check apiTest
# (optional) test coverage report
./gradlew jacocoTestReport
```

2. container test

```bash
# package
./gradlew clean build bootJar
# start in test container
docker-compose -f docker-compose.test.yml up --build
```

## Deploy (locally)

```bash
# build image
docker build -t kotlin-spring-ddd .
# inject environment variables, check details in config/application-dev.yml
docker run -p 8080:8080 <environment> kotlin-spring-ddd
```

**Should put this part into CI/CD, DON'T manually deploy in production**
**If you want to check it locally, please try container test**

# Features

- [x] 权限管理: 用户 角色 资源
  - [x] 使用 code 作为 permission 主键 instead `id`
  - [x] ~~Add `resource url` mapping to permission, 通过 filter 自动校验权限~~, 使用 annotation -[x] 登录校验, security
  - [x] JWT, 可跨应用
  - [x] ~~OAuth: 多 client~~
- [x] Spring Cache: method level
- [x] field validation
  - [x] complex model validation
  - [x] mess validation
- [x] integration test,jpa test, unit test, mockk, assertj
- [ ] logging
  - [ ] DB audit logging
  - [x] request/response logging - once request filter
  - [x] security logging - debug logging on
- [x] jpa, ~~mybatis,~~ hibernate
  - hibernate: More suitable object-oriented, modeling, DDD
  - mybatis: Only use it if you have a lot of join/union/other complex DB operation
  - [Hibernate vs MyBatis](https://www.zhihu.com/question/21104468),
    Hibernate is more advantageous than mybatis in most of scenario.
  - [ ] hibernate cache: second level
- [x] jacoco, ~~ktlint,~~ detekt
  - [x] detekt include most of features of ktlint, idea also provides good lint.
- [x] exception, http status, handler
- [ ] contract testing with wiremock
- [ ] model mapping
- [ ] split api test
