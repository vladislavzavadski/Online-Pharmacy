package by.training.online_pharmacy.dao.impl.database.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vladislav on 31.08.16.
 */
public class ListConverter {
    public static List<String> toStringList(List<Integer> integerList){
        List<String> result = new ArrayList<>(integerList.size());
        result.addAll(integerList.stream().map(item -> String.valueOf(item)).collect(Collectors.toList()));
        return result;
    }
}
