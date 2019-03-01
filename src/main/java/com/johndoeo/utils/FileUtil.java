package com.johndoeo.utils;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileUtil {
    /**
     * 拷贝文件
     *
     * @param in 需要拷贝的路径+文件名
     * @param out 拷贝后的路径+文件名
     * @throws IOException
     */
    public static boolean fileCopy(String in, String out){
        if(in == null || in.trim().equals("")){
            System.out.println("拷贝文件为空");
            return false;
        }
        if(out == null || out.trim().equals("")){
            System.out.println("输出文件为空");
            return false;
        }
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(new File(in)).getChannel();
            outChannel = new FileOutputStream(new File(out)).getChannel();
            // inChannel.transferTo(0, inChannel.size(), outChannel); // original -- apparently has trouble copying large files on Windows
            // magic number for Windows, 64Mb - 32Kb)
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 读文件
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            // 使用jdk1.7+的try with resource 语句，无需进行资源释放
            try (RandomAccessFile ra = new RandomAccessFile(new File(filePath), "r");
                 FileChannel channel = ra.getChannel()) {
                ByteBuffer cb = ByteBuffer.allocate(6 * 1024 * 1024);
                while (channel.read(cb) != -1) {
                    sb.append(new String(cb.array(), "UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


    /**
     * 写文件
     * @param content
     * @param filePath
     * @return
     */
    public static boolean writeToFile(String content, String filePath) {
        byte[] bbb = new byte[20 * 1024 * 1024];
        try {
            // 使用jdk1.7+的try with resource 语句，无需进行资源释放
            try (RandomAccessFile raf = new RandomAccessFile(new File(filePath), "rw");
                 FileChannel fc = raf.getChannel()) {
                ByteBuffer bf = ByteBuffer.wrap(content.getBytes(Charset.forName("UTF-8")));
                fc.write(bf);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Test
    public void text001(){
//        final String s = readFile("C:\\Users\\Administrator\\Desktop\\新建文件夹\\tingyun\\ReleaseNote.md");
        final boolean s = writeToFile("6666666666666598546", "C:\\Users\\Administrator\\Desktop\\新建文件夹\\text.txt");
        System.out.println(s);
    }
}
