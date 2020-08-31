# NFCShell
A backend framework for opening text/sh type NFC tags as shell scripts.

# Writing
Use an app like NFCTools to write a raw data record with the mime tag `text/sh`. You can also use ZLIB compression if you need to compress your script.

# Scanning
The app has no foreground screen. An NFC intent will trigger NFCShell to open a background thread, which executes your script.

# Notes
- There is no script output.
- Your working directory is /sdcard/Android/data/id/files, /sdcard/Android/data/id/cache, or /data/data/id/files depending on what is available.
