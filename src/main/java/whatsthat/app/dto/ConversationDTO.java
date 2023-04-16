package whatsthat.app.dto;

import whatsthat.app.entity.User;

import java.util.HashSet;
import java.util.Set;

public class ConversationDTO {
    private Long id;
    private String name;
    private Set<UserDTO> participants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserDTO> participants) {
        this.participants = participants;
    }
}
