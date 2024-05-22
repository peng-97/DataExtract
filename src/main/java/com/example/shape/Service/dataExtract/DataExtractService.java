package com.example.shape.Service.dataExtract;

import java.util.List;
import java.util.Map;

public interface DataExtractService {
   public boolean dataExtract(String type);

   public List<Map<String, Object>> getSource(String sql);

   public List<Map<String, Object>> getSource1(String sql);
}
