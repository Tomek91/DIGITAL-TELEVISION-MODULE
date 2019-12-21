package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelDTO {
    private Long id;
    private String name;
    private String packageName;
    private CategoryDTO categoryDTO;
}
