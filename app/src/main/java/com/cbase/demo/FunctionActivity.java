package com.cbase.demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cbase.demo.base.AppActivity;
import com.cbase.demo.model.ItemBean;
import com.cbase.demo.ui.LazyViewPagerFragment;
import com.cbase.demo.ui.ListFragment;
import com.cbase.demo.ui.RequestFragment;
import com.cbase.demo.ui.StatusFragment;
import com.cbase.sample.ImageGalleryFragment;
import com.cbase.sample.WebViewFragment;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author : zhouyx
 * @date : 2017/9/17
 * @description :
 */
public class FunctionActivity extends AppActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    public static void open(AppActivity context, ItemBean itemBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", itemBean);
        context.startActivity(bundle, FunctionActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_function;
    }

    @Override
    protected void initActivity(Bundle savedInstanceState, Bundle inputBundle) {
        if (inputBundle == null) {
            return;
        }
        ItemBean itemBean = (ItemBean) inputBundle.getSerializable("item");
        if (itemBean == null) {
            return;
        }
        setToolbar(toolbar, itemBean.getName());
        Fragment fragment = null;
        switch (itemBean.getType()) {
            case ItemBean.TYPE_LIST:
                fragment = ListFragment.getInstance();
                break;
            case ItemBean.TYPE_STATUS:
                fragment = StatusFragment.getInstance();
                break;
            case ItemBean.TYPE_REQUEST:
                fragment = RequestFragment.getInstance();
                break;
            case ItemBean.TYPE_WEB_VIEW:
                fragment = WebViewFragment.getUrlInstance("https://www.baidu.com");
                break;
            case ItemBean.TYPE_IMAGE_GALLERY:
                ArrayList<String> list = new ArrayList<>();
                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505886789089&di=b6ed1281e701e3210508ab651ff6b747&imgtype=0&src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1011%2F0R31G14310%2F1FR3114310-17.jpg");
                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505886785772&di=8907cff50663b0287e5dd12a93360d83&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201606%2F24%2F2340578lqll23i231cn3i4.jpg");
                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506481845&di=8ec7c8fc09313f6a3053092a3180a831&imgtype=jpg&er=1&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57344496ae2ff.jpg");
                fragment = ImageGalleryFragment.getInstance(list, 1);
                ((ImageGalleryFragment) fragment).setImageLoadCallback(new ImageGalleryFragment.ImageLoadCallback() {
                    @Override
                    public void onImageLoad(Fragment fragment, String url, PhotoView photoView, final ProgressBar progressBar) {
                        progressBar.setVisibility(View.VISIBLE);
                        Glide.with(fragment).load(url).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(photoView);
                    }
                });
                break;
            case ItemBean.TYPE_LAZY:
                fragment = LazyViewPagerFragment.getInstance();
                break;
            default:
                break;
        }
        if (fragment == null) {
            return;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commitAllowingStateLoss();
    }

}
