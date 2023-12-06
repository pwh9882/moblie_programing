# moblie_programing 팀 2
## Routine Mate 팀 프로젝트

# Routine Mate

Routine Mate는 사용자의 일상을 관리하는 Android 앱입니다. 사용자는 자신의 루틴을 등록하고, 루틴을 수행할 때마다 별을 획득합니다.

## 개발 환경

| 도구 | 버전 |
|---|---|
| Android Studio | 2022.3.1 Patch 3 |
| Kotlin | 1.9.0 |
| Firebase | 20.3.0 |

## 기능

- 로그인과 회원가입
- 루틴 등록
- 루틴 수정
- 루틴 삭제
- 루틴 수행
- 루틴 수행 완료

## 실행 방법

1. Android Studio를 설치합니다.
2. 이 GitHub 저장소를 클론합니다.
3. Android Studio에서 프로젝트를 엽니다.
4. Android Studio에서 'Run'을 클릭하여 앱을 실행합니다.

## 참고 사항

- 이 앱은 Firebase를 사용하여 사용자 인증과 데이터 저장을 처리합니다. 따라서 Firebase 프로젝트를 설정하고, `google-services.json` 파일을 앱 디렉토리에 추가해야 합니다.
- Authentication의 Sign-in method 로그인 제공업체는 google을 활성화해야 합니다.
- 실시간 데이터베이스 규칙은 다음과 같이 설정해야 합니다. Routine by userId를 하기 위한 인덱싱을 추가했습니다.

```json
{
  "rules": {
    ".read": "true",
    ".write": "true",
    "Routine": { 
      ".indexOn": ["userId"]
    }
  }
}
```


## SDK 버전 (상세 내용은 build.gradle 참고)

- 최소 SDK 버전: 30 (Android 11.0, R)
- 대상 SDK 버전: 33 (Android 11.0, R)
- 컴파일 SDK 버전: 34 (Android 11.0, R)
- JVM 버전: 1.8 (Java 8)
- Kotlin 버전: 1.9.0
- Firebase database 버전: 20.3.0
- Google Services 버전: 4.4.0


## 4. 시연 영상
https://youtu.be/oIKH0-OAKKU?feature=shared

---------

# ./description.md

