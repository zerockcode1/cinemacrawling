package org.zeorck.ex;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NaverTest {

    public static void main(String[] args)throws Exception {

        String url ="https://movie.naver.com/movieChartJson.naver?type=BOXOFFICE";

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36";


        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("ajax", "true");
        con.setRequestProperty("x-requested-with", "XMLHttpRequest");
        con.setRequestProperty("referer","https://movie.naver.com/");
        con.setDoOutput(true);

        InputStream in = con.getInputStream();

        System.out.println(in);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024*8];

        while(true){
            int count = in.read(buffer);

            if(count == -1){ break; }

            bos.write(buffer,0,count);
        }

        in.close();

        String str = new String(bos.toByteArray());

        System.out.println(str);


    }
}
