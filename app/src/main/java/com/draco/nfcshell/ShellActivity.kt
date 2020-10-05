package com.draco.nfcshell

import android.app.Activity
import android.os.Bundle
import java.io.FileOutputStream

class ShellActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()

        /* Register our Nfc helper class */
        val nfc = Nfc(this)

        /* Get tag contents */
        val bytes = nfc.readBytes(intent)

        /* Write our script */
        val fileOutput = createTempDir("script", "sh")
        val fileOutputStream = FileOutputStream(fileOutput)
        fileOutputStream.write(bytes)
        fileOutputStream.close()

        /* Begin execution */
        ProcessBuilder("sh", fileOutput.absolutePath).start()
    }
}