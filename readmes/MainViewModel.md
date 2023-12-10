`MainViewModel` 클래스는 안드로이드 애플리케이션에서 루틴 관리와 사용자 별 개수 관련 데이터를 관리하는 데 사용되는 ViewModel입니다. 이 클래스의 주요 기능과 로직은 다음과 같습니다:

1. **Repository 초기화**: `RoutineRepository`를 사용하여 루틴 데이터에 접근합니다.

2. **LiveData 설정**: 루틴 목록(`_routineList`)과 사용자 별 개수(`_starCount`)를 관리하는 LiveData를 설정합니다.

3. **루틴 목록 및 별 개수 업데이트**: `updateRoutineListData` 및 `fetchUserStarCount` 함수를 통해 Firebase에서 루틴 데이터와 사용자의 별 개수를 가져와 업데이트합니다.

4. **루틴 생성, 수정, 삭제**: `createRoutine`, `updateRoutine`, `deleteRoutine` 함수를 사용하여 Firebase에 루틴을 추가, 업데이트, 삭제합니다. 이 과정에서 루틴 목록과 별 개수도 업데이트합니다.

5. **사용자 별 백분율 계산**: `getUserStarPercentile` 함수를 통해 사용자의 별 백분율을 계산합니다. 이는 `calculatePercentile` 함수를 사용하여 계산되며, 소수점 아래 세 자리까지 반올림됩니다.

6. **루틴 목록 정렬**: 루틴 목록은 마지막 수정 시간의 내림차순으로 정렬됩니다.

이 ViewModel은 사용자 인터페이스와 분리된 데이터 관리 및 비즈니스 로직 처리를 담당합니다. 루틴 데이터의 CRUD 작업을 처리하고, 사용자의 성과를 나타내는 별 개수 및 백분율 계산 기능을 제공합니다. ViewModel의 사용은 애플리케이션의 반응성을 향상시키고, 데이터 관리를 보다 효율적으로 만듭니다.