`RoutineProgressViewModel` 클래스는 안드로이드 애플리케이션에서 루틴 실행의 진행 상태를 관리하는 데 사용되는 ViewModel입니다. 이 클래스의 주요 기능과 로직은 다음과 같습니다:

1. **루틴 및 카드 데이터 관리**: 현재 진행 중인 루틴(`_currentRoutine`)과 카드(`_currentCard`), 카드의 인덱스(`_currentCardIndex`), 루틴과 카드의 타이머(`_currentRoutineTime`, `_currentCardTime`)를 관리하는 LiveData를 설정합니다.

2. **루틴 및 카드 데이터 업데이트**: `setCurrentCard`, `updateCurrentRoutineData`, `updateCurrentCardIndexData` 함수를 통해 현재 진행 중인 카드 및 루틴 데이터를 업데이트합니다.

3. **타이머 관리**: `startRoutineTimer`, `stopRoutineTimer`, `startCardTimer`, `stopCardTimer` 함수를 사용하여 루틴 및 카드의 타이머를 시작하고 중지합니다. 이 타이머들은 1초마다 갱신됩니다.

4. **일시 정지 및 재개 처리**: `pauseTimer`, `resumeTimer` 함수를 통해 타이머를 일시 정지하거나 재개할 수 있습니다. 이를 위한 `_isPaused` LiveData도 관리합니다.

5. **카드 진행 상태 및 세트 관리**: `setCardProgress`, `setCardSet` 함수를 통해 현재 카드의 진행 상태와 반복 횟수(세트)를 업데이트합니다.

6. **별 개수 증가**: `incrementUserStars` 함수를 사용하여 사용자의 별 개수를 증가시킵니다.

이 ViewModel은 사용자 인터페이스와 분리된 데이터 관리 및 비즈니스 로직 처리를 담당합니다. 사용자가 루틴을 진행하는 동안 카드의 타이머, 진행 상태, 세트 수 등을 관리하고, 전체 루틴의 진행 시간도 추적합니다. 또한 사용자가 타이머를 일시 정지하거나 재개할 수 있는 기능을 제공하여 유연한 사용 경험을 지원합니다.