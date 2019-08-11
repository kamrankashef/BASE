package main;

import common.FileUtil;

import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
common.InitDatabase;
import playerscouting.model.PlayerScoutingFeed

public class PlayerScoutingFeedParser {

    public static void main(final String ... args) throws Exception {

        final String fileName = args[0];
        final Reader in = new FileReader(fileName);
        final CSVParser records = CSVFormat.EXCEL.withHeader().parse(in);
        final Set<String> headers = records.getHeaderMap().keySet();

        try (Connection conn = InitDatabase.getConnection(false)) {

            for (CSVRecord record : records) {
                final PlayerScoutingFeed playerScoutingFeed = PlayerScoutingFeed.fromCSVRecord(headers, record);
            }
            conn.commit();
        }
    }

}
