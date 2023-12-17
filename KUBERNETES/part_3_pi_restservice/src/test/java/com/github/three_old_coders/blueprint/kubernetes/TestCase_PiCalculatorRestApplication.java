package com.github.three_old_coders.blueprint.kubernetes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)    // missing in ChatGPT
@AutoConfigureMockMvc
@SpringBootTest
public class TestCase_PiCalculatorRestApplication
{
    @Autowired private MockMvc _mockMvc;

    @Test
    public void calculatePi_withValidPrecision_shouldReturnCalculatedPi() throws Exception
    {
        int precision = 5; // Set your desired precision for the test

        _mockMvc.perform(MockMvcRequestBuilders.get("/calculatePi/{precision}", precision))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("3.14159"));
    }

    @Test
    public void calculatePi_withNegativePrecision_shouldReturnErrorMessage() throws Exception
    {
        int precision = -1; // Set a negative precision for the test

        _mockMvc.perform(MockMvcRequestBuilders.get("/calculatePi/{precision}", precision))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Precision must be a non-negative integer."));
    }
}
