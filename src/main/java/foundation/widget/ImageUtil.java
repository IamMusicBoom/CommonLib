package foundation.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 还是感觉这个第三方图片比较好
 * 
 * @author Administrator
 * 
 */
public class ImageUtil {

	private static ImageUtil sInstance;
	private PackageManager packageManager;
	private Context context;

	public static ImageUtil init(Context context) {
		if (sInstance == null) {
			sInstance = new ImageUtil();
			initImageLoader(context);
		}
		return sInstance;
	}

	public static ImageUtil getInstance() {
		if (sInstance == null) {
			throw new IllegalArgumentException("You must call init() method before call getInstance()");
		}
		return sInstance;
	}

	private static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13).threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiskCache(StorageUtils.getCacheDirectory(context))).writeDebugLogs().build();

		ImageLoader.getInstance().init(config);

	}

	public static DisplayImageOptions displayOptions(boolean inMemory, boolean onDisk) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.cacheInMemory(inMemory);
		builder.cacheOnDisk(onDisk);
		builder.considerExifParams(true);
		// builder.showImageOnLoading(R.drawable.ico_user_default);
		// builder.showImageForEmptyUri(R.drawable.ico_user_default);
		// builder.showImageOnFail(R.drawable.ico_user_default);
		// builder.displayer(new FadeInBitmapDisplayer(300));
		builder.imageScaleType(ImageScaleType.EXACTLY);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		return builder.build();
	}

	public static DisplayImageOptions noneOptions(int drawable) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.considerExifParams(true);
		builder.resetViewBeforeLoading(true);
		builder.showImageOnLoading(drawable);
		builder.showImageForEmptyUri(drawable);
		builder.showImageOnFail(drawable);
		builder.imageScaleType(ImageScaleType.EXACTLY);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		return builder.build();
	}

	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				 .showImageOnLoading(R.color.gray_normal) //设置图片在下载期间显示的图片
				// .showImageForEmptyUri(R.drawable.ic_launcher) //设置图片Uri为空或是错误的时候显示的图片
				// .showImageOnFail(R.drawable.error) //设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) // 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.build();// 构建完成

		return options;
	}
}
