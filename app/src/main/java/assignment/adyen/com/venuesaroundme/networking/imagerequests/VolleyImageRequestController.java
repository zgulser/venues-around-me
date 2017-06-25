package assignment.adyen.com.venuesaroundme.networking.imagerequests;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;

/**
 * Created by by Zeki 26/06/2017.
 */

public class VolleyImageRequestController {

    private static VolleyImageRequestController volleyImageRequestController;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private VolleyImageRequestController(){
        initVolleyParams();
    }

    private void initVolleyParams(){
        requestQueue = Volley.newRequestQueue(FsqVenuesApplication.getAppContext());
        imageLoader = new ImageLoader(requestQueue, new AvatarImageCache());
    }

    public static synchronized VolleyImageRequestController getInstance(){
        if(volleyImageRequestController == null) {
            volleyImageRequestController = new VolleyImageRequestController();
        }

        return volleyImageRequestController;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
