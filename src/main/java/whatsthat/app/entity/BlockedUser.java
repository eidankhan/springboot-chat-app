package whatsthat.app.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "blocks")
public class BlockedUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private User blockedUser;

    public BlockedUser() {
    }

    public BlockedUser(User loggedInUser, User blockedUser) {
        this.user = loggedInUser;
        this.blockedUser = blockedUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(User blockedUser) {
        this.blockedUser = blockedUser;
    }
}
