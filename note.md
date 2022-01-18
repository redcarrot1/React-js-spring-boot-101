### 직렬화(Serialization), 역직렬화(Deserialization)

- 직렬화: 메모리 상의 오브젝트를 다른 형태로 변환하는 작업
  - 예를 들어 애플리케이션 1과 애플리케이션 2가 인터넷을 통해 통신을 한다고 가정했을 때, 서로 언어와 아키텍처가 다를 수 있다. 이때 둘 다 이해할 수 있는 형태로 오브젝트를 변환해야 한다.
- 역직렬화: 직렬화의 반대 작업



어떤 형태로 오브젝트를 직렬화할 것인가? -> JSON



- JSON 자료형
  - Boolean: true, false
  - 숫자: 쌍따옴표 없는 숫자
  - 문자열: 쌍따옴표로 감싼 형태
  - 오브젝트: 소괄호로 감싼 형태
  - 배열: 대괄호로 감싼 형태

```json
{
  "myString": "hello", //문자열
  "number": 10, //숫자
  "myStringArray": [ //문자열 배열
    "abc",
    "def"
  ],
  "myObject": { //오브젝트
    "name": "obj1"
  }
}
```









### Bean, DI, Component

- 스프링 부트의 가장 큰 특징을 꼽으라면 DI
- 오브젝트(Bean)을 의존성 주입 컨테이너 오브젝트(ApplicationContext)에 등록 후, 오브젝트가 필요한 경우(@Autowired) 의존하는 오브젝트를 찾아 주입해준다.
- 자바 빈 등록은 클래스 단위로 @Component를 이용할 수 있다. (@Controller, @Service, @Repository 에 이미 포함)
- 만약 @Component가 아닌, 직접 빈을 관리하려면 @Bean 을 사용하면 된다.









### 레이어드 아키텍처(Layered Architecture)

리퀘스트 -> 프레젠테이션 레이어(Controller) -> 비즈니스 레이어(Service) -> 퍼시스턴스 레이어(Persistence) -> 데이터베이스 레이어(DB)

상위 레이어는 자신의 바로 하위 레이어만 사용한다.







### 간단한 JSON 컨트롤러에서 리턴하기

```java
// 방법1 : HashMap 사용
@PostMapping("/login")
public HashMap<String, Object> isDuplicationEmail(@RequestBody RequestLoginForm form) {
  String loginToken = memberService.login(form.getEmail(), form.getPassword());

  HashMap<String, Object> result = new HashMap<String, Object>();
  result.put("token", loginToken);
  return result;
}

// 방법2 : JSONObject 사용
// 단, Content-Type이 json이 아닌 text/plain;charset=UTF-8으로 설정된다.
@PostMapping("/login")
public String isDuplicationEmail(@RequestBody RequestLoginForm form) {
  String loginToken = memberService.login(form.getEmail(), form.getPassword());

  JSONObject json = new JSONObject();
  json.put("token", loginToken);
  return json.toString();
}
```

