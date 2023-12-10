# moblie_programing 팀 2
## Routine Mate 팀 프로젝트

# Routine Mate

Routine Mate는 사용자의 일상을 관리하는 Android 앱입니다. 사용자는 자신의 루틴을 등록하고, 루틴을 수행할 때마다 별을 획득합니다.


# 현재 버전

현재 버전은 1.1.0입니다.

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

## 주요 흐름

이 안드로이드 애플리케이션은 사용자들이 개인 루틴을 관리하고 실행할 수 있는 기능을 제공합니다. 주요 흐름과 클래스들은 다음과 같습니다:

1. **로그인 및 사용자 관리 (`LoginActivity`, `UserProfileActivity`)**: 사용자는 Google 로그인을 통해 앱에 접근하고, 자신의 프로필을 관리할 수 있습니다. 프로필 관리에는 별 백분율 확인, 로그아웃, 계정 삭제 기능이 포함됩니다.

2. **루틴 목록 관리 (`MainActivity`, `RoutineAdapter`, `ItemTouchHelperCallback`)**: 메인 화면에서 사용자는 자신의 루틴 목록을 확인하고, 루틴을 추가, 수정, 삭제할 수 있습니다. 루틴은 드래그 앤 드롭 및 스와이프를 통해 순서를 변경하거나 제거할 수 있습니다.

3. **루틴 상세 및 카드 관리 (`RoutineDetailActivity`, `RoutineDetailCardAdapter`, `ItemTouchHelperCallbackForCard`)**: 사용자는 각 루틴을 클릭하여 상세 페이지로 이동할 수 있으며, 여기에서 개별 카드를 관리할 수 있습니다. 카드는 시간, 세트, 메모 등의 정보를 포함하며, 수정 및 삭제가 가능합니다.

4. **루틴 실행 (`RoutineProgressActivity`, `RoutineProgressViewModel`, `CardDisplayFragment`)**: 사용자가 루틴을 실행하면, 진행 중인 루틴과 각 카드의 상세 정보가 시간과 함께 표시됩니다. 타이머는 루틴과 각 카드에 대해 별도로 작동하며, 일시 정지 및 재개 기능을 지원합니다.

5. **루틴 완료 처리 (`RoutineCompleteActivity`)**: 루틴을 모두 완료하면 사용자는 총 소요 시간과 함께 완료 화면을 볼 수 있습니다.

6. **데이터 관리 및 저장 (`MainViewModel`, `RoutineProgressViewModel`, `RoutineRepository`)**: ViewModel을 통해 사용자 인터페이스와 데이터 로직을 분리하여 관리합니다. Firebase를 이용하여 사용자 데이터, 루틴 및 카드 정보를 저장하고 관리합니다.


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
## readmes 폴더에는 각 클래스에 대한 상세 설명이 있습니다.
[모든 클래스 상세 설명](readmes)
