package com.github.three_old_coders.blueprint.common;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public abstract class CsvGroupBuilder
{
    protected final CSVParser _csvParser;
    protected final String _groupColumn;

    public CsvGroupBuilder(final CSVParser csvParser, final String groupColumn)
    {
        _csvParser = csvParser;
        _groupColumn = groupColumn;
    }

    public void process()
    {
        final List<CSVRecord> csvRecords = new ArrayList<>();
        for (final CSVRecord csvRecord : _csvParser) {
            if (csvRecords.size() == 0 || csvRecords.get(0).get(_groupColumn).equals(csvRecord.get(_groupColumn))) {
                csvRecords.add(csvRecord);
            } else {
                handleGroup(csvRecords);
                csvRecords.clear();
                csvRecords.add(csvRecord);
            }
        }

        if (csvRecords.size() > 0) {
            handleGroup(csvRecords);
        }
    }

    protected abstract void handleGroup(final List<CSVRecord> items);
}
