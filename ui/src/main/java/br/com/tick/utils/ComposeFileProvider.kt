package br.com.tick.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import br.com.tick.ui.R
import java.io.File
import java.time.LocalDate

class ComposeFileProvider : FileProvider(R.xml.filepaths) {
    companion object {
        fun getImageUri(fileName: String, context: Context): Uri {
            val directory = File(context.filesDir, "images")
            directory.mkdirs()

            val file = try {
                File.createTempFile("${fileName}_", ".jpg", directory)
            } catch (exception: IllegalArgumentException) {
                File.createTempFile(LocalDate.now().toString(), ".jpg", directory)
            }

            val authority = context.packageName + ".fileprovider"

            return getUriForFile(context, authority, file)
        }
    }
}
