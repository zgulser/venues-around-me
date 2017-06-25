package assignment.adyen.com.venuesaroundme.networking.imagerequests;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by by Zeki 26/06/2017.
 */

public class AvatarImageCache implements ImageLoader.ImageCache {

    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }
}
