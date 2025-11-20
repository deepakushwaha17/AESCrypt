# 🔐 AES Crypt App – Text & Image Encryption (Android | Java)

A simple yet powerful Android application that allows users to **encrypt and decrypt both text and images**.  
The app converts images into **Base64 encoded text** and securely encrypts/decrypts text messages.  
Built with Java for Android and supports **Android 13+ storage permissions (READ_MEDIA_IMAGES)**.

---

## 🚀 Features

### 📝 **1. Text Encryption & Decryption**
- Enter any text  
- Encrypt or decrypt using **AES algorithm** (if implemented)  
- Copy encrypted text  
- Clean UI & simple workflow  

### 🖼 **2. Image Encryption & Decryption**
- Pick images from storage  
- Convert image → Base64 text (encryption)  
- Convert Base64 → image (decryption)  
- Fully crash-proof  
- Works for large images  

### 📋 **3. Clipboard Support**
- Copy encrypted content  
- Paste Base64 text to decrypt  

### 🔐 **4. Modern Permission Handling**
Supports:
- `READ_MEDIA_IMAGES` (Android 13 & 14)
- `READ_EXTERNAL_STORAGE` (Android 12 and below) 

---

## 🧩 Tech Stack

- **Java**
- **Android Studio**
- **ImageDecoder API**
- **MediaStore API**
- **AES / Base64**
- **Runtime Permissions**

---

## 🔧 Android Permissions

```xml
<!-- Android 13+ -->
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES"/>

<!-- Android 12 and below -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
