package com.zpjj.musicapp.musicianmanagementapp;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.exceptions.UserNotFoundException;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by daniel on 15.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private UserService userService;
    FirebaseDatabase mDatabase;
    @Before
    public void setUp() {
        mDatabase = Mockito.mock(FirebaseDatabase.class);
        Mockito.when(mDatabase.getReference("users")).thenReturn(Mockito.mock(DatabaseReference.class));
        Mockito.when(mDatabase.getReference("bands")).thenReturn(Mockito.mock(DatabaseReference.class));
        userService = new UserService(mDatabase);
    }

    @Test(expected = NullPointerException.class)
    public void userHasBandShouldThrowNullPointerExceptionWhenIsNull() throws UserNotFoundException {
        userService.userHasBand(null);
    }
    @Test(expected = UserNotFoundException.class)
    public void userHasBandShouldThrowUserNotFoundExceptionIfNotExistInUserList() throws UserNotFoundException {
        FirebaseUser user = Mockito.mock(FirebaseUser.class);
        userService.userHasBand(user);
    }

    @Test
    public void userHasBandShouldReturnFalseIfBandListIsEmpty() throws UserNotFoundException {
        FirebaseUser user = Mockito.mock(FirebaseUser.class);
        user.updateEmail("test@test.pl");
        Mockito.when(mDatabase.getReference("users").child(user.getEmail())).thenReturn(Mockito.mock(DatabaseReference.class));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserBands(new HashMap<>());
        mDatabase.getReference().child("users").child(user.getEmail()).setValue(userInfo);
        userService.userHasBand(user).subscribe(
                data -> {
                    Assert.assertEquals(false, data);
                }
        );
    }
}
