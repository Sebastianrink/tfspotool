package de.datev.tfspotool;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class TestResourceTest {

    @Test
    public void getGoals() throws Exception {
        //arrange
        TestResource testResource = new TestResource();

        //act
        Response response = testResource.getGoals();

        //assert
        Assert.assertNotNull(response);
    }

}