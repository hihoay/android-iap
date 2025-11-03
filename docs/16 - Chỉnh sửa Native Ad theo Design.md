Native Ad có khả năng tùy biến phù hợp với giao diện của App, các thành phần có thể chỉnh sửa được gồm:
- 1, Tiêu đề (
- 2, Mô tả ngắn
- 3, Mô tả dài
- 4, Icon 
- 5, Nút CTA (_* Cái này phải có_)
- 6, Ad Tag (_* Cái này phải có_)
- 7, Background
- 8, Đường Viền của Ad (_* Cái này phải có_)
- 9, Media View

## Cấu hình và Chạy

### 1, Tạo Layout View chứa `AdmobNativeAdTemplateView`: 

- Đối với dạng size Medium : `layout/template_native_ad_medium_custom.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>  
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:app="http://schemas.android.com/apk/res-auto"  
    android:layout_width="match_parent"  
    android:layout_height="wrap_content"  
    android:orientation="vertical">  
  ...
    <com.hihoay.adx.service.objecs.AdmobNativeAdTemplateView  
        android:id="@+id/temp_view_native"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        app:gnt_template_type="@layout/admob_native_ad_medium_custom" />  
  ...
</LinearLayout>
```

- Đối với dạng Size Small: `layout/template_native_ad_small_custom.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>  
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:app="http://schemas.android.com/apk/res-auto"  
    android:layout_width="match_parent"  
    android:layout_height="wrap_content"  
    android:orientation="vertical">  
  ...
    <com.hihoay.adx.service.objecs.AdmobNativeAdTemplateView  
        android:id="@+id/temp_view_native"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        app:gnt_template_type="@layout/admob_native_ad_small_custom" />  
  ...
</LinearLayout>
```

### 2, Tạo LayoutView thể hiện tùy biến NativeAd Mẫu:

- Đối với size Medium: `layout/admob_native_ad_medium_custom.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>  
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:app="http://schemas.android.com/apk/res-auto"  
    xmlns:tools="http://schemas.android.com/tools"  
    android:layout_width="match_parent"  
    android:layout_height="wrap_content">  
  
    <com.google.android.gms.ads.nativead.NativeAdView  
        android:id="@+id/native_ad_view"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:padding="@dimen/_3sdp">  
  
        <LinearLayout  
            android:id="@+id/background"  
            android:layout_width="match_parent"  
            android:layout_height="match_parent"  
            android:layout_centerInParent="true"  
            android:background="@drawable/admob_outline_shape"  
            android:orientation="vertical"  
            android:padding="@dimen/_4sdp">  
  
            <LinearLayout  
                android:id="@+id/middle"  
                android:layout_width="match_parent"  
                android:layout_height="wrap_content"  
                android:gravity="center"  
                android:orientation="horizontal">  
  
                <ImageView  
                    android:id="@+id/icon"  
                    android:layout_width="@dimen/_50sdp"  
                    android:layout_height="@dimen/_50sdp"  
                    android:layout_margin="@dimen/_4sdp"  
                    android:scaleType="fitCenter"  
                    app:layout_constraintTop_toTopOf="parent" />  
  
                <LinearLayout                    android:id="@+id/content"  
                    android:layout_width="wrap_content"  
                    android:layout_height="wrap_content"  
                    android:layout_weight="1"  
  
                    android:orientation="vertical">  
  
                    <TextView  
                        android:id="@+id/primary"  
                        style="@style/TextAppearance.MaterialComponents.Subtitle1"  
                        android:layout_width="match_parent"  
                        android:layout_height="wrap_content"  
                        android:background="@color/native_ad_background"  
                        android:textColor="@color/native_ad_title"  
                        android:textStyle="bold"  
                        tools:text="abc" />  
  
                    <LinearLayout                        android:layout_width="match_parent"  
                        android:layout_height="wrap_content"  
                        android:gravity="center"  
                        android:orientation="horizontal">  
  
                        <androidx.appcompat.widget.AppCompatTextView  
                            android:id="@+id/ad_notification_view"  
                            style="@style/TextAppearance.MaterialComponents.Body2"  
                            android:layout_width="wrap_content"  
                            android:layout_height="wrap_content"  
                            android:layout_gravity="center|top"  
                            android:layout_margin="@dimen/_4sdp"  
                            android:background="@drawable/admob_rounded_corners_shape"  
                            android:gravity="center"  
                            android:paddingLeft="@dimen/_4sdp"  
                            android:paddingRight="@dimen/_4sdp"  
                            android:text="@string/ad_attribution"  
                            android:textColor="@color/native_ad_ad_tag_text"  
                            android:textStyle="bold" />  
  
                        <TextView                            android:id="@+id/body"  
                            style="@style/TextAppearance.MaterialComponents.Body2"  
                            android:layout_width="match_parent"  
                            android:layout_height="wrap_content"  
                            android:gravity="center|left"  
                            android:textColor="@color/native_ad_body"  
                            tools:text="abc" />  
                    </LinearLayout>  
  
                    <TextView                        android:id="@+id/secondary"  
                        style="@style/TextAppearance.MaterialComponents.Body2"  
                        android:layout_width="match_parent"  
                        android:layout_height="wrap_content"  
                        android:layout_marginTop="@dimen/_2sdp"  
                        android:background="@color/native_ad_background"  
                        android:gravity="top"  
                        android:textColor="@color/native_ad_body"  
                        tools:text="abc" />  
                </LinearLayout>  
            </LinearLayout>  
  
            <LinearLayout                android:layout_width="match_parent"  
                android:layout_height="wrap_content"  
                android:layout_marginTop="@dimen/_4sdp"  
                android:gravity="center|bottom"  
                android:orientation="vertical">  
  
                <TextView  
                    style="@style/TextAppearance.AppCompat.Title"  
                    android:layout_width="wrap_content"  
                    android:layout_height="wrap_content"  
                    android:text="Demo chỉnh sửa content"  
                    android:textColor="#fff" />  
  
                <com.google.android.gms.ads.nativead.MediaView                    android:id="@+id/media_view"  
                    android:layout_width="match_parent"  
                    android:layout_height="@dimen/_110sdp"  
                    android:layout_gravity="center|left"  
                    android:layout_weight="1"  
                    app:layout_constraintTop_toTopOf="parent" />  
  
                <androidx.appcompat.widget.AppCompatButton                    android:id="@+id/cta"  
                    android:layout_width="match_parent"  
                    android:layout_height="@dimen/_30sdp"  
                    android:layout_margin="@dimen/_4sdp"  
                    android:background="@drawable/bg_btn_ad_action"  
                    android:gravity="center"  
                    android:lines="1"  
                    android:textColor="@color/native_ad_button_text"  
                    tools:text="@string/action" />  
            </LinearLayout>  
  
  
        </LinearLayout>  
    </com.google.android.gms.ads.nativead.NativeAdView>  
</LinearLayout>
```


- Đối với size Small: `layout/admob_native_ad_small_custom.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>  
<merge xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:tools="http://schemas.android.com/tools">  
  
    <com.google.android.gms.ads.nativead.NativeAdView  
        android:id="@+id/native_ad_view"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:layout_centerInParent="true"  
        android:padding="@dimen/_3sdp">  
  
        <LinearLayout  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:background="@drawable/admob_outline_shape"  
            android:orientation="vertical"  
  
            android:padding="@dimen/_4sdp">  
  
            <TextView  
                style="@style/TextAppearance.AppCompat.Title.Inverse"  
                android:layout_width="wrap_content"  
                android:layout_height="wrap_content"  
                android:text="Demo chỉnh sửa content"  
                android:textColor="#fff" />  
  
  
            <LinearLayout                android:layout_width="match_parent"  
                android:layout_height="wrap_content"  
                android:layout_gravity="center"  
                android:gravity="center"  
                android:orientation="horizontal">  
  
                <ImageView  
                    android:id="@+id/icon"  
                    android:layout_width="@dimen/_45sdp"  
                    android:layout_height="@dimen/_45sdp"  
                    android:layout_marginRight="@dimen/_4sdp"  
                    android:layout_weight="0" />  
  
                <LinearLayout                    android:layout_width="0dp"  
                    android:layout_height="wrap_content"  
                    android:layout_weight="1"  
                    android:orientation="vertical">  
  
                    <TextView  
                        android:id="@+id/primary"  
                        style="@style/TextAppearance.MaterialComponents.Subtitle1"  
                        android:layout_width="match_parent"  
                        android:layout_height="wrap_content"  
                        android:textColor="@color/native_ad_title"  
                        tools:text="abc" />  
  
                    <LinearLayout                        android:layout_width="match_parent"  
                        android:layout_height="wrap_content"  
                        android:layout_marginTop="@dimen/_2sdp"  
                        android:gravity="center|left"  
                        android:orientation="horizontal"  
  
                        tools:ignore="RtlHardcoded">  
  
                        <androidx.appcompat.widget.AppCompatTextView  
                            android:id="@+id/ad_notification_view"  
                            style="@style/TextAppearance.MaterialComponents.Body2"  
                            android:layout_width="wrap_content"  
                            android:layout_height="wrap_content"  
                            android:background="@drawable/admob_rounded_corners_shape"  
                            android:gravity="center|left"  
                            android:paddingLeft="@dimen/_4sdp"  
                            android:paddingRight="@dimen/_4sdp"  
                            android:text="@string/ad_attribution"  
                            android:textColor="@color/native_ad_ad_tag_text"  
                            android:textStyle="bold" />  
  
                        <TextView                            android:id="@+id/body"  
                            style="@style/TextAppearance.MaterialComponents.Body2"  
                            android:layout_width="match_parent"  
                            android:layout_height="wrap_content"  
                            android:layout_marginLeft="@dimen/_4sdp"  
                            android:gravity="center|left"  
                            android:textColor="@color/native_ad_body"  
                            tools:text="abc" />  
                    </LinearLayout>  
  
  
                    <TextView                        android:id="@+id/secondary"  
                        android:layout_width="wrap_content"  
                        android:layout_height="wrap_content"  
                        android:layout_marginTop="@dimen/_2sdp"  
                        android:layout_weight="1"  
                        android:background="@color/native_ad_background"  
                        android:gravity="top"  
                        android:lines="1"  
                        android:textColor="@color/native_ad_body"  
                        android:textSize="@dimen/gnt_text_size_small"  
                        tools:text="abc" />  
  
  
                </LinearLayout>  
  
                <LinearLayout                    android:layout_width="wrap_content"  
                    android:layout_height="wrap_content"  
                    android:layout_marginLeft="@dimen/_4sdp"  
                    android:gravity="center"  
                    android:orientation="vertical">  
  
                    <LinearLayout  
                        android:id="@+id/row_two"  
                        android:layout_width="wrap_content"  
                        android:layout_height="wrap_content"  
                        android:layout_marginBottom="@dimen/_4sdp"  
                        android:gravity="center"  
                        android:orientation="horizontal"  
                        android:visibility="gone"></LinearLayout>  
                </LinearLayout>  
            </LinearLayout>  
  
            <androidx.appcompat.widget.AppCompatButton                android:id="@+id/cta"  
                style="@style/TextAppearance.AppCompat.Body1"  
                android:layout_width="match_parent"  
                android:layout_height="@dimen/_30sdp"  
                android:layout_margin="@dimen/_4sdp"  
                android:background="@drawable/bg_btn_ad_action"  
                android:gravity="center"  
                android:textColor="@color/native_ad_button_text"  
                tools:text="Action" />  
  
        </LinearLayout>  
  
    </com.google.android.gms.ads.nativead.NativeAdView>  
  
</merge>
```

### 3, Chính sửa lại thiết kế của tệp `.xml` trên theo mong muốn 

> Lưu ý là giữ nguyên các `id` và các thành phần của mẫu là những thành phần cần có của `NativeAd`, chỉnh sửa size và màu sắc tùy ý, có thểm thêm các View khác nếu có.


### 4, Chạy quảng cáo:
- Đối với size Medium: 

```kotlin
hihoayLoadAndShowAdWait(  
    ad_name,
    ByLayout(binnding.llAdContainerTop.apply {  
        tag = TemplateNativeAdMediumCustomBinding.inflate(layoutInflater).root  
    })  
)
```

- Đối với size Small: 

```kotlin
hihoayLoadAndShowAdWait(  
    ad_name,
    ByLayout(binnding.llAdContainerTop.apply {  
        tag = TemplateNativeAdSmallCustomBinding.inflate(layoutInflater).root  
    })  
)
```

### 5, Chạy ứng dụng và xem kết quả.