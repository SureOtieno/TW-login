package com.example.tendawaks.utils;


import com.example.tendawaks.dto.Pagination;
import com.example.tendawaks.utils.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GenericUtils {

    public static ResponseEntity<Object> noRecordsFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponse.GenericResponseData.builder()
                        .status(ResponseStatus.FAILED.getStatus())
                        .data(new ArrayList<>())
                        .message(Constants.NO_RECORD_FOUND)
                        .msgDeveloper(Constants.NO_RECORD_FOUND)
                        .build());
    }

    public static Map<String, Object> getRecordsResponse(Map<String, String> getParams, Pagination pagination, List<?> ls)
    {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(Constants.PAGE, pagination.getPage());
        map.put(Constants.PAGESIZE, pagination.getPageSize());
        map.put(Constants.TOTALPAGES, pagination.getTotalPages());
        map.put(Constants.TOTALCOUNT, pagination.getTotalCount());
        map.put("data", ls);

        Map responsesLs = new HashMap<>();
        responsesLs.put("data", ls);

        return getParams.containsKey(Constants.PAGE) ? map : responsesLs;
    }
}
