package com.example.shape.Util;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.apache.http.HttpEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/***
 * 下载研发文件流
 */
public class FileUtil {

    /**
     * Logger实例
     */
    static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    //删除文件以及文件夹

    /**
     * 删除文件或文件夹
     *
     * @param directory
     */
    public void delAllFile(File directory) {
        if (!directory.isDirectory()) {
            directory.delete();
        } else {
            File[] files = directory.listFiles();
            // 空文件夹
            if (files.length == 0) {
                directory.delete();
//                System.out.println("删除" + directory.getAbsolutePath());
                return;
            }
            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    delAllFile(file);
                } else {
                    file.delete();
//                    System.out.println("子文件夹删除" + file.getAbsolutePath());
                }
            }
            // 删除文件夹本身
            directory.delete();
            System.gc();
            System.out.println("文件夹删除" + directory.getAbsolutePath());
        }
    }


    /**
     * 删除文件或文件夹，MP4
     *
     * @param directory
     */
    public void delAllFileMP4(File directory,File parentdirectory) {
        if (!directory.isDirectory()) {
            directory.delete();
        }
        File[] files = parentdirectory.listFiles();
        // 空文件夹
        if (files!=null&&files.length == 0) {
            parentdirectory.delete();
        }
        System.gc();
    }


    //删除文件方法
    public void deltefilemp4(boolean deletefile, String filePathName, String descDirPath) {
        if (deletefile) {
           //获取父文件夹以及删除文件
            File deletefilePathName = new File(filePathName);
            File parentdirectory = new File(deletefilePathName.getParent());

            delAllFileMP4(deletefilePathName,parentdirectory);
        }
    }


    //删除文件方法
    public void deltefile(boolean deleteZip, boolean deleteunpack, String filePathName, String descDirPath) {
        if (deleteZip && deleteunpack) {
            //删除zip文件包

            File deletefilePathName = new File(filePathName);
            delAllFile(deletefilePathName);
            //删除解压文件
            File deleteunpackfile = new File(descDirPath);
            delAllFile(deleteunpackfile);

        } else if (deleteZip && deleteunpack == false) {
            //删除zip文件包
            File deletefilePathName = new File(filePathName);
            delAllFile(deletefilePathName);
        }
    }


    public String getGeometryWkt(RestTemplate restTemplate, String originSRWktUrl, String targetSRWktUrl, String shape, String geometryUrl) {
        String shapeData = null;
        Map<String, Object> geometryMap = new HashMap<>();
        geometryMap.put("originSRWkt", originSRWktUrl);
        geometryMap.put("targetSRWkt", targetSRWktUrl);
        List<String> shapeList = new ArrayList<>();
        shapeList.add(shape);
        geometryMap.put("geometry_list", shapeList);
        //发送post请求
        ResponseEntity<Map> result = restTemplate.postForEntity(geometryUrl, geometryMap, Map.class);
        Map geoshape = result.getBody();
        if (geoshape != null) {
            if (geoshape.containsKey("state") && geoshape.containsKey("data") && geoshape.get("state").equals(1)) {
                List<String> newShape = (List<String>) geoshape.get("data");
                shapeData = newShape.get(0);
                logger.info("空间数据转换成功");
            } else logger.error("错误：转换接口状态为" + geoshape.get("state"));
        } else logger.error("错误：转换接口返回为空");
        return shapeData;
    }


    public ByteArrayOutputStream getByteArrayOutputStream(MultipartFile file) throws IOException {
        InputStream inputStream=null;
        ByteArrayOutputStream baos=null;
        try {
             inputStream = file.getInputStream();

            baos  = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            baos.close();
            return baos;
        } catch (Exception e) {
            logger.error("转换成ByteArrayOutputStream错误：" + e);
            return null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (baos != null) {
                baos.close();
            }

        }
    }





    /**
     * 获取类型
     * @param byteArrayOutputStream
     * @return
     * @throws IOException
     */
    public String getFileType(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        InputStream judgeInputStream = null;
        try {
            //获取判断类型的流
            judgeInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            //判断类型
            byte[] b = new byte[3];
            judgeInputStream.read(b, 0, b.length);
            String typecode = TypeDict.bytesToHexString(b);
            typecode = typecode.toUpperCase();
            String type = TypeDict.checkType(typecode);
            return type;
        } catch (Exception e) {
            logger.error("判断类型错误：" + e);
            return null;
        } finally {
            if (judgeInputStream != null) {
                judgeInputStream.close();
            }

        }
    }

    //下载文件
    public boolean downLoadFile(ByteArrayOutputStream byteArrayOutputStream, String filePathName) {
        try {
            //获取流信息下载
            InputStream downloadInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//            String generateFileName = this.getClass().getClassLoader().getResource("temporaryDirectory").getPath();//获取文件路径
            //下载文件到指定目录
            OutputStream out = new FileOutputStream(filePathName);
            int size = 0;
            //流只能读取一次,并且不能复制
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = downloadInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            downloadInputStream.close();
            return true;
        } catch (Exception e) {
            logger.error("下载文件到指定目录：" + e);
            return false;
        }

    }

    //解压zip文件
    public boolean UnpackZipFile(String filePathName, String descDirPath) {
        ZipFile zip = null;
        InputStream in = null;
        OutputStream out1 = null;
        try {
            long start = System.currentTimeMillis();
            File zipFile = new File(filePathName);
            //判断解压的文件是否存在
            if (!zipFile.exists()) {
                logger.error("需解压文件不存在");
            }
            zip = new ZipFile(zipFile, Charset.forName("GBK"));
            File descDirFile = new File(descDirPath);
            //解压的文件夹是否存在
            if (!descDirFile.exists()) {
                descDirFile.mkdirs();
            }
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
//                System.err.println(zipEntryName);
                in = zip.getInputStream(entry);
                String outPath = (descDirPath + File.separator + zipEntryName).replaceAll("\\*", "/");
//                System.err.println(outPath);
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                // 输出文件路径信息
                out1 = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len2;
                while ((len2 = in.read(buf1)) > 0) {
                    out1.write(buf1, 0, len2);
                }

            }

            logger.info("文件:{}. 解压路径:{}. 解压完成. 耗时:{}ms. ", filePathName, descDirPath, System.currentTimeMillis() - start);
            return true;
        } catch (Exception e) {
            logger.error("解压文件错误：" + e);
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out1 != null) {
                    out1.close();
                }
                if (zip != null) {
                    zip.close();
                }
            } catch (Exception e) {
                logger.error("关闭连接");
            }

        }
    }


    /**
     *
     * @param filePathName
     * @param destPath
     */
    public  boolean unPackRar(String  filePathName,String destPath) {
        File rarFile = new File(filePathName);
        try (Archive archive = new Archive(rarFile)) {
            if (null != archive) {
                FileHeader fileHeader = archive.nextFileHeader();
                File file = null;
                while (null != fileHeader) {
                    // 防止文件名中文乱码问题的处理
                    String fileName = fileHeader.getFileNameW().isEmpty() ? fileHeader.getFileNameString() : fileHeader.getFileNameW();
                    if (fileHeader.isDirectory()) {
                        //是文件夹
                        file = new File(destPath + File.separator + fileName);
                        file.mkdirs();
                    } else {
                        //不是文件夹
                        file = new File(destPath + File.separator + fileName.trim());
                        if (!file.exists()) {
                            if (!file.getParentFile().exists()) {
                                // 相对路径可能多级，可能需要创建父目录.
                                file.getParentFile().mkdirs();
                            }
                            file.createNewFile();
                        }
                        FileOutputStream os = new FileOutputStream(file);
                        archive.extractFile(fileHeader, os);
                        os.close();
                    }
                    fileHeader = archive.nextFileHeader();
                }
            }
        } catch (Exception e) {

            logger.error("解压失败：", e.getMessage(), e);
            return false;
        }
        return true;
    }



    public List<String> findFileName(String descDirPath, List<String> nameList) {
        try {
            File file = new File(descDirPath);
            //查找文件类型
            // 获得该文件夹内的所有文件
            File[] array = file.listFiles();
            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile())//如果是文件
                {
                    // 只输出文件名字
//                  System.out.println(array[i].getName());
                    //只获取后缀名为shp的文件
                    String[] strArray = array[i].getName().split("\\.");
                    int suffixIndex = strArray.length - 1;
//                  System.out.println(array[i].getPath());
                    if (strArray[suffixIndex] != null && strArray[suffixIndex].toLowerCase().equals("shp")) {
                        nameList.add(array[i].getPath());
                    }
                } else if (array[i].isDirectory())//如果是文件夹
                {
                    //获取业务
//                    System.out.println(array[i].getName());
                    //System.out.println(array[i].getPath());
                    //文件夹需要调用递归 ，深度+1
                    findFileName(array[i].getPath(), nameList);
                }
            }

            return nameList;
        } catch (Exception e) {
            logger.error("获取解压文件所有内容错误：" + e);
            return null;
        }
    }


    public Map findFilecpg(String descDirPath, List<String> nameList,List<String> cpgnameList) {
        Map<String,Object> map=new HashMap<>();
        try {
            File file = new File(descDirPath);
            //查找文件类型
            // 获得该文件夹内的所有文件
            File[] array = file.listFiles();
            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile())//如果是文件
                {
                    // 只输出文件名字
//                  System.out.println(array[i].getName());
                    //只获取后缀名为shp的文件
                    String[] strArray = array[i].getName().split("\\.");
                    int suffixIndex = strArray.length - 1;
//                  System.out.println(array[i].getPath());
                    if (strArray[suffixIndex] != null && strArray[suffixIndex].toLowerCase().equals("shp")) {
                        nameList.add(array[i].getPath());
                    }
                    if (strArray[suffixIndex] != null && strArray[suffixIndex].toLowerCase().equals("cpg")) {
                        cpgnameList.add(array[i].getPath());
                    }
                } else if (array[i].isDirectory())//如果是文件夹
                {
                    //获取业务
//                    System.out.println(array[i].getName());
                    //System.out.println(array[i].getPath());
                    //文件夹需要调用递归 ，深度+1
                    findFilecpg(array[i].getPath(), nameList,cpgnameList);
                }
            }
            map.put("nameList",nameList);
            map.put("cpgnameList",cpgnameList);
            return map;

        } catch (Exception e) {
            logger.error("获取解压文件所有内容错误：" + e);
            return null;
        }
    }


    /**
     *
     * @param path
     * @return
     * @throws Exception
     */
    public  String getShapeFileCharsetName(String path) throws Exception {
        File pFile = new File(path);
        if (pFile.exists() && !pFile.isFile()) {
            return "GBK";
        }
        File file = new File(path);
        String encode = "GBK";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                if ("UTF-8".equals(tempString.toUpperCase())) {
                    encode = "UTF-8";
                    break;
                }
                break;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return encode;
    }







}

