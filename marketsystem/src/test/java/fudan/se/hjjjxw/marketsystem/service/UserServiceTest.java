package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser() {

        userService.createUser("junit!");
        Assertions.assertNotNull(userService.findUser("junit!"));
    }

    @Test
    void findUser() {
        User user= userService.findUser("test_user");
        Assertions.assertNotNull(user);
    }
}