package com.inditex.icdmsuscon.emissions.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.inditex.icdmsuscon.emissions.entity.EmissionEntity;

public class EmissionEntityCsvParser {

  public static List<EmissionEntity> readCsv(String filePath) throws Exception {
    try (Reader reader = new FileReader(filePath)) {
      CsvToBean<EmissionEntity> csvToBean = new CsvToBeanBuilder<EmissionEntity>(reader)
          .withType(EmissionEntity.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build();

      return csvToBean.parse();
    }
  }
}
