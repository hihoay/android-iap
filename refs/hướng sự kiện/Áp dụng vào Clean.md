Tất nhiên, áp dụng luồng sự kiện này vào mô hình **Clean Architecture** là một cách rất hay để tạo ra sự giao tiếp linh hoạt giữa các lớp mà vẫn đảm bảo các nguyên tắc của mô hình.

Để làm điều này một cách chính xác, bạn cần tuân thủ **Quy tắc Phụ thuộc (Dependency Rule)**: các lớp bên ngoài (Presentation, Data) phụ thuộc vào các lớp bên trong (Domain), nhưng lớp Domain không được biết gì về các lớp bên ngoài.

Chúng ta sẽ đạt được điều này thông qua **Abstraction (trừu tượng hóa)** và **Dependency Injection (DI)**.

-----

### \#\# Cấu trúc tổng thể

Kiến trúc sẽ được phân chia như sau:

1.  **Lớp Domain (Trung tâm):**

    * Sẽ định nghĩa một **`interface`** cho Event Bus (ví dụ: `AppEventBus`). Đây là bản hợp đồng, quy định Event Bus *có thể làm gì* (`post`, `register`), nhưng không quan tâm *làm như thế nào*.
    * Các `UseCase` trong Domain sẽ sử dụng `interface` này để **gửi** sự kiện.

2.  **Lớp Presentation (Bên ngoài):**

    * Sẽ chứa lớp triển khai cụ thể (`implementation`) của `interface` đó (chính là lớp `EventManager` chúng ta đã tạo).
    * Các `ViewModel` và `UI` (Activity/Fragment) sẽ **gửi** và **lắng nghe** sự kiện thông qua `interface`.

3.  **Lớp Data (Bên ngoài):**

    * Các `Repository` cũng có thể **gửi** sự kiện (ví dụ: sau khi đồng bộ dữ liệu xong) thông qua `interface`.

4.  **Dependency Injection (DI - Ví dụ: Hilt, Koin):**

    * DI sẽ chịu trách nhiệm "tiêm" (inject) bản triển khai cụ thể (`EventManager`) vào bất kỳ nơi nào cần dùng (`ViewModel`, `UseCase`, `Repository`), trong khi những nơi đó chỉ biết đến `interface` (`AppEventBus`).

-----

### \#\# Triển khai chi tiết từng lớp

#### \#\#\# Lớp Domain (Tầng trung tâm)

Lớp này không chứa logic Android, chỉ có Kotlin/Java thuần túy.

1.  **Định nghĩa `interface` cho Event Bus:**

    ```kotlin
    // file: domain/event/AppEventBus.kt
    package com.example.myapp.domain.event

    import androidx.lifecycle.LifecycleOwner // !! Lưu ý đặc biệt ở đây

    interface AppEventBus {
        fun register(owner: LifecycleOwner, eventName: String, callback: EventCallback)
        fun post(eventName: String, data: Map<String, Any>?)
    }

    fun interface EventCallback {
        fun onEvent(eventName: String, data: String?)
    }
    ```

    > **Lưu ý quan trọng:** `LifecycleOwner` là một thư viện của Android. Về lý thuyết, Domain không nên chứa thư viện Android. Tuy nhiên, `androidx.lifecycle:lifecycle-common` được xem là một ngoại lệ chấp nhận được trong nhiều dự án vì nó không chứa `Context` hay logic UI, chỉ có các định nghĩa trừu tượng về vòng đời. Nếu muốn tuân thủ 100%, bạn sẽ cần tạo một hệ thống callback phức tạp hơn để tự quản lý vòng đời. Với thực tế, việc giữ `LifecycleOwner` ở đây là một sự đánh đổi hợp lý.

2.  **Sử dụng trong `UseCase`:**

    ```kotlin
    // file: domain/usecase/UpdateUserProfileUseCase.kt
    package com.example.myapp.domain.usecase

    import com.example.myapp.domain.event.AppEventBus
    import com.example.myapp.domain.repository.UserRepository
    import javax.inject.Inject

    class UpdateUserProfileUseCase @Inject constructor(
        private val userRepository: UserRepository,
        private val eventBus: AppEventBus // Chỉ biết đến interface
    ) {
        suspend fun execute(userId: String, newName: String) {
            val result = userRepository.updateName(userId, newName)
            if (result.isSuccess) {
                // Gửi sự kiện đi sau khi hoàn thành logic nghiệp vụ
                eventBus.post("USER_UPDATED", mapOf("userId" to userId))
            }
        }
    }
    ```

-----

#### \#\#\# Lớp Presentation (Tầng giao diện)

1.  **Triển khai `interface`:**

    Lớp `EventManager` của chúng ta bây giờ sẽ implement `AppEventBus` từ Domain.

    ```kotlin
    // file: presentation/event/EventManager.kt
    package com.example.myapp.presentation.event

    import com.example.myapp.domain.event.AppEventBus
    import com.example.myapp.domain.event.EventCallback
    // ... các import khác

    // Triển khai interface từ Domain
    class EventManager : AppEventBus {
        // ... toàn bộ mã nguồn của EventManager giữ nguyên ...
        
        override fun register(owner: LifecycleOwner, eventName: String, callback: EventCallback) {
            // ...
        }

        override fun post(eventName: String, data: Map<String, Any>?) {
            // ...
        }
    }
    ```

2.  **Cung cấp bằng Dependency Injection (Ví dụ với Hilt):**

    ```kotlin
    // file: di/AppModule.kt
    package com.example.myapp.di

    import com.example.myapp.domain.event.AppEventBus
    import com.example.myapp.presentation.event.EventManager
    import dagger.Module
    import dagger.Provides
    import dagger.hilt.InstallIn
    import dagger.hilt.components.SingletonComponent
    import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {

        @Provides
        @Singleton
        fun provideEventBus(): AppEventBus {
            // Cung cấp EventManager khi có ai đó yêu cầu AppEventBus
            return EventManager()
        }
    }
    ```

3.  **Sử dụng trong `ViewModel` và `UI`:**

    ```kotlin
    // file: presentation/user/ProfileViewModel.kt
    @HiltViewModel
    class ProfileViewModel @Inject constructor(
        private val updateUserProfileUseCase: UpdateUserProfileUseCase,
        private val eventBus: AppEventBus // Được Hilt tiêm vào
    ) : ViewModel() {
        
        init {
            // ViewModel có thể lắng nghe sự kiện (nhưng ít phổ biến)
        }

        fun onUpdateNameClicked(newName: String) {
            viewModelScope.launch {
                updateUserProfileUseCase.execute("123", newName)
            }
        }
    }

    // file: presentation/home/HomeFragment.kt
    @AndroidEntryPoint
    class HomeFragment : Fragment() {
        @Inject lateinit var eventBus: AppEventBus // Tiêm vào Fragment

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            
            // UI lắng nghe sự kiện để cập nhật giao diện
            eventBus.register(viewLifecycleOwner, "USER_UPDATED") { eventName, data ->
                activity?.runOnUiThread {
                    Toast.makeText(context, "Một người dùng vừa được cập nhật!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    ```

-----

### \#\# Luồng hoạt động hoàn chỉnh

1.  **Giao diện (HomeFragment):** Người dùng thấy thông báo "Một người dùng vừa được cập nhật" và giao diện tự refresh.
2.  **Hành động (ProfileFragment):** Người dùng nhấn nút "Cập nhật tên".
3.  **ViewModel (ProfileViewModel):** Gọi `updateUserProfileUseCase`.
4.  **UseCase (UpdateUserProfileUseCase):** Thực hiện logic nghiệp vụ, gọi `UserRepository` để lưu vào DB/API. Sau khi thành công, nó gọi `eventBus.post("USER_UPDATED", ...)`.
5.  **EventManager (Presentation):** Nhận được lệnh `post`, tìm tất cả các listener đã đăng ký cho sự kiện "USER\_UPDATED" và gọi hàm `onEvent` của chúng.
6.  **Giao diện (HomeFragment):** Callback đã đăng ký trong `HomeFragment` được kích hoạt, một `Toast` được hiển thị.

Bằng cách này, `ProfileFragment` và `HomeFragment` không cần biết gì về nhau. Chúng giao tiếp một cách gián tiếp thông qua một cơ chế sự kiện đã được trừu tượng hóa ở lớp Domain, tuân thủ đúng các nguyên tắc của Clean Architecture.