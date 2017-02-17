package com.tts.codelab.oauth2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tts.codelab.oauth2.domain.User;

@Service
public class DummyUserDetailServiceImpl implements DummyUserDetailService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        switch (username) {
        case "read":
            user.setUsername("read");
            user.setPassword("$2a$10$9B6sWlWF64x2oL6qsn98hO3KfTRAcMof11nnWlE4kysZ2NQ.BO6Y.");//read
            user.setRole("FOO_READ");
            break;

        case "write":
            user.setUsername("write");
            user.setPassword("$2a$10$PlUD5AyfdfZz80tYuFL12uAWoZmdVbc9OpGSKY0WfLgyoXdqnoaW.");//write
            user.setRole("FOO_READ", "FOO_WRITE");
            break;
            
        default:
            break;
        }
        return user;
    }

}
