`CardDisplayFragment` 클래스는 루틴 실행 중인 카드의 상세 정보를 표시하는 안드로이드 프래그먼트입니다. 이 프래그먼트의 주요 기능과 로직은 다음과 같습니다:

1. **데이터 바인딩 및 뷰모델 초기화**: `FragmentCardDisplayBinding`을 사용하여 레이아웃과 연결하고, `RoutineProgressViewModel`을 통해 루틴 실행 관련 데이터를 관리합니다.

2. **현재 카드 정보 표시**: 루틴에서 현재 진행 중인 카드의 정보를 화면에 표시합니다. 카드의 이름, 세트 수, 상태 등을 업데이트합니다.

3. **타이머 및 프로그레스 바 업데이트**: 현재 카드의 타이머 시간과 진행 상태에 따라 프로그레스 바를 업데이트합니다. 타이머는 시, 분, 초 단위로 표시됩니다.

4. **프로그레스 바 색상 설정**: 카드의 상태(준비, 활동, 휴식)에 따라 프로그레스 바의 색상을 변경합니다. 이를 위해 `GradientDrawable`의 색상을 조정합니다.

5. **카드 상태에 따른 텍스트 업데이트**: 카드의 상태에 따라 해당하는 상태 메시지를 표시합니다 (예: "준비하세요!", "진행 중!", "다음 세트를 준비하세요!").

6. **메모 텍스트 감시 및 업데이트**: `RoutineProgressActivity`의 메모 필드에 현재 카드의 메모를 업데이트합니다.

이 프래그먼트는 사용자가 루틴을 실행하는 동안 각 카드의 상세 정보를 보고, 시간을 추적하며, 상태를 확인할 수 있도록 디자인되었습니다. 카드의 진행 상황을 시각적으로 쉽게 인식할 수 있게 하며, 사용자가 루틴을 효과적으로 관리할 수 있도록 지원합니다.