package org.zeorck.ex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.IntStream;

public class CrawlingTest {

    public static void main(String[] args) throws Exception{

        final String savePath = "C:\\movies";

        String url ="https://www.lottecinema.co.kr/LCAPI/Home/getMovie";

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";

        String payload ="{\"channelType\":\"HO\",\"osType\":\"W\",\"osVersion\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36\",\"multiLanguageId\":\"KR\",\"data\":{\"memberNoOn\":\"0\"}}";

        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //System.out.println(response.toString());
            JsonElement jsonElement = JsonParser.parseString(response.toString());

            //log.info(jsonElement);

            JsonObject jsonObject  = jsonElement.getAsJsonObject();

            JsonArray movies = jsonObject.getAsJsonObject("Movies").getAsJsonArray("Items").get(0).getAsJsonObject().getAsJsonArray("Items");

            //System.out.println(movies);

            //System.out.println(jsonObject.getAsJsonArray("Items"));

            for (int i = 0; i < movies.size() ; i++) {

                JsonObject movie = movies.get(i).getAsJsonObject();

                String movieTitle = movie.get("MovieNameKR").getAsString();
                String posterURL = movie.get("PosterURL").getAsString();
                System.out.println("------------------------");

                try (FileOutputStream fos = new FileOutputStream(savePath+ File.separator+movieTitle+".jpg");
                     InputStream in = new URL(posterURL).openStream();){



                    byte[] buffer = new byte[1024 * 8];

                    while (true) {

                        int count = in.read(buffer);

                        if (count == -1) {
                            break;
                        }

                        fos.write(buffer, 0, count);

                    }

                    fos.close();
                    in.close();
                }catch(Exception e){

                }



            }




        }catch(Exception ee) {
            ee.printStackTrace();
        }
    }
}
