Dưới đây là tài liệu chi tiết để xây dựng và phát hành ứng dụng iOS bằng GitHub Actions cho ứng dụng Flutter. Quy trình này tự động hoá việc build và tải ứng dụng lên App Store Connect khi có thay đổi trên nhánh `release`.

## 1. Cấu Hình Workflow

### Tạo Tệp Workflow

Trong thư mục của repository, tạo một tệp workflow `.yml` nằm trong thư mục `.github/workflows/`. Đặt tên tệp là `ios-build.yml`.

### Cấu Trúc Workflow

Workflow này sẽ kích hoạt khi có push vào nhánh `release`. Cấu trúc như sau:

```yaml
name: Build iOS Release

on:
  push:
    branches:
      - release

jobs:
  ios_build:
    runs-on: macos-latest

    steps:
      - name: Check out repository
        uses: actions/checkout@v2

      - name: Set up Flutter
        uses: subosito/flutter-action@v2
        with:
          flutter-version: "3.24.3"

      - name: Clean project
        run: flutter clean

      - name: Install dependencies
        run: flutter pub get

      - name: Decode Certificate and Provisioning Profile
        run: |
          echo "${{ secrets.CERTIFICATE_P12}}" | base64 --decode > cert.p12
          echo "${{ secrets.PROVISIONING_PROFILE}}" | base64 --decode > profile.mobileprovision
          echo "${{ secrets.APP_STORE_CONNECT_PRIVATE_KEY}}" | base64 --decode > AuthKey_${{ secrets.APP_STORE_CONNECT_KEY_ID }}.p8
          mkdir -p ~/.appstoreconnect/private_keys/
          scp -r AuthKey_${{ secrets.APP_STORE_CONNECT_KEY_ID }}.p8 ~/.appstoreconnect/private_keys/
          echo "${{ secrets.EXPORT_OPTIONS_PLIST }}" > ExportOptions.plist

      - name: Install Certificate and Provisioning Profile
        run: |
          security create-keychain -p "" ios-build.keychain
          security import cert.p12 -t agg -k ~/Library/Keychains/ios-build.keychain -P "${{ secrets.CERTIFICATE_PASSWORD }}" -A
          security list-keychains -s ~/Library/Keychains/ios-build.keychain
          security set-key-partition-list -S apple-tool:,apple: -s -k "" ~/Library/Keychains/ios-build.keychain
          mkdir -p ~/Library/MobileDevice/Provisioning\ Profiles
          cp profile.mobileprovision ~/Library/MobileDevice/Provisioning\ Profiles/
          ls -a ~/Library/MobileDevice/Provisioning\ Profiles/

      - name: Build iOS release IPA
        run: flutter build ipa --release --export-options-plist=ExportOptions.plist

      - name: Upload to App Store Connect
        run: |
          xcrun altool --upload-app --type ios -f build/ios/ipa/*.ipa --apiKey "${{ secrets.APP_STORE_CONNECT_KEY_ID }}" --apiIssuer "${{ secrets.APP_STORE_CONNECT_ISSUER_ID }}"
      - name: Notify Discord
        run: |
          curl -H "Content-Type: application/json" \
              -X POST \
              -d '{
              "content": "Build iOS Release has completed successfully!",
              "embeds": [
                {
                  "title": "Build iOS Release",
                  "description": "Build iOS Release has completed successfully!",
                  "color": 5814783
                }
                  ]
                }'\
                ${{ secrets.DISCORD_WEBHOOK_URL }}


```

## 2. Giải Thích Các Bước

### Bước 1: Check out Repository
- **Mô tả**: Clone mã nguồn từ repository về runner của GitHub Actions.
- **Công cụ**: `actions/checkout@v2`

### Bước 2: Cài Đặt Flutter
- **Mô tả**: Thiết lập môi trường Flutter trên runner với phiên bản Flutter đã chỉ định.
- **Công cụ**: `subosito/flutter-action@v2`
- **Cấu hình**: `flutter-version` - Phiên bản Flutter (ở đây là `"3.24.3"`).

### Bước 3: Clean Project
- **Mô tả**: Dọn dẹp các file build cũ để đảm bảo quá trình build mới không gặp vấn đề từ các tệp build cũ.

### Bước 4: Cài Đặt Dependencies
- **Mô tả**: Tải và cài đặt các package Flutter yêu cầu cho dự án.

### Bước 5: Giải Mã và Lưu Chứng Chỉ & Provisioning Profile
- **Mô tả**: Giải mã các chuỗi Base64 (đã lưu trong GitHub Secrets) và lưu thành các file gốc, bao gồm:
  - `cert.p12`: Chứng chỉ .p12.
  - `profile.mobileprovision`: Provisioning profile.
  - `AuthKey_*.p8`: Private key để kết nối với App Store Connect.
  - `ExportOptions.plist`: File cấu hình xuất bản (Export Options).
- **Lưu ý**: Chuỗi Base64 này cần được lưu trong GitHub Secrets để đảm bảo bảo mật.

### Bước 6: Cài Đặt Chứng Chỉ và Provisioning Profile
- **Mô tả**: Tạo một keychain tạm, nhập chứng chỉ vào keychain, và cài đặt provisioning profile cho quá trình build iOS.

### Bước 7: Build iOS Release IPA
- **Mô tả**: Build file `.ipa` sử dụng lệnh `flutter build ipa` và chỉ định file `ExportOptions.plist` để cấu hình các tùy chọn build.

### Bước 8: Tải Lên App Store Connect
- **Mô tả**: Tải file `.ipa` lên App Store Connect bằng cách sử dụng `xcrun altool` và xác thực thông qua API Key và API Issuer ID được lưu trong Secrets.

## 3. Cấu Hình GitHub Secrets

Để đảm bảo an toàn cho các thông tin nhạy cảm, bạn cần thêm các secrets sau vào GitHub Secrets trong repository:
- `CERTIFICATE_P12`
- `CERTIFICATE_PASSWORD`
- `PROVISIONING_PROFILE`
- `APP_STORE_CONNECT_KEY_ID`
- `APP_STORE_CONNECT_ISSUER_ID`
- `APP_STORE_CONNECT_PRIVATE_KEY`
- `EXPORT_OPTIONS_PLIST`

### Cách Thêm Secrets

1. Mở repository trên GitHub.
2. Đi đến **Settings** > **Secrets and variables** > **Actions**.
3. Thêm mỗi secret với tên và giá trị tương ứng.

Với tài liệu này, bạn có thể dễ dàng thiết lập CI/CD cho ứng dụng iOS trên Flutter với GitHub Actions.
