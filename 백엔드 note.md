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









### 자바 비즈니스 애플리케이션 클래스의 2가지 종류

1. 기능을 수행하는 클래스 : 컨트롤러, 서비스, 퍼시스턴스처럼 로직을 수행
2. 데이터를 담는 클래스 : 기능에 따라 엔티티, 모델, DTO 등으로 부르지만 이름에 큰 의미를 둘 필요는 없다.







### @Builder

- 오브젝트 생성을 위한 디자인 패턴 중 하나
- 롬복이 제공하는 @Builder 어노테이션을 사용하면 Builder 클래스를 따로 개발하지 않아도 된다.
- 생성자를 이용해 생성하는 것과 유사하지만, 생성자 매개변수의 순서를 기억할 필요가 없다는 장점이 있다.

```java
@Builder
public class Entity{
    private String id;
    private String userId;
}

Entity entity = Entity.builder()
    			.id("asdfasdf")
   				.userId("asdf")
   				.build();
```









### DTO(Data Transfer Object)

- 서비스가 요청을 처리하고 클라이언트로 반환할 때 모델 자체를 그대로 리턴하는 경우는 별로 없다.
  1. 비즈니스 로직을 캡슐화하기 위함이다. : DB의 스키마를 외부인에게 다 노출할 필요는 없다.
  2. 클라이언트가 필요한 정보를 모델이 모두 포함하지 않을 수 있다. : 에러 등의 정보는 모델에 없다.
- 엔티티 -> Dto,  Dto -> 엔티티 변환 로직

```java
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDto {
    private String id;
    private String title;
    private boolean done;

    public TodoDto(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    public static TodoEntity toEntity(final TodoDto dto){
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }
}
```







### 커멘드로 실행하기(빌드와 실행 등 모두 수행)

```
./gradlew bootRun
```







### REST 아키텍처

1. 클라이언트-서버
2. 상태가 없는(stateless)
3. 캐시되는(Cacheable) 데이터
4. 일관적인 인터페이스(Uniform Interface)
5. 레이어 시스템(Layered System)
6. 코드-온-디멘드(Code-On-Demand)(선택사항)







### Request 데이터 받기

- url 로 간단하게 전달할 때는 @PathVariable 이나 @RequestParam 사용
- 오브젝트 형태로 복잡한 데이터를 받을 때는 @RequestBody 사용 (파라미터로 DTO 받기)







### 객체를 JSON 으로 리턴하기

RestController이면 그냥 객체를 리턴하면 그대로 JSON으로 변환 후 리턴된다.





### 응답코드 설정 + 객체를 JSON 으로 리턴하기

- ResponseEntity<?> 를 사용하면 JSON 객체 뿐 아니라, 헤더와 Http Status 를 조작할 수 있다.

```java
@GetMapping("/testResponseEntity")
public ResponseEntity<?> testControllerResponseEntity() {
  List<String> list = new ArrayList<>();
  list.add("test list");
  ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
  return ResponseEntity.badRequest().body(response);
}
```







### UUID를 엔티티 key로 사용하기

- @GeneratedValue : ID를 자동으로 생성. generator로 어떤 방식으로 id를 생성할지 지정할 수 있다. 기본 Generator로는 INCREMENTAL, SEQUENCE, IDENTITY 등이 있는데, 자신이 커스텀해서 만든 Generator을 사용할 수 있다.
- @GenericGenerator: name으로 커스텀 generator을 만들고, strategy로 정책을 설정할 수 있다.

```java
@NoArgsConstructor
@Data
@Entity
public class TodoEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String userId;
    private String title;
    private boolean done;
}
```







### Spring data jpa 에서 쿼리문 사용하기

```java
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    
    // ?1은 메서드의 매개변수의 순서 위치다.
    @Query("select * from Todo t where t.userId = ?1")
    List<TodoEntity> findByUserId(String userId);
}
```





### exists

- 성능면에서 이득이다.
- 모든 데이터를 살펴보지 않고, 처음 만다는 데이터에서 바로 리턴을 하니 그럴수밖에..

```java
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Boolean existsByEmail(String email);
}
```

