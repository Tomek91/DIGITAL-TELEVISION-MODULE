package pl.com.app.mappers;

import org.springframework.stereotype.Service;
import pl.com.app.dto.CategoryDTO;
import pl.com.app.dto.ChannelDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParamsMapMapper {

    public List<ChannelDTO> paramsMapToChannelDTOList(ResponseMessage message) {
        try {
            if (message == null) {
                throw new NullPointerException("MESSAGE IS NULL");
            }

            List<Map<String, Object>> map = (List<Map<String, Object>>) message.getData();

            return map
                    .stream()
                    .map(this::paramsMapToChannelDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.MAPPERS, e.getMessage());
        }
    }

    public List<CategoryDTO> paramsMapToCategoryDTOList(ResponseMessage message) {
        try {
            if (message == null) {
                throw new NullPointerException("MESSAGE IS NULL");
            }

            List<Map<String, Object>> map = (List<Map<String, Object>>) message.getData();

            return map
                    .stream()
                    .map(this::paramsMapToCategoryDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.MAPPERS, e.getMessage());
        }
    }

    private ChannelDTO paramsMapToChannelDTO(Map<String, Object> map) {
        try {
            if (map == null) {
                throw new NullPointerException("MAP IS NULL");
            }

            Long id = map.get("id") == null ? null : Long.valueOf(map.get("id").toString());
            String name = map.get("name") == null ? null : map.get("name").toString();
            String packageName = map.get("packageName") == null ? null : map.get("packageName").toString();
            CategoryDTO categoryDTO = mapToCategoryDTO(map);
            return ChannelDTO.builder().id(id).name(name).packageName(packageName).categoryDTO(categoryDTO).build();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.MAPPERS, e.getMessage());
        }
    }

    private CategoryDTO paramsMapToCategoryDTO(Map<String, Object> map) {
        try {
            if (map == null) {
                throw new NullPointerException("MAP IS NULL");
            }

            Long id = map.get("id") == null ? null : Long.valueOf(map.get("id").toString());
            String name = map.get("name") == null ? null : map.get("name").toString();

            return CategoryDTO.builder().id(id).name(name).build();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.MAPPERS, e.getMessage());
        }
    }

    private CategoryDTO mapToCategoryDTO(Map<String, Object> map) {
        try {
            if (map == null) {
                throw new NullPointerException("MAP IS NULL");
            }

            if (map.get("categoryDTO") != null) {
                Map<String, Object> categoryMap = (Map<String, Object>) map.get("categoryDTO");
                return paramsMapToCategoryDTO(categoryMap);
            }
            return null;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.MAPPERS, e.getMessage());
        }
    }
}
