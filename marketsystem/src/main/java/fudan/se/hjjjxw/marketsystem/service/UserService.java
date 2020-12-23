package fudan.se.hjjjxw.marketsystem.service;


import fudan.se.hjjjxw.marketsystem.entity.User;
import fudan.se.hjjjxw.marketsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@Service
@RestController
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/creat")
    public void createUser(String name){
        User user = new User(name);
        User user2 = new User("test_user");
        userRepository.save(user2);
    }

    @RequestMapping(value = "/findUser")
    public User findUser(String name){
        return userRepository.findUserByName("test_user");
    }

}
