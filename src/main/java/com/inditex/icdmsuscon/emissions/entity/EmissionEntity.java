package com.inditex.icdmsuscon.emissions.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmissionEntity {

  private UUID id;

  @CsvBindByName(column = "marketId")
  private UUID marketId;

  @CsvBindByName(column = "unitId")
  private UUID unitId;

  @CsvBindByName(column = "factor")
  private BigDecimal factor;

}
