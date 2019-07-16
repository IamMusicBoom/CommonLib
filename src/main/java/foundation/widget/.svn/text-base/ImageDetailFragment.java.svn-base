package foundation.widget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.common.library.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import foundation.toast.ToastUtil;

import foundation.zoom.PhotoViewAttacher;


public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private Bitmap bm;
	private int isshop;//0 可保存，1不可保存

	public static ImageDetailFragment newInstance(String imageUrl,int isshop) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		args.putInt("isshop",isshop);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		isshop = getArguments() != null ? getArguments().getInt("isshop") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);

		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
		if(isshop==0) {
			ToastUtil.showToast("长按图片可保存");
			mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
				@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
				@Override
				public boolean onLongClick(View v) {
					if (null == bm) {
						ToastUtil.showToast("图片信息不存在...");
						return true;
					}
					new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
							.setMessage("保存到本地")
							.setNegativeButton("取消", null)
							.setPositiveButton("保存", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bm, "芈友" + System.currentTimeMillis(), "芈友" + System.currentTimeMillis());
								}
							}).show();

					return true;
				}
			});
		}

		progressBar = (ProgressBar) v.findViewById(R.id.loading);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, ImageUtil.getOptions(), new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				ToastUtil.showToast(message);
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				bm = loadedImage;
				progressBar.setVisibility(View.GONE);
				mAttacher.update();
			}
		});
	}
}
