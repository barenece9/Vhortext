package apps.lnsel.com.vhortexttest.utils;

import android.text.TextUtils;
import android.util.Log;


public class LogUtils {

	private static final String DEFAULT_LOG_TAG = "VHORTEXT LOG";
	private static final boolean SHOUL_PRINT_LOG =true;

	private static final String LOG_PREFIX = /* AppConfig.APP_TAG + "_" */"";
	private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
	private static final int MAX_LOG_TAG_LENGTH = 23;

	public static String makeLogTag(String str) {
		if (TextUtils.isEmpty(str)) {
			return DEFAULT_LOG_TAG;
		}
		if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
			return LOG_PREFIX
					+ str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH
							- 1);
		}

		return LOG_PREFIX + str;
	}

	/**
	 * Don't use this when obfuscating class names!
	 */
	@SuppressWarnings("rawtypes")
	public static String makeLogTag(Class cls) {
		return makeLogTag(cls.getSimpleName());
	}

	public static void d(String message) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.i(DEFAULT_LOG_TAG, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void d(Exception exception) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.d(DEFAULT_LOG_TAG, exception.getLocalizedMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void d(String tag, String message) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.d(tag, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void d(String tag, Exception exception) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.d(tag, exception.getLocalizedMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void i(String message) {
		try {
			if (SHOUL_PRINT_LOG) {
                Log.i(DEFAULT_LOG_TAG, message);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void i(Exception exception) {
		try {
			if (SHOUL_PRINT_LOG) {
                Log.i(DEFAULT_LOG_TAG, exception.getLocalizedMessage());
            }
		} catch (Exception e) {
		}
	}

	public static void i(String tag, String message) {
		try {
			if (SHOUL_PRINT_LOG) {
                Log.i(tag, message);
            }
		} catch (Exception e) {
		}
	}

	public static void i(String tag, Exception exception) {
		try {
			if (SHOUL_PRINT_LOG) {
                Log.i(tag, exception.getLocalizedMessage());
            }
		} catch (Exception e) {
		}
	}

	public static void e(String message) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.e(DEFAULT_LOG_TAG, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void e(Exception exception) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.e(DEFAULT_LOG_TAG, exception.getLocalizedMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void e(String tag, String message) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.e(tag, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void e(String tag, Exception exception) {
		try {
			if (SHOUL_PRINT_LOG) {
				Log.e(tag, exception.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
