package ch.hearc.jee2024.discussionappproject.model;

import jakarta.persistence.*;

@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Discussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(Discussion discussion) {
        this.discussion = discussion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
