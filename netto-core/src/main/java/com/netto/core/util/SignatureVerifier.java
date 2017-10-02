package com.netto.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SignatureVerifier {



    private static Charset charset = Charset.forName("utf-8");





    private final static String[] salts = { "nCQge'g`c5p", "%x6a}?;C};?", "<IF7<5N,W~r", "'Z~ioo#sh8V", "Vp+U-J}HrE_",
            "Y.;2..P4hO}", "@!M*8$pntzD", "ECJ9g+HB8bT", "p&YZUW4tXa~", "do_Zk%]2G(J", "3rRKV(pH.K#", "#7X[wWL]H,8",
            "1tFX|ph1c;[", "CLbQvd5y{lU", "E#Qm.i#>_Hv", "9_m{yI7>uyo", "^%hZp?O7A!%", "i'lJNc(gCtd", "?{\\6uL$UPk0",
            "+_YL_]zJdgs", "Lzzop_/1[kF", "8I'h67,Audl", "Bg!'Pt:|o6m", "t{qQh8$\"a+i", "Bt98Z'n|no$", "d>=UT+7I()z",
            "A}p$*[zKQ5c", "<R)+B-J(Kf\\", "qrIy{Di>C[A", "7=vgQZrl.{w", ")BWT*4S{({u", "A_Bd<c1R!):", "c.?q+8B4Yxg",
            ".<_58V9Y7Z>", ">O%?X?p=M1/", "HPj#JSfXnFl", "|'`4a~e!Oj?", "*1EWAS/i%yk", "MR(<xtCuz%+", "%o]S[|\\e.\"=",
            "UL8YF%*sW20", "'SG#XQ_\\B>1", ";lbHn!vDL0}", "5(ek:u=@JcC", "6$_WBpU\\^9&", "9|qd-oWAw>.", "4JR^.uaDyB{",
            "4Qwqj}@$z3h", "#AIz^V.r!_Q", "V4RPUNkh!d[", "bzo].Xe1x0-", "e:Kfy{tP1H\"", "vlOwQ+c5'Sr", "-XF&gR^Nl*5",
            "'+87S9|J'LC", "V'l]Y_qe99k", "jJPWSe?ZoVp", "omnu:2Np8;O", "zBX4ZD6Fne}", "K=|<sm_c\\NZ", "`)hwD8j?YCS",
            "ghZVOXxyuu7", "duNAE)8&2\"}", "9itq-I's3aJ", ":D\\0:ry~hH@", "QX5cY4:q|/d", "Xk}jMILgm)v", "zp'd:Gsj~)N",
            "<hL:w2DP}C;", "lg4Zp,ak{hQ", "1E\"1NPii97$", "HHaFj|rWd'2", "2sB/]sEm9G}", "whQ2~UCH}%n", "\"aAfgS=yG^)",
            "SNsltqTl[&}", "[A$YFqCGSc/", "7K6^*>d]kXJ", "J}N'%Ma_md:", "V\\[+AiFW5[7", "yz{eSFH}Eu%", "Cf*2KDF\"\"\",",
            "jRy!.1?(-<m", "b6_(-dQPK[a", "~(w!)%CsV=t", "N5kZPY<3oz;", "_lM+I/!I7xI", "|l4S**8W>$3", "]N\"MI=J)*v4",
            "'5{=.F\\+3p^", "yur8x&&VT(%", "DO'NF:\"MN}i", "DFfV8Eq2;eI", "kOj@Wo={?DJ", "]L3-J|hmDPE", "u7m1|8Fhg2*",
            "\"G;Aje'I2:U", "7?K[no7d(&u", "=<m%MvCO>]o", "DvQV1(3HF^$" };

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    public static boolean  verify(byte[] data, int saltIndex, String requestSignature) throws IOException {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            
            messageDigest.update(data);
            String salt = salts[saltIndex];
            messageDigest.update(salt.getBytes(charset));
            String md5Digest = encodeHex(messageDigest.digest());
                       


            if (!md5Digest.equals(requestSignature)) {
                return false;
            }
            else{
                return true;
            }
            
            
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        
        
    }
    

    protected static String encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
    
    public static String createSignature(String data) throws UnsupportedEncodingException{
        
        int saltIndex = randomSaltIndex();
        
        String timestampStr = RandomStrGenerator.createTimestampStr(RandomStrGenerator.HOUR_TIMESTAMP);
        
        String signature = RandomStrGenerator.mixedUp(timestampStr,RandomStrGenerator.md5(data+salts[saltIndex]))+","+saltIndex;
        
        return signature;
    }
    

    public static int randomSaltIndex(){
        Random random = ThreadLocalRandom.current();
        return random.nextInt(salts.length);
    }

}
