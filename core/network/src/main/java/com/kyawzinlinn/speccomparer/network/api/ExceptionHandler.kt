package com.kyawzinlinn.speccomparer.network.api

import android.net.ParseException
import android.net.http.NetworkException
import com.kyawzinlinn.speccomparer.domain.utils.NetworkError
import com.kyawzinlinn.speccomparer.domain.utils.Resource
import org.json.JSONException
import org.jsoup.HttpStatusException
import org.jsoup.UnsupportedMimeTypeException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

object ExceptionHandler {
    inline fun <T> handleError(exception: Exception): Resource<T> {
        return when (exception) {
            is JSONException -> Resource.Error(NetworkError(title = "JSON Parsing Error", message = "Error parsing response"))
            is HttpException -> Resource.Error(NetworkError(title = "HTTP Exception", message = exception.message.toString()))
            is IllegalStateException -> Resource.Error(NetworkError(title = "Illegal State Exception", message = exception.message.toString()))
            is IOException -> Resource.Error(NetworkError(title = "Network Connection Error", message="Indicates a problem reading from the website URL, often caused by network issues, a downed website, or an incorrect URL"))
            is HttpStatusException -> Resource.Error(NetworkError(title = "HTTP Error Status", message="Thrown when Jsoup encounters an HTTP error status (e.g., 404, 500) while attempting to connect to the website"))
            is UnsupportedMimeTypeException -> Resource.Error(NetworkError(title = "Unsupported Content Type", message="Occurs when Jsoup encounters a content type it cannot handle, such as non-HTML content like PDF or images"))
            is SocketTimeoutException -> Resource.Error(NetworkError(title = "Connection Timeout", message="Indicates a timeout during the connection to the website, often due to slow network connections or delayed server responses"))
            is IllegalArgumentException -> Resource.Error(NetworkError(title = "Invalid Argument", message="Thrown when invalid arguments are passed to Jsoup methods, such as null URLs or invalid CSS selectors"))
            is ParseException -> Resource.Error(NetworkError(title = "Parsing Error", message="Occurs when Jsoup encounters HTML or XML it cannot parse due to syntax errors or invalid markup"))
            is SSLHandshakeException -> Resource.Error(NetworkError(title = "SSL Handshake Error", message="Indicates a problem with the SSL handshake process when attempting to connect to a website over HTTPS"))
            else -> Resource.Error(NetworkError(title = "", message = "Unknown error occurred"))
        }
    }
}