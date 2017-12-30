package com.nj.nfhy.util.basicUtils;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Administrator on 15-8-28.
 */
public class StringUtil {
    /**
     * 用户附件上传后，获得的附件名称的转码
     * @param filename
     * @return
     */
    public static String transFileName(String filename){
        if(filename == null || filename.equals("")) return "";
        String beforeTransEncode = StringUtil.strIfNull(ConstantUtils.obj.getSysEnvironment().get("FileBeforeTransEncode"));
        String afterTransEncode = StringUtil.strIfNull(ConstantUtils.obj.getSysEnvironment().get("FileAfterTransEncode"));
        if (beforeTransEncode != null && beforeTransEncode.length() > 0) {
            try {
                return new String(filename.getBytes(beforeTransEncode), afterTransEncode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return filename;
            }
        }
        return filename;
    }
    /**
     * 截取宽度固定字符串，若字符串宽度大于参数设置，则按照参数截取加上"..."
     *
     * @param string
     *            待截取字符串
     * @param startIndex
     *            截取开始位置
     * @param width
     *            宽度（一个汉字宽度为宽度单位）
     * @return 截取后的字符串
     */

    public static String getWidthFixedStr(String string, int startIndex, int width, boolean htmlStr) {
        if (string == null || string.equals(""))
            return "";
        if (startIndex < 0)
            return "";
        if (width < 0)
            return "";

        char baseChar = ' '; // char = 32
        char topChar = '~'; // char = 126
        char c;
        double tempWidth = 0.0;
        int charIndex = 0;
        string = string.substring(startIndex);
        StringBuffer sb = new StringBuffer();
        String resultStr;

        string = string.replaceAll("&lt;", "<");
        string = string.replaceAll("&gt;", ">");
        string = string.replaceAll("&nbsp;", " ");
        string = string.replaceAll("&quot;", "\"");

        while (charIndex <= string.length() - 1 && tempWidth <= width - 1) {
            c = string.charAt(charIndex);
            if (c >= baseChar && c <= topChar)
                tempWidth += 0.5;
            else
                tempWidth += 1; // 汉字加1
            sb.append(c);
            charIndex++;
        }
        resultStr = sb.toString();
        if (resultStr.length() < string.length()) {
            sb.deleteCharAt(resultStr.length() - 1);
            sb.append("...");
            resultStr = sb.toString();
        }
        if (htmlStr) {
            resultStr = resultStr.replaceAll("<", "&lt;");
            resultStr = resultStr.replaceAll(">", "&gt;");
            resultStr = resultStr.replaceAll(" ", "&nbsp;");
            resultStr = resultStr.replaceAll("\"", "&quot;");
        }
        return resultStr;
    }

    /**
     * 对于字符串为空的判断处理
     *
     * @param src
     *            String 要判断的原始字符串
     * @param defaultValue
     *            String 假如原始字符串为空的时候需要返回的默认字符串，假如该参数为空并且原始字符串为空返回长度为零的字符串
     * @param isTrim
     *            boolean 是否需要截断空格
     * @return String
     * @author huangjiansong
     */
    public String strIfNull(String src, String defaultValue, boolean isTrim) {
        if (src == null)
            src = "";
        if (isTrim)
            src = src.trim();
        if (defaultValue != null && src.length() == 0)
            return defaultValue;
        return src;
    }

    /**
     * 字符串处理
     *
     * @param src
     *            String
     * @return String
     */
    public String strIfNull(String src) {
        if (src == null)
            src = "";

        return src;
    }

    /**
     * 字符串处理
     *
     * @param src
     *            String
     * @return String
     */
    public String strChangeZeroByDouble(String src) {
        if (src == null)
            src = "";

        if (Double.parseDouble(src) == 0) {
            src = "";
        }

        return src;
    }

    public static String getHtmlSpace(String str) {
        if (str.length() == 0) {
            return "&nbsp";
        } else {
            return str;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static final String strIfNull(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof String) {
            return (String) obj;
        } else {
            return obj.toString();
        }
    }

    /**
     * 替换字符串中的回车和换行符
     *
     * @author jk
     * @param str
     * @return
     */
    public static final String crToChar(String str) {

        char[] strChar = str.toCharArray();

        // P.p("AssetsMng.main, ArraySize: " + strChar.length);

        for (int c = 0; c < strChar.length; c++) {
            if (strChar[c] == 10 || strChar[c] == 13) {
                char[] strCharTmp = new char[strChar.length + 1];

                int cc = 0;
                for (; cc < c; cc++) {
                    strCharTmp[cc] = strChar[cc];
                }

                //strCharTmp[cc++] = '\\';
                if (strChar[c] == 10) {
                    strCharTmp[cc++] = 'n';
                } else {
                    strCharTmp[cc++] = 'r';
                }

                for (; cc < strChar.length; cc++) {
                    strCharTmp[cc] = strChar[cc - 1];
                }

                strCharTmp[strCharTmp.length - 1] = strChar[strChar.length - 1];
                strChar = strCharTmp;
            }
        }

        return new String(strChar);
    }

    public static String getStringNowTime() {
        return (new SimpleDateFormat(DateUtil.DEFAULT_TIME_PATTERN)).format(new Date());
    }

    public static String getStringNowDate() {
        return (new SimpleDateFormat(DateUtil.DEFAULT_DATE_PATTERN)).format(new Date());
    }

    /**
     * 从Blob中获取字符串
     *
     * @param blob
     *            Blob对象
     * @return 字符串
     */
    public static String fromBlob(Blob blob) {
        if (blob == null) {
            return "";
        }
        // StringBuffer sb = new StringBuffer();
        try {

            byte[] array = new byte[1000];
            InputStream in = blob.getBinaryStream();

            /**
             * hjs 为了修改乱码的问题
             */
            // 保存每次读取的字节列表
            Vector v = new Vector();

            // 保存每次读取的字节长度
            Vector v1 = new Vector();
            int len = in.read(array, 0, 1000);

            // 保存所有字节的长度
            int alllen = 0;
            while (len > 0) {
                alllen = alllen + len;
                v.add(array);
                v1.add(new Integer(len));
                array = new byte[1000];
                len = in.read(array, 0, 1000);
            }
            if (alllen > 0 && v.size() > 0) {
                byte[] arraytemp = new byte[alllen];
                int index = 0;
                for (int i = 0; i < v.size(); i++) {
                    int lentemp = ((Integer) v1.get(i)).intValue();
                    byte[] arraytt = (byte[]) v.get(i);
                    System.arraycopy(arraytt, 0, arraytemp, index, lentemp);
                    index = index + lentemp;
                }

                return new String(arraytemp, ConstantUtils.obj.getSysEnvironment().get("afterTransEncode.get").toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从clob中获取字符串
     *
     * @param clob
     *            clob对象
     * @return 字符串
     */
    public static String fromClob(Clob clob) {
        if (clob == null) {
            return "";
        }
        String str = "";
        try {
            str = clob != null ? clob.getSubString(1, (int) clob.length()) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 把字符串中的回车符转换成指定的字符
     *
     * @param str
     *            字符串
     * @param wrapStr
     *            替换字符串
     * @return 字符串
     */
    public final static String transWrapToString(String str, String wrapStr) {
        StringBuffer sb = new StringBuffer(str);
        int notCheckedLength = sb.length();
        int start = 0;
        int wrapCount = WRAP.length;
        for (int j = 0; j < wrapCount; j++) {
            notCheckedLength = sb.length();
            start = 0;
            while (notCheckedLength > 0) {
                String temp = sb.substring(start);
                int index = temp.indexOf(WRAP[j]);
                if (index == -1) {
                    break;
                }

                notCheckedLength -= (index + WRAP[j].length());
                start += index;
                sb.replace(start, start + WRAP[j].length(), wrapStr);
                start += wrapStr.length();
            }
        }
        return sb.toString();
    }

    /**
     * HTML转义
     *
     * @param str
     * @return
     */
    public static String escape2HTML(String str) {
        return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;").replaceAll("\"", "&quot;")
                .replaceAll("\n", "<BR>").replaceAll("\\s", "&nbsp;");
    }

    public static String getHumanFilesize(long filesize) {

        String s = "";

        if (filesize >= (1024 * 1024 * 1024)) {
            s = Integer.toString(Math.round(filesize / (1024 * 1024 * 1024)
                    * 100) / 100)
                    + "Gb";
        } else if (filesize >= (1024 * 1024)) {
            s = Integer
                    .toString(Math.round(filesize / (1024 * 1024) * 100) / 100)
                    + "Mb";
        } else if (filesize >= 1024) {
            s = Integer.toString(Math.round(filesize / 1024 * 100) / 100)
                    + "Kb";
        } else if (filesize > 0) {
            s = Long.toString(filesize) + "bit";
        } else {
            s = "-";
        }

        return s;
    }

    private final static String WRAP[] = { "\r\n", "\r", "\n" }; // wrap

    public static void main(String[] args) {
        System.out.println(escape2HTML("aaa\nbbbnn"));
    }

    //将全角的汉字换成半角的
    public static String turnTheAlltoTheHalf(String str){
        str = getStr(str);
        str = str.replaceAll("０","0");
        str = str.replaceAll("１","1");
        str = str.replaceAll("２","2");
        str = str.replaceAll("３","3");
        str = str.replaceAll("４","4");
        str = str.replaceAll("５","5");
        str = str.replaceAll("６","6");
        str = str.replaceAll("７","7");
        str = str.replaceAll("８","8");
        str = str.replaceAll("９","9");
        return str;
    }

    /**
     * 获取一个字符串,null转换为空字符串
     *
     * @param src
     *            源对象
     * @return 字符串
     */
    public static String getStr(Object src) {

        return getStr(src, -1);

    }

    public static String getStr(int src) {

        return String.valueOf(src);
    }

    public static String getStr(long src) {

        return String.valueOf(src);

    }

    /**
     * 获取一个一定长度的字符串,null转换为空字符串
     *
     * @param src
     *            源对象
     * @param length
     *            字符串长度
     * @return 字符串
     */
    public static String getStr(Object src, int length) {
        if (src == null) {
            return "";
        }

        if (src instanceof String) {
            return (String) src;
        }

        String value = String.valueOf(src);

        return value.length() > length && length != -1 ? value.substring(0,
                length) : value;

    }

    public static String changeEncoding(String src){
        try {
            String beforeTransEncode = StringUtil.strIfNull(ConstantUtils.obj.getSysEnvironment().get("beforeTransEncode.get"));
            String afterTransEncode = StringUtil.strIfNull(ConstantUtils.obj.getSysEnvironment().get("afterTransEncode.get"));
            if (beforeTransEncode != null && beforeTransEncode.length() > 0) {
                src = new String(src.getBytes(beforeTransEncode),afterTransEncode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return src;
    }
}
