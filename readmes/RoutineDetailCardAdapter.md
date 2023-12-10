`RoutineDetailCardAdapter` 클래스와 `ItemTouchHelperCallbackForCard` 클래스는 안드로이드 애플리케이션에서 루틴 내의 카드 목록을 관리하는 데 사용됩니다. 이들의 주요 기능과 로직은 다음과 같습니다:

### `RoutineDetailCardAdapter` 클래스
1. **데이터 및 뷰 초기화**: `ActivityRoutineDetailBinding`, `RoutineDetailActivity`를 초기화하고, `ActivityResultLauncher`를 통해 카드 상세 화면으로 이동합니다.

2. **ViewHolder 설정**: `RecyclerView.ViewHolder`를 상속받는 `CustomViewHolder` 클래스를 정의하여 카드의 이름, 메모, 총 시간, 세트 수를 표시합니다.

3. **아이템 클릭 리스너**: 각 카드 아이템에 클릭 리스너를 설정하여, 아이템을 클릭할 때 `CardDetailActivity`로 이동합니다.

4. **데이터 바인딩**: `onBindViewHolder`에서는 카드 목록의 각 아이템에 대한 데이터를 뷰 홀더에 바인딩합니다.

5. **아이템 이동 및 삭제 처리**: `removeAt` 및 `onItemMove` 함수를 통해 카드 목록의 아이템을 삭제하거나 이동할 수 있습니다.

### `ItemTouchHelperCallbackForCard` 클래스
1. **드래그 및 스와이프 활성화**: `isLongPressDragEnabled` 및 `getMovementFlags`를 통해 드래그와 스와이프 기능을 활성화합니다.

2. **아이템 이동 로직**: `onMove`에서 `RoutineDetailCardAdapter`의 `onItemMove` 함수를 호출하여 아이템을 이동합니다.

3. **스와이프로 삭제 처리**: `onSwiped`에서 스와이프된 아이템을 삭제합니다. 삭제 전에 `AlertDialog`를 표시하여 사용자의 확인을 받습니다. 사용자가 '예'를 선택하면 `RoutineDetailCardAdapter`의 `removeAt` 함수를 호출하여 아이템을 삭제합니다.

이 두 클래스는 루틴의 카드 목록 관리 UI와 상호작용을 관리하는 중요한 역할을 합니다. `RoutineDetailCardAdapter`는 카드 목록의 데이터를 화면에 표시하고, `ItemTouchHelperCallbackForCard`는 사용자가 카드 목록을 드래그하거나 스와이프하여 쉽게 조작할 수 있도록 합니다.