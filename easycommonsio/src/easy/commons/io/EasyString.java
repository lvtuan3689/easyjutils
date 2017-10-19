package easy.commons.io;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class EasyString {
	
	/**
	 * Convert object to String type
	 * @param object
	 * @return
	 */
	@Nullable
	public static String str(Object object) {
		if(object==null) {
			return null;
		}
		return String.valueOf(object);
	}
	
	/**
	 * Convert object to String type with default value
	 * @param object
	 * @param vDefault
	 * @return
	 */
	@NotNull
	public static String str(Object object,String vDefault) {
		if(object==null) {
			return vDefault==null?"":vDefault;
		}
		return String.valueOf(object);
	}
	
	/**
	 * Get a part of string from begin index
	 * @param needed
	 * @param begin
	 * @return
	 */
	public static String subStr(String needed, int begin) {
		String vstr = str(needed, "");
		if(Math.abs(begin)>=vstr.length())
			return "";
		if(begin>=0)
			return vstr.substring(begin);
		return vstr.substring(vstr.length() + begin);
	}
	
	/**
	 * Get a part of string from begin index to before end index
	 * @param needed
	 * @param begin
	 * @return
	 */
	public static String subStr(String needed, int begin, int end) {
		String vstr = str(needed, "");
		if(begin>=vstr.length())
			return "";
		return vstr.substring(begin);
	}
}
