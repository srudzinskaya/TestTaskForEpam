package jsonplaceholder.api.functionaltests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentItem {
    @JsonProperty("postId")
    int postId;
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
    @JsonProperty("email")
    String email;
    @JsonProperty("body")
    String body;
}