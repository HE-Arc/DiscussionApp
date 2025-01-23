package ch.hearc.jee2024.discussionappproject;

import ch.hearc.jee2024.discussionappproject.model.Category;
import ch.hearc.jee2024.discussionappproject.model.Discussion;
import ch.hearc.jee2024.discussionappproject.model.Response;
import ch.hearc.jee2024.discussionappproject.model.Role;
import ch.hearc.jee2024.discussionappproject.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class DiscussionAppProjectApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User adminUser;
    private User normalUser;
    private Category category;
    private Discussion discussion;

    @BeforeEach
    public void setup() throws Exception {
        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setRole(Role.ADMIN);
        String adminResponse = this.mvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(adminUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        adminUser = objectMapper.readValue(adminResponse, User.class);

        normalUser = new User();
        normalUser.setUsername("user1");
        normalUser.setPassword("userpass");
        normalUser.setRole(Role.USER);
        String normalResponse = this.mvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(normalUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        normalUser = objectMapper.readValue(normalResponse, User.class);

        category = new Category();
        category.setName("Technology");
        String categoryResponse = this.mvc.perform(post("/api/categories?userId=" + adminUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        category = objectMapper.readValue(categoryResponse, Category.class);

        String discussionJson = String.format(
                "{\"title\":\"AI Discussion\",\"user\":{\"id\":%d},\"category\":{\"id\":%d},\"blocked\":false}",
                normalUser.getId(), category.getId()
        );

        String discussionResponse = this.mvc.perform(post("/api/discussions")
                        .contentType("application/json")
                        .content(discussionJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        discussion = objectMapper.readValue(discussionResponse, Discussion.class);
    }

    @Test
    public void userControllerExist() throws Exception {
        String controllerExpectedFqdn = "ch.hearc.jee2024.discussionappproject.controller.UserController";

        try {
            Class<?> controller = Class.forName(controllerExpectedFqdn);
            boolean isAnnotation = controller.isAnnotationPresent(RestController.class);
            assertTrue(isAnnotation, String.format("Controller %s is not a RestController", controllerExpectedFqdn));

        } catch (ClassNotFoundException ex) {
            fail(String.format("Controller %s doesn't exist", controllerExpectedFqdn));
        }
    }

    @Test
    public void callPostUserShouldAddUser() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("newpass");
        user.setRole(Role.USER);

        this.mvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"newuser\",\"role\":\"USER\"}"));
    }

    @Test
    public void callGetAllUsersShouldReturnUsers() throws Exception {
        this.mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callGetUserByIdShouldReturnUser() throws Exception {
        this.mvc.perform(get("/api/users/" + normalUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callDeleteUserShouldRemoveUser() throws Exception {
        this.mvc.perform(delete("/api/discussions/" + discussion.getId() + "?userId=" + adminUser.getId()))
                .andExpect(status().isNoContent());

        this.mvc.perform(delete("/api/users/" + normalUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void callGetDiscussionsByUserShouldReturnDiscussions() throws Exception {
        this.mvc.perform(get("/api/users/" + normalUser.getId() + "/discussions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callPostCategoryShouldAddCategory() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("Science");

        this.mvc.perform(post("/api/categories?userId=" + adminUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Science\"}"));
    }

    @Test
    public void callGetAllCategoriesShouldReturnCategories() throws Exception {
        this.mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callGetDiscussionsByCategoryShouldReturnDiscussions() throws Exception {
        this.mvc.perform(get("/api/categories/" + category.getId() + "/discussions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callDeleteCategoryShouldRemoveCategory() throws Exception {
        this.mvc.perform(delete("/api/categories/" + category.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void callGetAllDiscussionsShouldReturnDiscussions() throws Exception {
        this.mvc.perform(get("/api/discussions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callGetDiscussionByIdShouldReturnDiscussion() throws Exception {
        this.mvc.perform(get("/api/discussions/" + discussion.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callBlockDiscussionShouldBlockDiscussion() throws Exception {
        this.mvc.perform(put("/api/discussions/" + discussion.getId() + "/block?userId=" + adminUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Discussion blocked successfully."));
    }

    @Test
    public void callUnblockDiscussionShouldUnblockDiscussion() throws Exception {
        this.mvc.perform(put("/api/discussions/" + discussion.getId() + "/unblock?userId=" + adminUser.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void callDeleteDiscussionShouldRemoveDiscussion() throws Exception {
        this.mvc.perform(delete("/api/discussions/" + discussion.getId() + "?userId=" + adminUser.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void callCreateResponseShouldAddResponse() throws Exception {
        String responseJson = String.format("{\"content\":\"This is a response\",\"discussion\":{\"id\":%d},\"user\":{\"id\":%d}}",
                discussion.getId(), normalUser.getId());

        this.mvc.perform(post("/api/responses")
                        .contentType("application/json")
                        .content(responseJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"content\":\"This is a response\"}"));
    }

    @Test
    public void callGetAllResponsesShouldReturnResponses() throws Exception {
        this.mvc.perform(get("/api/responses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callGetResponseByIdShouldReturnResponse() throws Exception {
        String responseJson = String.format("{\"content\":\"Response content\",\"discussion\":{\"id\":%d},\"user\":{\"id\":%d}}",
                discussion.getId(), normalUser.getId());

        String responseContent = this.mvc.perform(post("/api/responses")
                        .contentType("application/json")
                        .content(responseJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Response response = objectMapper.readValue(responseContent, Response.class);

        this.mvc.perform(get("/api/responses/" + response.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void callDeleteResponseWithUserShouldNotRemoveResponse() throws Exception {
        String responseJson = String.format("{\"content\":\"Response content\",\"discussion\":{\"id\":%d},\"user\":{\"id\":%d}}",
                discussion.getId(), normalUser.getId());

        String responseContent = this.mvc.perform(post("/api/responses")
                        .contentType("application/json")
                        .content(responseJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Response response = objectMapper.readValue(responseContent, Response.class);

        this.mvc.perform(delete("/api/responses/" + response.getId() + "?userId=" + normalUser.getId()))
                .andExpect(status().isForbidden());
    }
}
