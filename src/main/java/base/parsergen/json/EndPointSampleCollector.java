package base.parsergen.json;

import kamserverutils.common.util.FileUtil;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;

public class EndPointSampleCollector {

    public static void main(final String... args) throws IOException, InterruptedException {
        final String rootPath = "/tmp/nhl_json";
        final File dir = new File(rootPath);
        FileUtil.attemptDeleteFile(dir);
        dir.mkdirs();
        for (final EndPointDesc endPointDec : NHLEndPointDescs.END_POINTS) {
            System.out.println("Working " + endPointDec.name);
            if (endPointDec.queryBySeason) {
                final String url = endPointDec.getBySeasonSampleUrl();
                final String body = Jsoup.connect(url)
                        .maxBodySize(0)
                        .timeout(30000)
                        .ignoreContentType(true)
                        .execute()
                        .body();
                if (body.length() < 25) {
                    System.out.println(url);
                }
                FileUtil.stringToFile(rootPath + "/" + endPointDec.name + "BySeason.json", body);
            }
            if (endPointDec.queryByGame) {
                final String url = endPointDec.getByGameSampleUrl();
                final String body = Jsoup.connect(url)
                        .maxBodySize(0)
                        .timeout(30000)
                        .ignoreContentType(true)
                        .execute()
                        .body();
                if (body.length() < 25) {
                    System.out.println(url);
                }

                FileUtil.stringToFile(rootPath + "/" + endPointDec.name + "ByGame.json", body);
            }
            Thread.sleep(500);
        }
    }
}
