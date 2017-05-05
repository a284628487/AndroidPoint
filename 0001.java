        // 内部存储空间中的应用私有目录
        Log.e(TAG, "getFilesDir: " + getFilesDir().getAbsolutePath());
        // /data/user/0/com.example.ccfyyn/files
        Log.e(TAG, "getCacheDir: " + getCacheDir().getAbsolutePath());
        // /data/user/0/com.example.ccfyyn/cache
        Log.e(TAG, "Environment.getDataDirectory: " + Environment.getDataDirectory());
        // /data
        String[] filelist = fileList();
        if (null != filelist && filelist.length > 0) {
            for (int i = 0; i < filelist.length; i++) {
                Log.e(TAG, "fileList: " + filelist[i]);
            }
        }

        // 外部存储空间中的应用私有目录
        Log.e(TAG, "getExternalFilesDir: " + getExternalFilesDir("").getAbsolutePath());
        // /storage/emulated/0/Android/data/com.example.ccfyyn/files
        Log.e(TAG, "getExternalFilesDir_lib: " + getExternalFilesDir("lib").getAbsolutePath());
        // /storage/emulated/0/Android/data/com.example.ccfyyn/files/lib
        Log.e(TAG, "getExternalFilesDir_so: " + getExternalFilesDir("so").getAbsolutePath());
        // /storage/emulated/0/Android/data/com.example.ccfyyn/files/so
        Log.e(TAG, "getExternalCacheDir: " + getExternalCacheDir().getAbsolutePath());
        // /storage/emulated/0/Android/data/com.example.ccfyyn/cache
        Log.e(TAG, "Environment.getExternalStorageDirectory: " + Environment.getExternalStorageDirectory().getAbsolutePath());
        // /storage/emulated/0