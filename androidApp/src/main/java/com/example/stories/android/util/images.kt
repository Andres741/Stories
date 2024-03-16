package com.example.stories.android.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toIcon
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.example.stories.model.domain.model.ImageDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt
import kotlin.math.sqrt

object ImageUtils {

    const val SERVICE_MAX_SIZE = 16_000_000

    suspend fun uriToBitmap(uri: Uri, context: Context) = withContext(Dispatchers.IO) {
        uri.toIcon().loadDrawable(context)?.toBitmap()
    }

    suspend fun convertBitmapToByteArray(bitmap: Bitmap): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            ByteArrayOutputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
                output.toByteArray()
            }
        }
    }

    /*
    w = realImage.width
    h = realImage.height
    MAX = maxResolution
    X = scaleDownFraction

    w*X * h*X = MAX
    w*h*X^2 = MAX
    X^2= MAX/w*h
    X = sq(MAX/w*h)
     */
    suspend fun ensureMaxResolution(realImage: Bitmap, maxResolution: Int): Bitmap {
        val width = realImage.width
        val height = realImage.height

        if (width * height <= maxResolution) return realImage

        return withContext(Dispatchers.IO) {
            val scaleDownFraction = sqrt(maxResolution.toFloat() / (width * height))
            Bitmap.createScaledBitmap(
                /* src = */ realImage,
                /* dstWidth = */ (width * scaleDownFraction).roundToInt(),
                /* dstHeight = */ (height * scaleDownFraction).roundToInt(),
                /* filter = */ true,
            )
        }
    }

    suspend fun uriToByteArray(uri: Uri, maxResolution: Int, context: Context): Result<ByteArray> = withContext(Dispatchers.IO) {
        val bitmap = uriToBitmap(uri, context) ?: return@withContext Result.failure(RuntimeException())
        val ensuredBitmap = ensureMaxResolution(bitmap, maxResolution)
        return@withContext convertBitmapToByteArray(ensuredBitmap)
    }

    suspend fun uriToImageDomain(uri: Uri, context: Context): ImageDomain? = withContext(Dispatchers.IO) {
        ImageDomain(
            name = uri.lastPathSegment ?: return@withContext null,
            data = uriToByteArray(uri, SERVICE_MAX_SIZE, context).getOrNull() ?: return@withContext null
        )
    }

    suspend fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap = withContext(Dispatchers.IO) {
        BitmapFactory.decodeByteArray(src, 0, src.size)
    }
}

@Composable
fun rememberRefreshableAsyncImagePainter(data: Any?, refreshKey: Any?): AsyncImagePainter {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.DISABLED).build()
    }

    val painter = rememberAsyncImagePainter(
        model = remember {
            ImageRequest.Builder(context).data(data).size(Size.ORIGINAL).build()
        },
        imageLoader = imageLoader,
    )

    LaunchedEffect(
        key1 = refreshKey,
        block = {
            painter.onForgotten()
            painter.onRemembered()
        }
    )

    return painter
}
