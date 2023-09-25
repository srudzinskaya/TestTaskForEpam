package jsonplaceholder.api.functionaltests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostItem {
    @JsonProperty("userId")
    int userId;
    @JsonProperty("id")
    int id;
    @JsonProperty("title")
    String title;
    @JsonProperty("body")
    String body;
}