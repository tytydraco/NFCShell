package com.draco.nfcshell

import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ShellActivity : AppCompatActivity() {
    private lateinit var nfc: Nfc
    private lateinit var execution: Execution

    /* Choose the best accessible working directory for the script */
    private fun getBestWorkingDir(): File {
        val externalFilesDir = getExternalFilesDir(null)

        /* ~/Android/data/id/files */
        if (externalFilesDir != null)
            return externalFilesDir

        /* ~/Android/data/id/cache */
        if (externalCacheDir != null)
            return externalCacheDir!!

        /* /data/data/id/files */
        return filesDir
    }

    /* Clean and prepare working directory and return its path */
    private fun prepareWorkingDir(delete: Boolean = true): File {
        /* Find best choice of working dir */
        val workingDir = getBestWorkingDir()

        /* Delete everything in it */
        if (delete)
            workingDir.deleteRecursively()

        /* Recreate clean working dir */
        workingDir.mkdirs()

        return workingDir
    }

    /* First write out script to internal storage, then execute it */
    private fun executeScriptFromBytes(bytes: ByteArray) {
        /* Do not allow concurrent modifications */
        if (execution.executing.get())
            return

        /* Setup our execution environment */
        val execParams = ExecParams()
        with (execParams) {
            scriptBytes = bytes
            workingDir = prepareWorkingDir()
        }

        /* Execute script in another thread */
        Thread {
            execution.execute(execParams)
        }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()

        /* Register our Nfc helper class */
        nfc = Nfc(this)

        /* Create our execution environment */
        execution = Execution(getSystemService(POWER_SERVICE) as PowerManager)

        /* Get tag contents */
        val bytes = nfc.readBytes(intent)

        /* Execute */
        executeScriptFromBytes(bytes)
    }
}